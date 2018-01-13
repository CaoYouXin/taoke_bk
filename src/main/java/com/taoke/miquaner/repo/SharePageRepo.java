package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ESharePage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SharePageRepo extends JpaRepository<ESharePage, Long> {

    List<ESharePage> findTop1ByCreateTimeLessThanOrderByCreateTimeAsc(Date expired);

    ESharePage findOneByKeyEquals(String key);

}
