package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepo extends JpaRepository<EMessage, Long> {
}
