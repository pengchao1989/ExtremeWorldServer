package com.jixianxueyuan.service.account;

import com.jixianxueyuan.entity.UserBase;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by 23653 on 2016/12/21.
 */
public class SecurityUser extends UserBase implements UserDetails {

    private static final long serialVersionUID = 1L;

    public SecurityUser(UserBase userBase){
        if (userBase != null){
            this.setId(userBase.getId());
            this.setLoginName(userBase.getLoginName());
            this.setPassword(userBase.getPassword());
            this.setName(userBase.getName());
            this.setRoles(userBase.getRoles());
            this.setSalt(userBase.getSalt());
            this.setToken(userBase.getToken());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority authority  = new SimpleGrantedAuthority(getRoles());
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getUsername() {
        return super.getLoginName();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getSalt() {
        return super.getSalt();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
