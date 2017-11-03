package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivilegeRepo extends JpaRepository<EPrivilege, Long> {

    EPrivilege findByApiEquals(String api);
}
