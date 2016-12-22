package com.jixianxueyuan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.ImageHistory;
import com.jixianxueyuan.repository.ImageHistoryDao;

@Component
@Transactional
public class ImageHistoryService {
	@Autowired
	private ImageHistoryDao imageHistoryDao;
	
	public void save(ImageHistory imageHistory){
		imageHistoryDao.save(imageHistory);
	}
}
