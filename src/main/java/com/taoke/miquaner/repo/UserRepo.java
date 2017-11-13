package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EUser;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<EUser, Long> {

    EUser findByPhoneEquals(String phone);

    EUser findByNameEquals(String name);

}
