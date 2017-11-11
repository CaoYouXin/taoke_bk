package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EGuide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepo extends JpaRepository<EGuide, Long> {
}
