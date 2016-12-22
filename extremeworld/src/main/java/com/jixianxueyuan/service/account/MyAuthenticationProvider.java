package com.jixianxueyuan.service.account;

import com.jixianxueyuan.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springside.modules.security.utils.Digests;
import org.springside.modules.utils.Encodes;

import java.util.Collection;

/**
 * Created by 23653 on 2016/12/21.
 */
@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

    public static final int HASH_INTERATIONS = 1024;


    @Autowired
    SecurityUserDetailService securityUserDetailService;

    @Autowired
    AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        SecurityUser securityUser = (SecurityUser) securityUserDetailService.loadUserByUsername(username);
        if(securityUser == null){
            throw new BadCredentialsException("Username not found.");
        }

        if (!checkPassword(securityUser, password)) {
            throw new BadCredentialsException("Wrong password.");
        }


        Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();

        return  new UsernamePasswordAuthenticationToken(securityUser, password, authorities);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    public boolean checkPassword(SecurityUser user,String plainPassword){
        byte[] salt = Encodes.decodeHex(user.getSalt());
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
        String password = Encodes.encodeHex(hashPassword);
        if(user.getPassword().equals(password)){
            return true;
        }
        return false;
    }

}
