package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EShareImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShareImgRepo extends JpaRepository<EShareImg, Long> {

    List<EShareImg> findAllByOrderByOrderDesc();

}
