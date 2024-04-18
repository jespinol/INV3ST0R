package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.StockPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<StockPosition, Long> {
    ArrayList<StockPosition> findAllByAccountId(@Param("accountId") Long accountId);

    Optional<StockPosition> findBySymbolAndAccountId(@Param("symbol") String symbol, @Param("accountId") Long accountId);
}
