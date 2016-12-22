package com.jixianxueyuan.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.Trade;


public interface TradeDao extends PagingAndSortingRepository<Trade, Long>{

	public Trade findByInternalTradeNo(String internalTradeNo);
}
