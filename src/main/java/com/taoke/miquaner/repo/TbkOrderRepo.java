package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ETbkOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface TbkOrderRepo extends JpaRepository<ETbkOrder, Long> {

    ETbkOrder findByOrderIdEqualsAndItemNumIidEquals(Long orderId, Long itemId);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdInAndOrderStatusContains(Long siteId, List<Long> adZoneId, String orderStatus, Pageable pageable);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdInAndOrderStatusNotContains(Long siteId, List<Long> adZoneId, String orderStatus, Pageable pageable);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdIn(Long siteId, List<Long> adZoneId, Pageable pageable);

    List<ETbkOrder> findAllBySiteIdEqualsAndAdZoneIdEqualsAndOrderStatusContainsAndSettleTimeBefore(Long siteId, Long adZoneId, String orderStatus, Date lastDate);

    List<ETbkOrder> findAllBySiteIdEqualsAndAdZoneIdInAndOrderStatusContainsAndSettleTimeBetween(Long siteId, List<Long> adZoneId, String orderStatus, Date start, Date end);

}

