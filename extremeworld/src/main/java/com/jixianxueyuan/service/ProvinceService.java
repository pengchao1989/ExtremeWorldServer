package com.jixianxueyuan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.Province;
import com.jixianxueyuan.repository.ProvinceDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ProvinceService
{
	@Autowired
	private ProvinceDao provinceDao;
	
	public List<Province> getAll()
	{
		return (List<Province>) provinceDao.findAll();
	}

}
