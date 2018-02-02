package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EHelpDoc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HelpDocRepo extends JpaRepository<EHelpDoc, Long> {

    List<EHelpDoc> findAllByOrderByOrderDesc();

}
