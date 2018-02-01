package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<EFeedback, Long> {
}
