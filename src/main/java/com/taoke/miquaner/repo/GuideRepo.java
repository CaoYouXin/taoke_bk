package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EGuide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideRepo extends JpaRepository<EGuide, Long> {

    List<EGuide> findAllByOrderByOrderDesc();

    List<EGuide> findAllByTypeEqualsOrderByOrderDesc(Integer type);

}
