package com.taoke.miquaner.repo;

import com.taoke.miquaner.data.EFavoriteOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteOrderRepo extends JpaRepository<EFavoriteOrder, Long> {

    List<EFavoriteOrder> findAllByFavoriteIdOrderByOrderDesc(Long favoriteId);

    EFavoriteOrder findByFavoriteIdEqualsAndNumIidEquals(Long favoriteId, Long numIid);

}
