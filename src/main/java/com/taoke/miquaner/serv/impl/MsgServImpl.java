package com.taoke.miquaner.serv.impl;

import com.taoke.miquaner.data.EMailBox;
import com.taoke.miquaner.data.EMessage;
import com.taoke.miquaner.data.EUser;
import com.taoke.miquaner.repo.MailBoxRepo;
import com.taoke.miquaner.repo.MessageRepo;
import com.taoke.miquaner.repo.UserRepo;
import com.taoke.miquaner.serv.IMsgServ;
import com.taoke.miquaner.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

public class MsgServImpl implements IMsgServ {

    private MessageRepo messageRepo;
    private MailBoxRepo mailBoxRepo;
    private UserRepo userRepo;

    @Autowired
    public MsgServImpl(MessageRepo messageRepo, MailBoxRepo mailBoxRepo, UserRepo userRepo) {
        this.messageRepo = messageRepo;
        this.mailBoxRepo = mailBoxRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Object listMessages(EUser user, Integer pageNo) {
        return Result.success(this.mailBoxRepo.findByReceiverUserEquals(user,
                new PageRequest(pageNo - 1, 10, new Sort(Sort.Direction.DESC, "createTime")))
                .stream().peek(eMailBox -> {
                    eMailBox.setReceiverUser(mapUser(eMailBox.getReceiverUser()));
                    eMailBox.setSenderUser(mapUser(eMailBox.getSenderUser()));
                }).collect(Collectors.toList()));
    }

    private EUser mapUser(EUser user) {
        EUser ret = new EUser();
        ret.setName(user.getName());
        return ret;
    }

    @Override
    public Object getUncheckedMessageCount(EUser user) {
        return Result.success(this.mailBoxRepo.countAllByReceiverUserEqualsAndCheckedEquals(user, false));
    }

    @Override
    public Object send2All(String title, String content) {
        EMessage message = new EMessage();
        message.setTitle(title);
        message.setContent(content);
        EMessage savedMessage = this.messageRepo.save(message);

        List<EMailBox> toSave = this.userRepo.findAll().stream().map(user -> {
            EMailBox mailBox = new EMailBox();
            mailBox.setReceiverUser(user);
            mailBox.setAdminToReceive(false);
            mailBox.setSendFromAdmin(true);
            mailBox.setChecked(false);
            mailBox.setMessage(savedMessage);
            return mailBox;
        }).collect(Collectors.toList());
        this.mailBoxRepo.save(toSave);

        return Result.success(null);
    }

    @Override
    public Object sendFeedback(EUser user, String content) {
        EMessage message = new EMessage();
        message.setTitle(String.format("来自%s(真名=%s)的反馈", user.getName(), null != user.getRealName() ? user.getRealName() : "unknown"));
        message.setContent(content);
        EMessage savedMessage = this.messageRepo.save(message);

        EMailBox mailBox = new EMailBox();
        mailBox.setMessage(savedMessage);
        mailBox.setChecked(false);
        mailBox.setSendFromAdmin(false);
        mailBox.setSenderUser(user);
        mailBox.setAdminToReceive(true);
        this.mailBoxRepo.save(mailBox);

        return Result.success(null);

    }
}
