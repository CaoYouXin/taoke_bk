package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ETbkItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TbkItemRepo extends JpaRepository<ETbkItem, Long> {
}
