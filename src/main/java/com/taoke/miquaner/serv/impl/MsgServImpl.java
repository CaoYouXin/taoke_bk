package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EAdmin;
import com.taoke.miquaner.data.EMailBox;
import com.taoke.miquaner.data.EMessage;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.AdminRepo;
import com.taoke.miquaner.repo.MailBoxRepo;
import com.taoke.miquaner.repo.MessageRepo;
import com.taoke.miquaner.repo.UserRepo;
import com.taoke.miquaner.serv.IMsgServ;
import com.taoke.miquaner.util.ErrorR;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class MsgServImpl implements IMsgServ {

    private static final String WRONG_MESSAGE_ID = "正在确认错误的消息";
    private MessageRepo messageRepo;
    private MailBoxRepo mailBoxRepo;
    private UserRepo userRepo;
    private AdminRepo adminRepo;

    @Autowired
    public MsgServImpl(MessageRepo messageRepo, MailBoxRepo mailBoxRepo, UserRepo userRepo, AdminRepo adminRepo) {
        this.messageRepo = messageRepo;
        this.mailBoxRepo = mailBoxRepo;
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
    }

    @Override
    public Object listMessages(EUser user, Integer pageNo) {
        return Result.success(this.mailBoxRepo.findByReceiverUserEquals(user,
                new PageRequest(pageNo - 1, 10, new Sort(Sort.Direction.DESC, "createTime")))
                .stream().peek(new MailBoxConsumer()).collect(Collectors.toList()));
    }

    private static class MailBoxConsumer implements Consumer<EMailBox> {

        @Override
        public void accept(EMailBox eMailBox) {
            eMailBox.setReceiverUser(mapUser(eMailBox.getReceiverUser()));
            eMailBox.setSenderUser(mapUser(eMailBox.getSenderUser()));
            eMailBox.getMessage().setMailBoxes(null);
            eMailBox.getMessage().setUser(null);
            eMailBox.getMessage().setAdmin(null);
        }

    }

    private static EUser mapUser(EUser user) {
        if (null == user) {
            return null;
        }
        EUser ret = new EUser();
        ret.setName(user.getName());
        return ret;
    }

    private static EAdmin mapAdmin(EAdmin admin) {
        EAdmin ret = new EAdmin();
        ret.setName(admin.getName());
        return ret;
    }

    @Override
    public Object getUncheckedMessageCount(EUser user) {
        return Result.success(this.mailBoxRepo.countAllByReceiverUserEqualsAndCheckedEquals(user, false));
    }

    @Override
    @Transactional
    public Object send2All(EAdmin admin, String title, String content) {
        EAdmin reattachedAdmin = this.adminRepo.findOne(admin.getId());

        EMessage message = new EMessage();
        message.setTitle(title);
        message.setContent(content);
        message.setAdmin(reattachedAdmin);
        EMessage savedMessage = this.messageRepo.save(message);

        final Date createTime = new Date();
        List<EMailBox> toSave = this.userRepo.findAll().stream().map(user -> {
            EMailBox mailBox = new EMailBox();
            mailBox.setCreateTime(createTime);
            mailBox.setReceiverUser(user);
            mailBox.setAdminToReceive(false);
            mailBox.setSendFromAdmin(true);
            mailBox.setChecked(false);
            mailBox.setMessage(savedMessage);
            return mailBox;
        }).collect(Collectors.toList());
        this.mailBoxRepo.save(toSave);

        EMessage ret = new EMessage();
        BeanUtils.copyProperties(savedMessage, ret, "admin", "user", "mailBoxes");
        return Result.success(ret);
    }

    @Override
    @Transactional
    public Object sendFeedback(EUser user, String content) {
        EUser reattachedUser = this.userRepo.findOne(user.getId());

        EMessage message = new EMessage();
        message.setTitle(String.format("来自%s(真名=%s)的反馈", user.getName(), null != user.getRealName() ? user.getRealName() : "unknown"));
        message.setContent(content);
        message.setUser(reattachedUser);
        EMessage savedMessage = this.messageRepo.save(message);

        EMailBox mailBox = new EMailBox();
        mailBox.setMessage(savedMessage);
        mailBox.setChecked(false);
        mailBox.setSendFromAdmin(false);
        mailBox.setSenderUser(reattachedUser);
        mailBox.setAdminToReceive(true);
        mailBox.setCreateTime(new Date());
        this.mailBoxRepo.save(mailBox);

        EMessage ret = new EMessage();
        BeanUtils.copyProperties(savedMessage, ret, "admin", "user", "mailBoxes");
        return Result.success(ret);
    }

    @Override
    public Object readMessage(EUser user, Long mailBoxId) {
        EMailBox one = this.mailBoxRepo.findOne(mailBoxId);
        if (null == one) {
            return Result.fail(new ErrorR(ErrorR.NO_ID_FOUND, ErrorR.NO_ID_FOUND_MSG));
        }

        if (!one.getReceiverUser().getId().equals(user.getId())) {
            return Result.fail(new ErrorR(ErrorR.WRONG_MESSAGE_ID, WRONG_MESSAGE_ID));
        }

        one.setChecked(true);
        this.mailBoxRepo.save(one);
        return Result.success(null);
    }

    @Override
    public Object readFeedback() {
        return Result.success(this.mailBoxRepo.findAll().stream().filter(eMailBox -> {
            EMessage message = eMailBox.getMessage();
            return message.getTitle().startsWith("来自") && message.getTitle().endsWith("反馈");
        }).peek(new MailBoxConsumer()).collect(Collectors.toList()));
    }

    @Override
    public Object getSend2All(EAdmin admin) {
        return Result.success(this.messageRepo.findAll().stream().filter(
                eMessage -> null != eMessage.getAdmin() && null == eMessage.getUser()
        ).peek(eMessage -> {
            eMessage.setMailBoxes(null);
            eMessage.setAdmin(null);
        }).collect(Collectors.toList()));
    }
}
