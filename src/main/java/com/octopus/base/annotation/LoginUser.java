package com.octopus.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // @LoginUser 어노테이션이 생성될 수 있는 위치를 지정한다.
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 보존한다.
public @interface LoginUser { // 어노테이션 클래스로 지정한다.
}