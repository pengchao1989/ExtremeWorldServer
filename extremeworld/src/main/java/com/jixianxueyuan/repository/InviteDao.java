package com.jixianxueyuan.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.jixianxueyuan.entity.Invite;

public interface InviteDao extends PagingAndSortingRepository<Invite,String>{

	Invite findByPhone(String phone);
}
