package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ECate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CateRepo extends JpaRepository<ECate, Long> {

    List<ECate> findAllByOrderByOrderDesc();

}
