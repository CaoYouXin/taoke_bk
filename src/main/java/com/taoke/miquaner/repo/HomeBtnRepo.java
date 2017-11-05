package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EHomeBtn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeBtnRepo extends JpaRepository<EHomeBtn, Long> {

    List<EHomeBtn> findAllByLocationTypeEqualsOrderByOrderAsc(Integer locationType);

    EHomeBtn findByIdEqualsAndLocationTypeEquals(Long id, Integer locationType);

}
