package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.Optional;

public interface StockRepository extends CrudRepository<Stock, Long> {
    ArrayList<Stock> findAllByAccountId(@Param("accountId") Long accountId);

    Optional<Stock> findBySymbolAndAccountId(@Param("symbol") String symbol, @Param("accountId") Long accountId);
}
