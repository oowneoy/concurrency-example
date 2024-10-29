package com.example.stock.facade;

import com.example.stock.repository.LockRepository;
import com.example.stock.service.StockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class NamedLockStockFacade {
    private static final Logger log = LoggerFactory.getLogger(NamedLockStockFacade.class);
    private final LockRepository lockRepository;
    private final StockService stockService;


    public NamedLockStockFacade(LockRepository lockRepository, StockService stockService) {
        this.lockRepository = lockRepository;
        this.stockService = stockService;
    }

    @Transactional
    public void decrease(Long id, Long quantity) {
        try {
            lockRepository.getLock(id.toString());
            log.info("Named Lock 획득");
            stockService.decrease(id, quantity);
        } finally {
            lockRepository.releaseLock(id.toString());
            log.info("Named Lock 해제");
        }
    }
}
