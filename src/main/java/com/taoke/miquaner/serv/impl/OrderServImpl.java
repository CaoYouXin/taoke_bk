package com.taoke.miquaner.serv.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mysql.jdbc.StringUtils;
import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.*;
import com.taoke.miquaner.repo.*;
import com.taoke.miquaner.serv.IMsgServ;
import com.taoke.miquaner.serv.IOrderServ;
import com.taoke.miquaner.serv.ISmsServ;
import com.taoke.miquaner.serv.ITbkServ;
import com.taoke.miquaner.util.DivideByTenthUtil;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.ExportUtils;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.view.TbkOrderView;
import com.taoke.miquaner.view.TbkOrderWrapper;
import com.taoke.miquaner.view.UserCommitView;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServImpl implements IOrderServ {

    private static final Logger logger = LogManager.getLogger(OrderServImpl.class);

    private static final String NO_TITLE_FOUND = "没找到标题";
    private static final String NO_COL_HANDLER_FOUND = "表格格式有变，请升级服务程序后再上传这批订单";
    private static final String WRONG_SEARCH_TYPE = "查询类型错误";
    private static final String AT_LEAST_TEN = "不满足最小提现金额";
    private static final String NO_THAT_MUCH = "可提现金额不足";

    private final TbkOrderRepo tbkOrderRepo;
    private final WithdrawRepo withdrawRepo;
    private final ConfigRepo configRepo;
    private final UserRepo userRepo;
    private final AdminRepo adminRepo;
    private final ITbkServ tbkServ;
    private final IMsgServ msgServ;
    private final ISmsServ smsServ;
    private Converter<EWithdraw, EWithdraw> eWithdrawConverter = eWithdraw -> {
        EUser user = eWithdraw.getUser();
        EUser viewUser = new EUser();
        BeanUtils.copyProperties(user, viewUser, "pUser", "cUsers", "withdraws", "sentMails", "receivedMails", "createdMessages");
        eWithdraw.setUser(viewUser);
        return eWithdraw;
    };
    private Function<EWithdraw, List<String>> eWithdrawListFunction = eWithdraw -> {
        List<String> row = new ArrayList<>();
        row.add(eWithdraw.getId().toString());
        row.add(String.format("%s(%s)", eWithdraw.getUser().getName(), eWithdraw.getUser().getRealName()));
        row.add(eWithdraw.getAmount());
        row.add(MiquanerApplication.DEFAULT_DATE_FORMAT.format(eWithdraw.getCreateTime()));
        row.add(eWithdraw.getPayed() ? "已支付" : "未支付");
        row.add(null == eWithdraw.getPayTime() ? "" : MiquanerApplication.DEFAULT_DATE_FORMAT.format(eWithdraw.getPayTime()));
        return row;
    };

    @Autowired
    public OrderServImpl(ISmsServ smsServ, ITbkServ tbkServ, IMsgServ msgServ, AdminRepo adminRepo,
                         TbkOrderRepo tbkOrderRepo, WithdrawRepo withdrawRepo, ConfigRepo configRepo,
                         UserRepo userRepo) {
        this.smsServ = smsServ;
        this.tbkServ = tbkServ;
        this.msgServ = msgServ;
        this.adminRepo = adminRepo;
        this.tbkOrderRepo = tbkOrderRepo;
        this.withdrawRepo = withdrawRepo;
        this.configRepo = configRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Object upload(String filePath) throws IOException {
        HSSFWorkbook wb = this.readFile(filePath);
        for (int k = 0; k < wb.getNumberOfSheets(); k++) {
            HSSFSheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();

            HSSFRow titleRow = sheet.getRow(0);
            if (titleRow == null) {
                return Result.fail(new ErrorR(ErrorR.NO_TITLE_FOUND, NO_TITLE_FOUND));
            }

            List<TbkOrderWrapper.TOW> towList = new ArrayList<>();
            List<Method> methodList = new ArrayList<>();
            Method[] declaredMethods = TbkOrderWrapper.class.getDeclaredMethods();
            for (int c = 0; c < titleRow.getLastCellNum(); c++) {
                HSSFCell cell = titleRow.getCell(c);
                String title = cell.getStringCellValue();
                boolean found = false;
                int count = 0;
                for (Method declaredMethod : declaredMethods) {
                    TbkOrderWrapper.TOW tow = declaredMethod.getDeclaredAnnotation(TbkOrderWrapper.TOW.class);
                    if (null == tow) {
                        logger.debug(declaredMethod.getName() + "没有TOW ANNOTATION : " + ++count);
                        continue;
                    }

                    if (!tow.value().equals(title)) {
                        continue;
                    }

                    towList.add(tow);
                    methodList.add(declaredMethod);
                    found = true;
                    break;
                }

                if (!found) {
                    logger.error(NO_COL_HANDLER_FOUND + " --> " + title);
                    return Result.fail(new ErrorR(ErrorR.NO_COL_HANDLER_FOUND, NO_COL_HANDLER_FOUND));
                }
            }

            List<ETbkOrder> toSave = new ArrayList<>();
            for (int r = 1; r < rows; r++) {
                HSSFRow row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }

                TbkOrderWrapper tbkOrderWrapper = new TbkOrderWrapper();
                HSSFCell cell = null;
                TbkOrderWrapper.TOW tow = null;
                Method method = null;
                try {
                    for (int c = 0; c < row.getLastCellNum(); c++) {
                        cell = row.getCell(c);
                        tow = towList.get(c);
                        method = methodList.get(c);

                        processCell(tbkOrderWrapper, cell, tow, method);
                    }
                } catch (Exception e) {
                    logger.error(String.format("第%d行第%d列 cellType=%s %s TOW: %s", row.getRowNum(),
                            cell != null ? cell.getColumnIndex() : -1,
                            cell != null && cell.getCellTypeEnum() != null ? cell.getCellTypeEnum().toString() : "unknown type",
                            method != null ? method.getName() : "unknown",
                            tow != null ? String.format("%s type=%d, sd=%b, ds=%b, dl=%b sl=%b",
                                    tow.value(), tow.type(), tow.string2date(), tow.double2string(),
                                    tow.double2long(), tow.string2long()) : "unknown"), e);
                    return Result.fail(new ErrorR(ErrorR.NO_COL_HANDLER_FOUND, NO_COL_HANDLER_FOUND));
                }

                ETbkOrder entity = tbkOrderWrapper.getEntity();
                ETbkOrder one = this.tbkOrderRepo.findByOrderIdEqualsAndItemNumIidEquals(entity.getOrderId(), entity.getItemNumIid());
                if (null == one) {
                    one = new ETbkOrder();
                }
                entity.setId(one.getId());
                BeanUtils.copyProperties(entity, one);
                toSave.add(one);
            }

            ArrayList<ETbkOrder> collected = toSave.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(ETbkOrder.staticHash, Function.identity(), (a, b) -> {
                                a.setItemCount(a.getItemCount() + b.getItemCount());
                                a.setEstimateEffect(String.format(Locale.ENGLISH, "%.2f",
                                        Double.parseDouble(a.getEstimateEffect())
                                                + Double.parseDouble(b.getEstimateEffect())));
                                a.setEstimateIncome(String.format(Locale.ENGLISH, "%.2f",
                                        Double.parseDouble(a.getEstimateIncome())
                                                + Double.parseDouble(b.getEstimateIncome())));
                                a.setPayedAmount(String.format(Locale.ENGLISH, "%.2f",
                                        Double.parseDouble(a.getPayedAmount())
                                                + Double.parseDouble(b.getPayedAmount())));
                                a.setSettleAmount(String.format(Locale.ENGLISH, "%.2f",
                                        Double.parseDouble(a.getSettleAmount())
                                                + Double.parseDouble(b.getSettleAmount())));
                                a.setCommissionAmount(String.format(Locale.ENGLISH, "%.2f",
                                        Double.parseDouble(a.getCommissionAmount())
                                                + Double.parseDouble(b.getCommissionAmount())));
                                return a;
                            }),
                            m -> new ArrayList<>(m.values())));
            this.tbkOrderRepo.save(collected);
        }

        return Result.success(null);
    }

    @Override
    public Object list(EUser user, Boolean isSuper, Integer type, Integer pageNo) {
        return Result.success(this.listOrders(user, isSuper, type, pageNo, 40));
    }

    private List<TbkOrderView> listOrders(EUser user, Boolean isSuper, Integer type, Integer pageNo, Integer pageSize) {
        DivideByTenthUtil.Tenth tenth = DivideByTenthUtil.get(this.configRepo);
        double userRate = isSuper ? (1 - tenth.platform) : tenth.second;

        user = this.userRepo.findOne(user.getId());
        Long siteId = getSiteId(user.getAliPid());
        Long adZoneId = getAdZoneId(user.getAliPid());

        List<EUser> cUsers = user.getcUsers();
        List<Long> adZoneIds = new ArrayList<>(cUsers.stream()
                .filter(cUser -> !StringUtils.isNullOrEmpty(cUser.getAliPid()))
                .map(cUser -> getAdZoneId(cUser.getAliPid())).collect(Collectors.toList()));
        adZoneIds.add(adZoneId);

        List<ETbkOrder> orders = null;
        pageNo = Math.max(0, --pageNo);
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, new Sort(Sort.Direction.DESC, "createTime"));
        switch (type) {
            case 1:
                orders = this.tbkOrderRepo.findBySiteIdEqualsAndAdZoneIdIn(
                        siteId, adZoneIds,
                        pageRequest
                );
                break;
            case 2:
                orders = this.tbkOrderRepo.findBySiteIdEqualsAndAdZoneIdInAndOrderStatusNotContains(
                        siteId, adZoneIds, "失效",
                        pageRequest
                );
                break;
            case 3:
                orders = this.tbkOrderRepo.findBySiteIdEqualsAndAdZoneIdInAndOrderStatusContains(
                        siteId, adZoneIds, "付款",
                        pageRequest
                );
                break;
            case 4:
                orders = this.tbkOrderRepo.findBySiteIdEqualsAndAdZoneIdInAndOrderStatusContains(
                        siteId, adZoneIds, "收货",
                        pageRequest
                );
                break;
            case 5:
                orders = this.tbkOrderRepo.findBySiteIdEqualsAndAdZoneIdInAndOrderStatusContains(
                        siteId, adZoneIds, "结算",
                        pageRequest
                );
                break;
            case 6:
                orders = this.tbkOrderRepo.findBySiteIdEqualsAndAdZoneIdInAndOrderStatusContains(
                        siteId, adZoneIds, "失效",
                        pageRequest
                );
                break;
            default:
                return new ArrayList<>();
        }

        logger.debug(String.format("page %d found %d", pageNo, orders.size()));
        Map<Long, ETbkItem> longETbkItemMap = this.tbkServ.loadSimpleItem(orders.stream().map(eTbkOrder -> eTbkOrder.getItemNumIid()).collect(Collectors.toList()));

        return orders.stream().map(eTbkOrder -> {
            boolean self = eTbkOrder.getAdZoneId().equals(adZoneId);
            double rate = self ? userRate : tenth.first;

            eTbkOrder.setCommissionRate(null);
            eTbkOrder.setEstimateEffect(String.format(Locale.ENGLISH, "%.2f",
                    rate * Double.parseDouble(eTbkOrder.getEstimateEffect())));
            eTbkOrder.setEstimateIncome(String.format(Locale.ENGLISH, "%.2f",
                    rate * Double.parseDouble(eTbkOrder.getEstimateIncome())));

            TbkOrderView view = new TbkOrderView();
            BeanUtils.copyProperties(eTbkOrder, view);
            view.setSelf(self);

            if (!self) {
                String teammateName = "";
                for (EUser cUser : cUsers) {
                    if (StringUtils.isNullOrEmpty(cUser.getAliPid())) {
                        continue;
                    }
                    if (getAdZoneId(cUser.getAliPid()).equals(eTbkOrder.getAdZoneId())) {
                        teammateName = cUser.getName();
                        break;
                    }
                }
                view.setTeammateName(teammateName);
            }

            ETbkItem eTbkItem = longETbkItemMap.get(eTbkOrder.getItemNumIid());
            if (null != eTbkItem) {
                view.setPicUrl(eTbkItem.getPicUrl());
            }

            return view;
        }).collect(Collectors.toList());
    }

    @Override
    public Object getChildUserCommit(List<EUser> children) {
        DivideByTenthUtil.Tenth tenth = DivideByTenthUtil.get(this.configRepo);

        return Result.success(children.stream().map(user -> {
            if (StringUtils.isNullOrEmpty(user.getAliPid())) {
                return new UserCommitView(user.getName(), "0.00");
            }

            return getUserCommitView(user, tenth.first);
        }).collect(Collectors.toList()));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Object withdraw(EUser user, Double amount, Boolean isSuper) {
        if (amount < 10.0) {
            return Result.fail(new ErrorR(ErrorR.AT_LEAST_TEN, AT_LEAST_TEN));
        }

        Result canDraw = (Result) this.canDraw(user, isSuper);
        if (Double.parseDouble((String) canDraw.getBody()) < amount) {
            return Result.fail(new ErrorR(ErrorR.NO_THAT_MUCH, NO_THAT_MUCH));
        }

        EWithdraw withdraw = new EWithdraw();
        withdraw.setCreateTime(new Date());
        withdraw.setUser(this.userRepo.findOne(user.getId()));
        withdraw.setAmount(String.format(Locale.ENGLISH, "%.2f", amount));
        withdraw.setPayed(false);
        this.withdrawRepo.save(withdraw);

        List<EAdmin> admins = this.adminRepo.findAllByGrantedAdminsIsNull();
        if (!admins.isEmpty()) {
            EUser retouchedUser = this.userRepo.findOne(user.getId());
            this.msgServ.send2One(admins.get(0), retouchedUser, "系统消息", String.format("您已发起一笔%s的提现请求！", withdraw.getAmount()));
        }

        return Result.success(null);
    }

    @Override
    public Object canDraw(EUser user, Boolean isSuper) {
        if (StringUtils.isNullOrEmpty(user.getAliPid())) {
            return Result.success("0.00");
        }

        DivideByTenthUtil.Tenth tenth = DivideByTenthUtil.get(this.configRepo);
        double userRate = isSuper ? (1 - tenth.platform) : tenth.second;

        user = this.userRepo.findOne(user.getId());
        UserCommitView userCommitView = getUserCommitView(user, userRate);
        Double childCommit = user.getcUsers().stream().map(cUser -> {
            if (StringUtils.isNullOrEmpty(cUser.getAliPid())) {
                return new UserCommitView(cUser.getName(), "0.00");
            }

            return getUserCommitView(cUser, tenth.first);
        }).reduce(0.0, (pv, cO) -> pv + Double.parseDouble(cO.getCommit()), (v1, v2) -> v1 + v2);

        Double hasDraw = this.withdrawRepo.findAllByUserEquals(user).stream()
                .reduce(0.0, (pv, cO) -> pv + Double.parseDouble(cO.getAmount()), (v1, v2) -> v1 + v2);

        return Result.success(String.format(Locale.ENGLISH, "%.2f",
                Double.parseDouble(userCommitView.getCommit()) + childCommit - hasDraw));
    }

    @Override
    public Object lastMonthSettled(EUser user, Boolean isSuper) {
        if (StringUtils.isNullOrEmpty(user.getAliPid())) {
            return Result.success("0.00");
        }

        DivideByTenthUtil.Tenth tenth = DivideByTenthUtil.get(this.configRepo);
        double userRate = isSuper ? (1 - tenth.platform) : tenth.second;

        user = this.userRepo.findOne(user.getId());
        Long siteId = getSiteId(user.getAliPid());
        Long adZoneId = getAdZoneId(user.getAliPid());

        List<EUser> cUsers = user.getcUsers();
        List<Long> adZoneIds = new ArrayList<>(cUsers.stream()
                .filter(cUser -> !StringUtils.isNullOrEmpty(cUser.getAliPid()))
                .map(cUser -> getAdZoneId(cUser.getAliPid())).collect(Collectors.toList()));
        adZoneIds.add(adZoneId);

        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH);
        now.set(now.get(Calendar.YEAR), month, 1, 0, 0, 0);
        Date end = now.getTime(), start;
        if (month == Calendar.JANUARY) {
            now.set(now.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 1, 0, 0, 0);
            start = now.getTime();
        } else {
            now.set(now.get(Calendar.YEAR), month - 1, 1, 0, 0, 0);
            start = now.getTime();
        }

        Double settled = this.tbkOrderRepo.findAllBySiteIdEqualsAndAdZoneIdInAndOrderStatusContainsAndSettleTimeBetween(
                siteId, adZoneIds, "结算", start, end
        ).stream().reduce(0.0, (pv, cO) -> {
            double rate = cO.getAdZoneId().equals(adZoneId) ? userRate : tenth.first;
            return pv + Double.parseDouble(cO.getEstimateIncome()) * rate;
        }, (v1, v2) -> v1 + v2);

        return Result.success(String.format(Locale.ENGLISH, "%.2f", settled));
    }

    @Override
    public Object thisMonthSettled(EUser user, Boolean isSuper) {
        if (StringUtils.isNullOrEmpty(user.getAliPid())) {
            return Result.success("0.00");
        }

        DivideByTenthUtil.Tenth tenth = DivideByTenthUtil.get(this.configRepo);
        double userRate = isSuper ? (1 - tenth.platform) : tenth.second;

        user = this.userRepo.findOne(user.getId());
        Long siteId = getSiteId(user.getAliPid());
        Long adZoneId = getAdZoneId(user.getAliPid());

        List<EUser> cUsers = user.getcUsers();
        List<Long> adZoneIds = new ArrayList<>(cUsers.stream()
                .filter(cUser -> !StringUtils.isNullOrEmpty(cUser.getAliPid()))
                .map(cUser -> getAdZoneId(cUser.getAliPid())).collect(Collectors.toList()));
        adZoneIds.add(adZoneId);

        Calendar now = Calendar.getInstance();
        now.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), 1, 0, 0, 0);

        Double settled = this.tbkOrderRepo.findAllBySiteIdEqualsAndAdZoneIdInAndOrderStatusContainsAndSettleTimeBetween(
                siteId, adZoneIds, "结算", now.getTime(), new Date()
        ).stream().reduce(0.0, (pv, cO) -> {
            double rate = cO.getAdZoneId().equals(adZoneId) ? userRate : tenth.first;
            return pv + Double.parseDouble(cO.getEstimateIncome()) * rate;
        }, (v1, v2) -> v1 + v2);
        return Result.success(String.format(Locale.ENGLISH, "%.2f", settled));
    }

    @Override
    public Object userWithdrawList(Integer type, Integer pageNo) {
        pageNo = Math.max(0, --pageNo);
        int pageSize = 15;
        PageRequest pageRequest = new PageRequest(pageNo, pageSize, Sort.Direction.ASC, "id");
        Page<EWithdraw> fetched = null;
        switch (type) {
            case 1:
                fetched = this.withdrawRepo.findAll(pageRequest);
                break;
            case 2:
                fetched = this.withdrawRepo.findAllByPayedEquals(true, pageRequest);
                break;
            case 3:
                fetched = this.withdrawRepo.findAllByPayedEquals(false, pageRequest);
                break;
            default:
                return Result.success(new PageImpl<EWithdraw>(new ArrayList<>()));
        }

        return Result.success(fetched.map(eWithdrawConverter));
    }

    @Override
    public Object userWithdrawList(String key) {
        List<EUser> matchedUsers = this.userRepo.findAllByNameContainsOrRealNameContainsOrAliPayIdContainsOrPhoneContains(key, key, key, key);
        if (matchedUsers.isEmpty()) {
            return Result.success(new PageImpl<EWithdraw>(new ArrayList<>()));
        }

        List<EWithdraw> results = new ArrayList<>();
        matchedUsers.forEach(eUser -> results.addAll(eUser.getWithdraws()));

        return Result.success(new PageImpl<>(
                results.stream().map(eWithdraw -> eWithdrawConverter.convert(eWithdraw)).collect(Collectors.toList())
        ));
    }

    @Override
    public Object payUserWithdraw(Long withdrawId) {
        EWithdraw one = this.withdrawRepo.findOne(withdrawId);
        if (null == one) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
        }

        one.setPayed(true);
        one.setPayTime(new Date());
        this.withdrawRepo.save(one);

        EUser user = one.getUser();
        EUser viewUser = new EUser();
        BeanUtils.copyProperties(user, viewUser, "pUser", "cUsers", "withdraws", "sentMails", "receivedMails", "createdMessages");
        one.setUser(viewUser);

        Map<String, String> params = new HashMap<>();
        params.put("name", user.getRealName());
        params.put("time", MiquanerApplication.DEFAULT_DATE_FORMAT.format(one.getCreateTime()));
        params.put("amount", one.getAmount());
        try {
            this.smsServ.send("SMS_115755120", user.getPhone(), params);
        } catch (JsonProcessingException e) {
            logger.error("sms param json error", e);
        }

        return Result.success(one);
    }

    @Override
    public boolean exportWithdraw(String filePath, Integer type) {
        List<EWithdraw> fetched;
        switch (type) {
            case 1:
                fetched = this.withdrawRepo.findAll();
                break;
            case 2:
                fetched = this.withdrawRepo.findAllByPayedEquals(true);
                break;
            case 3:
                fetched = this.withdrawRepo.findAllByPayedEquals(false);
                break;
            default:
                fetched = new ArrayList<>();
        }

        List<List<String>> data = new ArrayList<>();
        data.add(getHeaders());
        data.addAll(fetched.stream().map(eWithdrawListFunction).collect(Collectors.toList()));
        return ExportUtils.writeFile(filePath, data, getColWidths());
    }

    @Override
    public boolean exportWithdraw(String filePath, String key) {
        List<EUser> matchedUsers = this.userRepo.findAllByNameContainsOrRealNameContainsOrAliPayIdContainsOrPhoneContains(key, key, key, key);
        List<EWithdraw> fetched = new ArrayList<>();

        matchedUsers.forEach(eUser -> fetched.addAll(eUser.getWithdraws()));

        List<List<String>> data = new ArrayList<>();
        data.add(getHeaders());
        data.addAll(fetched.stream().map(eWithdrawListFunction).collect(Collectors.toList()));
        return ExportUtils.writeFile(filePath, data, getColWidths());
    }

    private List<String> getHeaders() {
        return Arrays.asList("ID", "用户名", "提现金额", "创建时间", "状态", "支付时间");
    }

    private List<Integer> getColWidths() {
        return Arrays.asList(9, 12, 10, 15, 9, 15);
    }

    private UserCommitView getUserCommitView(EUser user, final Double percent) {
        Double settled = this.tbkOrderRepo.findAllBySiteIdEqualsAndAdZoneIdEqualsAndOrderStatusContainsAndSettleTimeBefore(
                getSiteId(user.getAliPid()), getAdZoneId(user.getAliPid()), "结算", this.getNearestSettleEndTime()
        ).stream().reduce(0.0, (pv, cv) -> pv + Double.parseDouble(cv.getEstimateIncome()) * percent, (v1, v2) -> v1 + v2);
        logger.debug(String.format("%s settled %.2f", user.getName(), settled));
        return new UserCommitView(user.getName(), String.format(Locale.ENGLISH, "%.2f", settled));
    }

    private Date getNearestSettleEndTime() {
        Calendar now = Calendar.getInstance();
        int month = now.get(Calendar.MONTH);
        if (now.get(Calendar.DAY_OF_MONTH) > 21) {
            now.set(now.get(Calendar.YEAR), month, 1, 0, 0, 0);
        } else {
            if (month == Calendar.JANUARY) {
                now.set(now.get(Calendar.YEAR) - 1, Calendar.DECEMBER, 1, 0, 0, 0);
            } else {
                now.set(now.get(Calendar.YEAR), month - 1, 1, 0, 0, 0);
            }
        }
        return now.getTime();
    }

    private Long getSiteId(String pid) {
        int start = pid.indexOf('_') + 1;
        start = pid.indexOf('_', start) + 1;
        int end = pid.indexOf('_', start);
        return Long.parseLong(pid.substring(start, end));
    }

    private Long getAdZoneId(String pid) {
        int start = pid.indexOf('_') + 1;
        start = pid.indexOf('_', start) + 1;
        start = pid.indexOf('_', start) + 1;
        return Long.parseLong(pid.substring(start));
    }

    private void processCell(TbkOrderWrapper tbkOrderWrapper, HSSFCell cell, TbkOrderWrapper.TOW tow, Method method) throws IllegalAccessException, InvocationTargetException, ParseException {
        switch (cell.getCellTypeEnum()) {

            case NUMERIC:
                Double numericCellValue = cell.getNumericCellValue();

                if (tow.double2long()) {
                    method.invoke(tbkOrderWrapper, numericCellValue.longValue());
                    break;
                }

                if (tow.double2string()) {
                    method.invoke(tbkOrderWrapper, String.format(Locale.ENGLISH, "%.2f", numericCellValue));
                    break;
                }

                break;

            case STRING:

                String stringCellValue = cell.getStringCellValue();

                if (tow.string2date()) {
                    if (!StringUtils.isNullOrEmpty(stringCellValue)) {
                        method.invoke(tbkOrderWrapper, MiquanerApplication.DEFAULT_DATE_FORMAT.parse(stringCellValue));
                    }
                    break;
                }

                if (tow.string2long()) {
                    if (!StringUtils.isNullOrEmpty(stringCellValue)) {
                        method.invoke(tbkOrderWrapper, Long.parseLong(stringCellValue));
                    }
                    break;
                }

                method.invoke(tbkOrderWrapper, stringCellValue);
                break;

            default:
//                throw new RuntimeException();
        }
    }

    private HSSFWorkbook readFile(String filename) throws IOException {
        try (FileInputStream fis = new FileInputStream(filename)) {
            return new HSSFWorkbook(fis);
        }
    }

}
