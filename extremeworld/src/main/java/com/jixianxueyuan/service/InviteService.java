package com.jixianxueyuan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.Invite;
import com.jixianxueyuan.repository.InviteDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class InviteService {

	@Autowired
	private InviteDao inviteDao;
	
	public void saveInvite(Invite invite){
		inviteDao.save(invite);
	}
	
	public Invite getInvite(String id){
		return inviteDao.findOne(id);
	}
	
	public Invite findByPhone(String phone){
		return inviteDao.findByPhone(phone);
	}
}
