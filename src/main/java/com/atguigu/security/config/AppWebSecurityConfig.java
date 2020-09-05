package com.atguigu.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration //声明一个配置类。配置类就相当与XML配置文件的作用。
@EnableWebSecurity //启用权限框架功能
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启细粒度的权限控制功能。
public class AppWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired   //先按照bytype，再按照byname
    //@Resource  //先按照byname，再按照bytype
    DataSource dataSource;

    @Autowired
    UserDetailsService userDetailsService;

    //@Autowired
    //PasswordEncoder passwordEncoder;

    //对请求进行[认证]处理
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //默认 认证处理 ，任何登录信息都无法认证。
        //super.configure(auth);

        //4.实验四：自定义认证用户信息(基于内存的认证方式 - 了解)
//        auth.inMemoryAuthentication()
////                .withUser("zhangsan").password("123456").roles("学徒","大师")
////                .and()
////                .withUser("lisi").password("123123").authorities("罗汉拳","武当长拳");

        //基于数据库的认证
        //auth.userDetailsService(userDetailsService);
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }



    //对请求进行[授权]处理
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //默认 授权  对所有资源访问进行拦截，所有资源都不允许访问
        //super.configure(http);

        // ant 路径规则：
        // * 匹配任意字符
        // ** 匹配任意路径及路径下的资源
        // ? 匹配一个字符
        //1.授权首页和静态资源（这些资源不用登录就可以访问）
        /*http.authorizeRequests()
                .antMatchers("/index.jsp","/layui/**").permitAll()
                .anyRequest().authenticated();*/



        //6.实验六：基于角色的访问控制
        http.authorizeRequests()
                .antMatchers("/index.jsp","/layui/**").permitAll()
                //.antMatchers("/level1/**").hasRole("学徒")
                //.antMatchers("/level1/1").hasAuthority("罗汉拳")
                //.antMatchers("/level1/2").hasAuthority("武当长拳")
                //.antMatchers("/level2/**").hasRole("大师")
                //.antMatchers("/level3/**").hasRole("宗师")
                .anyRequest().authenticated();



        //2.授权默认登录页（当发生403无权访问时，去到默认登录页）或自定义登录页
        //http.formLogin(); //默认登录页要求：请求action必须是"/login" ,表单参数必须是username,password,必须post请求
        //http.formLogin().loginPage("/index.jsp");


        //3.实验三：自定义表单登录逻辑分析
        http.formLogin().loginPage("/index.jsp")
                        .loginProcessingUrl("/doLogin")
                        .usernameParameter("loginacct")
                        .passwordParameter("userpswd")
                        .defaultSuccessUrl("/main.html");


        //5.实验五：用户注销完成
        http.logout().logoutUrl("/logout")
                .logoutSuccessUrl("/index.jsp");


        //7.实验七：自定义访问拒绝处理页面
        //http.exceptionHandling().accessDeniedPage("/unauth.html");
        http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                httpServletRequest.getRequestDispatcher("/WEB-INF/views/unauth.jsp").forward(httpServletRequest,httpServletResponse);
            }
        });


        //8.实验八：记住我功能-Cookie版
        //http.rememberMe();
        //9.实验九 记住我功能 - 数据库版
        JdbcTokenRepositoryImpl ptr = new JdbcTokenRepositoryImpl();
        ptr.setDataSource(dataSource);
        http.rememberMe().tokenRepository(ptr);


        //暂时禁用csrf (防止跨站请求伪造)
        http.csrf().disable();
    }
}
