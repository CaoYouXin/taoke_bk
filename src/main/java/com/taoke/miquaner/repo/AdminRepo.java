package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepo extends JpaRepository<EAdmin, Long> {

    EAdmin findByNameEquals(String name);

    List<EAdmin> findAllByGrantedAdminsIsNull();

}
