package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ETbkOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TbkOrderRepo extends JpaRepository<ETbkOrder, Long> {

    ETbkOrder findByOrderIdEquals(Long orderId);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdInAndOrderStatusContains(Long siteId, List<Long> adZoneId, String orderStatus, Pageable pageable);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdInAndOrderStatusNotContains(Long siteId, List<Long> adZoneId, String orderStatus, Pageable pageable);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdIn(Long siteId, List<Long> adZoneId, Pageable pageable);

    List<ETbkOrder> findAllBySiteIdEqualsAndAdZoneIdEqualsAndOrderStatusContainsAndCreateTimeBefore(Long siteId, Long adZoneId, String orderStatus, Date lastDate);

    List<ETbkOrder> findAllBySiteIdEqualsAndAdZoneIdInAndOrderStatusContainsAndCreateTimeBetween(Long siteId, List<Long> adZoneId, String orderStatus, Date start, Date end);

}

