package com.plugin.help

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import org.gradle.api.Project

public class JavassistInfo {

    /**
     * 当前工程应用
     */
    Project project

    /**
     * 当前正在处理的CtClass
     */
    CtClass clazz

    /**
     * 带有ProviderModel注解的field
     */
    List<CtField> modelFields = new ArrayList<>()

    /**
     * 带有BusEvent注解的方法
     */
    List<CtMethod> busEventMethods = new ArrayList<>()

    /**
     * onEventHandle方法，用来分发调用带有BusEvent注解的方法
     */
    CtMethod eventHandleMethod;
}