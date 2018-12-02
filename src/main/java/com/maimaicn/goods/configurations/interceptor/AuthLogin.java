package com.maimaicn.goods.configurations.interceptor;

import java.lang.annotation.*;


/**
 * 登陆校验注解
 *
 * @param
 * @return
 */
@Documented
@Inherited
@Target(ElementType.METHOD)//
@Retention(RetentionPolicy.RUNTIME)//何时生效
public @interface AuthLogin {
}



