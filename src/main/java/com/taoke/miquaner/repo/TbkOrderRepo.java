package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ETbkOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TbkOrderRepo extends JpaRepository<ETbkOrder, Long> {

    ETbkOrder findByOrderIdEquals(Long orderId);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdEqualsAndOrderStatusContains(Long siteId, Long adZoneId, String orderStatus, Pageable pageable);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdEqualsAndOrderStatusNotContains(Long siteId, Long adZoneId, String orderStatus, Pageable pageable);

    List<ETbkOrder> findBySiteIdEqualsAndAdZoneIdEquals(Long siteId, Long adZoneId, Pageable pageable);

}
