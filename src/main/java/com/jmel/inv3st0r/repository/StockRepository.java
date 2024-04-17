package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.StockPosition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface StockRepository extends CrudRepository<StockPosition, Long> {
    ArrayList<StockPosition> findAllByAccountId(@Param("accountId") Long accountId);

    Optional<StockPosition> findBySymbolAndAccountId(@Param("symbol") String symbol, @Param("accountId") Long accountId);
}
