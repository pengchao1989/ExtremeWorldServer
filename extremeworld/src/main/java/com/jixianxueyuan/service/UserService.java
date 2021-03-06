package com.jixianxueyuan.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.User;
import com.jixianxueyuan.repository.UserDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class UserService {

	@Autowired
	private UserDao userDao;
	
	public User getUser(Long id)
	{
		return userDao.findOne(id);
	}
	
	public User findUserByName(String name){
		return userDao.findByName(name);
	}
	
	public User findByLoginName(String loginName){
		return userDao.findByLoginName(loginName);
	}
	
	public List<User> getAll()
	{
		return (List<User>) userDao.findAll();
	}
	
	public Page<User> findByGeoHash(
			String center,
			String north,
			String east,
			String south,
			String west,
			String northwest,
			String northeast,
			String southwest,
			String southeast,  int pageNumber, int pageSize,String sortType)
	{
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		
		return userDao.findByGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLikeOrGeoHashLike
				(center,north,east,south, west, northwest, northeast, southwest, southeast, pageRequest);
	}
	
	public Page<User> findByInviterId(Long inviterId, int pageNumber, int pageSize){
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, "id");
		return userDao.findByInviterId(inviterId, pageRequest);
	}
	
	public User saveUser(User user)
	{
		return userDao.save(user);
	}
	
	public List<User> getFollowings(Long id)
	{
		return userDao.findOne(id).getFollowings();
	}
	
	public List<User> getFollowers(Long id)
	{
		return userDao.findOne(id).getFollowers();
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.ASC, "geoHash");
		}else if("id".equals(sortType)){
			sort = new Sort(Direction.DESC, "id");
		}else{
			sort = new Sort(Direction.ASC, "geoHash");
		}
		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}
	
}
