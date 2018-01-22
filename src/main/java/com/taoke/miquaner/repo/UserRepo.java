package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<EUser, Long> {

    EUser findByPhoneEquals(String phone);

    EUser findByNameEquals(String name);

    EUser findByCodeEquals(String code);

    Page<EUser> findAllByExtNotContainsOrExtIsNull(String ext, Pageable pageable);

    List<EUser> findAllByExtNotContainsOrExtIsNull(String ext);

    Page<EUser> findAllByAnnouncementNotNullAndAliPidIsNull(Pageable pageable);

    List<EUser> findAllByAnnouncementNotNullAndAliPidIsNull();

    Page<EUser> findAllByExtContains(String ext, Pageable pageable);

    Page<EUser> findAllByPUser_idEquals(Long userId, Pageable pageable);

    List<EUser> findAllByExtContains(String ext);

    List<EUser> findAllByPUser_idEquals(Long userId);

    Page<EUser> findAllByNameContainsOrRealNameContainsOrAliPayIdContainsOrPhoneContains(String name, String realName, String aliPay, String phone, Pageable pageable);

    List<EUser> findAllByNameContainsOrRealNameContainsOrAliPayIdContainsOrPhoneContains(String name, String realName, String aliPay, String phone);

}
