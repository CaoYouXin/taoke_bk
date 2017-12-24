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

}
