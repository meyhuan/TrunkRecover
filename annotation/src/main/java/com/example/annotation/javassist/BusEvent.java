package com.example.annotation.javassist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface BusEvent {
}
