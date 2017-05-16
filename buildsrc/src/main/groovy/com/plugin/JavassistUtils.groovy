package com.plugin

import com.plugin.help.JavassistHelper
import javassist.ClassPool
import javassist.CtMethod

public class JavassistUtils {

    /**
     * 事先载入相关类
     * @param pool
     */
    static void importBaseClass(ClassPool pool) {
        pool.importPackage(JavassistHelper.BusEventAnnotation)
        pool.importPackage(JavassistHelper.ProviderModelAnnotation)
        pool.importPackage("com.meyhuan.trunkrecover.rxbus.RxInterface")
        pool.importPackage("com.meyhuan.trunkrecover.rxbus.RxInterface.*")
        pool.importPackage("com.meyhuan.trunkrecover.model.InjectHelper")
        pool.importPackage("com.meyhuan.trunkrecover.Event.*")
    }

    static String getSimpleName(CtMethod ctmethod) {
        def methodName = ctmethod.getName();
        return methodName.substring(
                methodName.lastIndexOf('.') + 1, methodName.length());
    }

    static String getClassName(int index, String filePath) {
        int end = filePath.length() - 6 // .class = 6
        return filePath.substring(index, end).replace('\\', '.').replace('/', '.')
    }
}