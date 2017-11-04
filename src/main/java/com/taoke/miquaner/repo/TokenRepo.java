package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface TokenRepo extends JpaRepository<EToken, Long> {

    EToken findByTokenEqualsAndExpiredAfter(String token, Date now);

}
