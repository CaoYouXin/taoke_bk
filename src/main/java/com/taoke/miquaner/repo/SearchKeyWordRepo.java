package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ESearchKeyWord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchKeyWordRepo extends JpaRepository<ESearchKeyWord, Long> {

    ESearchKeyWord findByKeywordEquals(String keyword);

    List<ESearchKeyWord> findAllByKeywordContains(String keyword);

}
