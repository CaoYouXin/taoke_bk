package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EAdZoneItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdZoneItemRepo extends JpaRepository<EAdZoneItem, Long> {

    List<EAdZoneItem> findAllByOrderByOrderDesc();

    List<EAdZoneItem> findAllByOrderByIosOrderDesc();

}
