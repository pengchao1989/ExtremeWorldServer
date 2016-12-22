package com.jixianxueyuan.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.jixianxueyuan.entity.AppKey;
import com.jixianxueyuan.repository.ClientConfigDao;

//Spring Bean的标识.
@Component
//类中所有public函数都纳入事务管理的标识.
@Transactional
public class ClientConfigManage {
	
	
	private ClientConfigDao clientConfigDao;
	
	List<AppKey> clientConfigList = null;
	
	
	public AppKey getCilentConfig(String hobbyName){
		
		clientConfigList = (List<AppKey>) clientConfigDao.findAll();
		
		for(AppKey clientConfig: clientConfigList){
			if(clientConfig.getHobby().geteName().equals(hobbyName)){
				return clientConfig;
			}
		}
		return null;
	}

	public List<AppKey> getClientConfigList() {
		return clientConfigList;
	}

	@Autowired
	public void setClientConfigDao(ClientConfigDao clientConfigDao) {
		this.clientConfigDao = clientConfigDao;
	}

}
