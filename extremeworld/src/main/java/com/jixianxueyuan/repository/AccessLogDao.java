package com.jixianxueyuan.repository;

import com.jixianxueyuan.entity.AccessLog;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface AccessLogDao extends PagingAndSortingRepository<AccessLog, Long>{

}
