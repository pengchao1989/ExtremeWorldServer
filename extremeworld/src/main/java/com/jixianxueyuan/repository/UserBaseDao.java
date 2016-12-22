/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.jixianxueyuan.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.jixianxueyuan.entity.UserBase;

public interface UserBaseDao extends PagingAndSortingRepository<UserBase, Long> {
	UserBase findByLoginName(String loginName);
	UserBase findByQqOpenId(String qqOpenId);
}
