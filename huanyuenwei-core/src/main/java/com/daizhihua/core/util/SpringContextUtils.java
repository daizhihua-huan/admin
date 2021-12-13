package com.daizhihua.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * spring上下文环境获取工具类
 * 1获取容器加载完成后自己注入的类
 *
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;

    }

    /**
     * 获取ServletRequestAttributes
     * @return ServletRequestAttributes
     * @author zmzhou
     * @date 2020/9/6 12:07
     */
    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }
    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }
    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static Object getBean(String beanName){
        Object bean = applicationContext.getBean(beanName);
        if(bean==null){
            throw new  RuntimeException("容器中没有叫"+beanName+"的类");
        }
        return bean;
    }

    public static <T> T getBean(Class clzz){

        T bean = (T) applicationContext.getBean(clzz);
        if(bean==null){
            throw new  RuntimeException("容器中没有叫"+clzz.getName()+"的类");
        }
        return bean;
    }


}
