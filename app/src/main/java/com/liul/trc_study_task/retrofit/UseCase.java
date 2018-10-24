package com.liul.trc_study_task.retrofit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个注册，申明使用位置以及在什么级别保存注解信息
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface UseCase {
    public String id();
    public String description();
}
