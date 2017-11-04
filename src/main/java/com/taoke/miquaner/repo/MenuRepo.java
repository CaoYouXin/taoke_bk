package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepo extends JpaRepository<EMenu, Long> {
}
