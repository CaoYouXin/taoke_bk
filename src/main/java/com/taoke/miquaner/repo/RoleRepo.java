package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<ERole, Long> {

    ERole findByNameEquals(String name);

}
