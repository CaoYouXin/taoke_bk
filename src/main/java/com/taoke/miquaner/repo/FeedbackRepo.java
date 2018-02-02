package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EFeedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepo extends JpaRepository<EFeedback, Long> {

    Page<EFeedback> findAllByCheckedEquals(Boolean checked, Pageable pageable);

}
