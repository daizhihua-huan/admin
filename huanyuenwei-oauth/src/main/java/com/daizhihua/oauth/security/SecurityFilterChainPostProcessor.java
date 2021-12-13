package com.daizhihua.oauth.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Repository;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SecurityFilterChainPostProcessor implements BeanPostProcessor {

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        System.out.println("找到了");

        /*if(beanName.equals("springSecurityFilterChain")){
            FilterChainProxy filterChainProxy = (FilterChainProxy) bean;
            System.out.println(beanName);
            List<SecurityFilterChain> filterChains = filterChainProxy.getFilterChains();
            SecurityFilterChain securityFilterChain = filterChains.get(0);
            List<Filter> filters = securityFilterChain.getFilters();

            FilterChainProxy filterChainProxy1 = new FilterChainProxy();
            return filterChainProxy1;


        }*/

//        if (beanName.equals("myFilterChain")) {
//            DefaultSecurityFilterChain fc = (DefaultSecurityFilterChain)bean;
//            List<Filter> filters = fc.getFilters();
//
//            // Modify the filter list as you choose here.
//            List<Filter> newFilters = ...
//
//            return new DefaultSecurityFilterChain(fc.getRequestMatcher(), newFilters);
//        }

        return bean;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
