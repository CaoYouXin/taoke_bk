package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.MiquanerApplication;
import com.taoke.miquaner.data.ESharePage;
import com.taoke.miquaner.repo.SharePageRepo;
import com.taoke.miquaner.serv.IShareServ;
import com.taoke.miquaner.util.DateUtils;
import com.taoke.miquaner.util.Result;
import com.taoke.miquaner.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class ShareServImpl implements IShareServ {

    private final Environment env;
    private final SharePageRepo sharePageRepo;
    private final int expire;

    @Autowired
    public ShareServImpl(Environment env, SharePageRepo sharePageRepo) {
        this.env = env;
        this.sharePageRepo = sharePageRepo;
        this.expire = 0 - Integer.parseInt(env.getProperty("taoke.share.expired"));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Object shareSave(String value) {
        Date now = new Date();
        List<ESharePage> toWrite = this.sharePageRepo.findTop1ByCreateTimeLessThanOrderByCreateTimeAsc(
                DateUtils.add(now, Calendar.DAY_OF_YEAR, this.expire));

        if (toWrite.isEmpty()) {
            toWrite.add(new ESharePage());
        }

        ESharePage sharePage = toWrite.get(0);
        String key = StringUtils.getMD5(value);
        sharePage.setValue(value);
        sharePage.setCreateTime(now);

        ESharePage oneByKeyEquals = null;
        Random random = null;
        do {
            oneByKeyEquals = this.sharePageRepo.findOneByKeyEquals(key);
            if (null != oneByKeyEquals) {
                if (null == random) {
                    random = new Random();
                }
                key = StringUtils.getMD5(Double.toString(random.nextDouble()));
            }
        } while (null != oneByKeyEquals);

        sharePage.setKey(key);
        this.sharePageRepo.save(sharePage);

        return Result.success(sharePage.getKey());
    }

    @Override
    public Object shareFetch(String key) {
        ESharePage oneByKeyEquals = this.sharePageRepo.findOneByKeyEquals(key);
        if (null == oneByKeyEquals) {
            return Result.success("");
        }
        return Result.success(oneByKeyEquals.getValue());
    }

}
