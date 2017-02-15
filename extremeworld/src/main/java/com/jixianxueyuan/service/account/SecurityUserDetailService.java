package com.jixianxueyuan.service.account;

import com.jixianxueyuan.entity.UserBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by 23653 on 2016/12/21.
 */
@Service
public class SecurityUserDetailService implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        UserBase user = accountService.findUserByLoginName(userName);
        if (user == null){
            throw new UsernameNotFoundException("UserName=" + userName + "not found");
        }
        return new SecurityUser(user);
    }
}
