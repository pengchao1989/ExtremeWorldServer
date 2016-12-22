package com.jixianxueyuan.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.VerificationCode;

public interface VerificationCodeDao extends PagingAndSortingRepository<VerificationCode,Long>{

	public VerificationCode findByPhoneAndCode(String phone, String code);
}
