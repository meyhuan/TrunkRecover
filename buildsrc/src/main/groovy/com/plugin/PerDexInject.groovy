package com.plugin

import com.plugin.help.JavassistInfo
import com.plugin.help.JavassistHelper
import javassist.ClassPool
import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.bytecode.DuplicateMemberException
import org.gradle.api.Project

import java.lang.annotation.Annotation;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class PerDexInject {

    private final static ClassPool pool = ClassPool.getDefault()

    public static void injectDir(String path, String packageName, Project project) {
        pool.appendClassPath(path)
        //project.android.bootClasspath 加入android.jar，否则找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString());
        JavassistUtils.importBaseClass(pool);
        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath
                //确保当前class文件不是系统自动生成的class文件
                if (filePath.endsWith(".class") && !filePath.contains('R$') && !filePath.contains('$')
                        && !filePath.contains('R.class') && !filePath.contains("BuildConfig.class")) {
                    int index = filePath.indexOf(packageName);
                    boolean isMyPackage = index != -1;
                    if (isMyPackage) {
                        String className = JavassistUtils.getClassName(index, filePath);
                        CtClass c = pool.getCtClass(className)

                        JavassistInfo mInfo = new JavassistInfo()
                        mInfo.setProject(project)
                        mInfo.setClazz(c)
                        for (CtMethod ct : c.getDeclaredMethods()) {
                            String methodName = JavassistUtils.getSimpleName(ct);
                            if (c.getName().endsWith("Presenter") && pool.get("com.meyhuan.trunkrecover.presenter.BaseMvpPresenter").getClass().isAssignableFrom(c.getClass())){
                                if(JavassistHelper.OnEventHandle.contains(methodName) && ct.getParameterTypes() != null && ct.getParameterTypes().size() == 1){
                                    mInfo.setEventHandleMethod(ct)
                                }
                                for (Annotation mAnnotation : ct.getAnnotations()) {
                                    if (mAnnotation.annotationType().canonicalName.equalsIgnoreCase(JavassistHelper.BusEventAnnotation)) {
                                        project.logger.error " method:" + c.getName() + " -" + ct.getName()
                                        mInfo.busEventMethods.add(ct)
                                    }
                                }
                            }
                        }
                        for (CtField ct : c.getDeclaredFields()){
                            for (Annotation mAnnotation : ct.getAnnotations()){
                                if(mAnnotation.annotationType().canonicalName.equalsIgnoreCase(JavassistHelper.ProviderModelAnnotation)) {
                                    project.logger.error " field:" + c.getName() + " -" + ct.getName()
                                    mInfo.modelFields.add(ct)
                                }
                            }
                        }
                        try {
                            JavassistHelper.process(mInfo, path)
                        } catch (DuplicateMemberException e) {
                            project.logger.error "javassist inject Model code fail"
                        }
                        //卸载更新pool里该类的代码
                        c.detach()
                    }
                }
            }
        }
    }
}
