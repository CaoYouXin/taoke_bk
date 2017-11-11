package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ESmsCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepo extends JpaRepository<ESmsCode, Long> {

    ESmsCode findByPhoneEquals(String phone);

}
