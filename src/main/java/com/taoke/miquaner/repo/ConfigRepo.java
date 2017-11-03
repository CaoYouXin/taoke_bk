package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepo extends JpaRepository<EConfig, Long> {

    EConfig findByKeyEquals(String key);

    EConfig findByKeyEqualsAndAndValueEquals(String key, String value);

}
