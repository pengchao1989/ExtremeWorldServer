package com.jixianxueyuan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.Trade;
import com.jixianxueyuan.repository.TradeDao;

@Component
@Transactional
public class TradeService {

	@Autowired
	private TradeDao tradeDao;
	
	public void save(Trade trade){
		tradeDao.save(trade);
	}
	
	public Trade findByInternalTradeNo(String internalTradeNo){
		return tradeDao.findByInternalTradeNo(internalTradeNo);
	}
}
