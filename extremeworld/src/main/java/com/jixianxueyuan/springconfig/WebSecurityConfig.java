package com.jixianxueyuan.springconfig;

import com.jixianxueyuan.service.account.MyAuthenticationProvider;
import com.jixianxueyuan.service.account.SecurityUserDetailService;
import com.jixianxueyuan.service.account.StaticParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by 23653 on 2016/12/20.
 */

@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityUserDetailService securityUserDetailService;

    @Autowired
    MyAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers(StaticParams.PATHREGX.CSS,StaticParams.PATHREGX.JS,StaticParams.PATHREGX.IMG, StaticParams.PATHREGX.API).permitAll()//无需访问权限

                .antMatchers(StaticParams.PATHREGX.ADMIN).hasAuthority(StaticParams.USERROLE.ROLE_ADMIN)//admin角色访问权限

                .antMatchers(StaticParams.PATHREGX.API_SECURE).hasAuthority(StaticParams.USERROLE.ROLE_USER)//user角色访问权限

                .anyRequest()//all others request authentication
                .authenticated()
/*                .and()
                .formLogin().loginPage("/login").permitAll()*/
                .and()
                .logout().permitAll();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
/*        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");*/
        //auth.userDetailsService(securityUserDetailService).passwordEncoder(passwordEncoder());
        auth.authenticationProvider(authenticationProvider);
        auth.eraseCredentials(false);//不删除凭据
    }
}
