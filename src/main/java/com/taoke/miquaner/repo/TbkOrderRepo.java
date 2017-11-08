package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.ETbkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TbkOrderRepo extends JpaRepository<ETbkOrder, Long> {

    ETbkOrder findByOrderIdEquals(Long orderId);

}
