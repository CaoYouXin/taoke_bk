package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EMailBox;
import com.taoke.miquaner.data.EUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailBoxRepo extends JpaRepository<EMailBox, Long> {

    List<EMailBox> findByReceiverUserEquals(EUser receiver, Pageable pageable);

    Long countAllByReceiverUserEqualsAndCheckedEquals(EUser receiver, Boolean checked);

    List<EMailBox> findAllByIsAdminToReceiveEquals(Boolean isAdmin2Receive);

}
