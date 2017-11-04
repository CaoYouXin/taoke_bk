package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EBanner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BannerRepo extends JpaRepository<EBanner, Long> {
}
