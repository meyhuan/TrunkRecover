package com.plugin.help

import javassist.CtClass
import javassist.CtField
import javassist.CtMethod
import javassist.bytecode.CodeAttribute
import javassist.bytecode.LocalVariableAttribute
import javassist.bytecode.MethodInfo

import java.lang.reflect.Modifier

public class JavassistHelper {

    final static String BusEventAnnotation = "com.example.annotation.javassist.BusEvent"
    final static String ProviderModelAnnotation = "com.example.annotation.javassist.ProviderModel"

    static def OnEventHandle = "onEventHandle"

    static def Pre_OnEventHandle = "\n" +
            " public void onEventHandle(com.meyhuan.trunkrecover.rxbus.RxInterface.RxEvent event){\n" +
            "     super.onEventHandle(event);\n"

    /**
     * 处理@ProviderModel 注解的field，移除原有field，替换为了带有赋值的新field
     * 例如 private LoginModel mLoginModel;
     * 替换为  private LoginModel mLoginModel = InjectHelper.getModel(LoginModel.class);
     *
     * 处理@BusEvent
     * @param mBusInfo
     * @param path
     */
    static void process(JavassistInfo info, String path) {
        boolean needInject = false;
        if(info.clazz.isFrozen())info.clazz.defrost()
        info.project.logger.error "########## className: -" + info.clazz.simpleName +" SuperclassName:  - " + info.clazz.getSuperclass().getName()

        //处理BusEvent注解
        if(info.busEventMethods != null && info.busEventMethods.size() > 0){
            if(info.eventHandleMethod !=  null){
                info.project.logger.error "onEventHandle not null"
                info.eventHandleMethod.insertAfter(getEventHandleMethodBodyStr(info))
//                info.clazz.removeMethod(info.eventHandleMethod)
//                CtMethod method = CtMethod.make(getEventHandleMethodStr(info), info.clazz)
//                info.clazz.addMethod(method)
            }else{
                info.project.logger.error "onEventHandle is null, need new onEventHandle method"
                CtMethod method = CtMethod.make(getEventHandleMethodStr(info), info.clazz)
                info.clazz.addMethod(method)
            }
            needInject = true;
        }

        if(info.modelFields != null && info.modelFields.size() > 0){
            processProviderModel(info)
            needInject = true;
        }

        if(needInject)info.clazz.writeFile(path)
    }

    /**
     * 处理@BusEvent
     * @param info
     */
    static void processProviderModel(JavassistInfo info){
        for(CtField c : info.modelFields){
            int modifiers = c.getModifiers();
            CtClass type = c.getType();

            StringBuffer body = new StringBuffer()
            body.append("public ")
            body.append(type.name)
            body.append(" ")
            body.append(c.getName())
            body.append(" = (")
            body.append(type.name)
            body.append(")InjectHelper.getModel(")
            body.append(type.name)
            body.append(".class);")

            info.project.logger.error "processProviderModel body: \n" + body.toString()

            CtField field = CtField.make(body.toString(), info.clazz)
            field.setModifiers(modifiers)

            //移除旧没有赋值的field 替换完为新赋值的field
            info.clazz.removeField(c)
            info.clazz.addField(field)
        }
    }

    /**
     * 创建全新onEventHandler方法
     * @param info
     * @return
     */
    static String getEventHandleMethodStr(JavassistInfo info){
        StringBuffer createStr = new StringBuffer()
        createStr.append(Pre_OnEventHandle)
        for(CtMethod m : info.busEventMethods){
            CtClass[] parameterTypes = m.getParameterTypes()
            if(parameterTypes.length > 0){
                CtClass c = parameterTypes[0];
                createStr.append("if(event instanceof ")
                createStr.append(c.name)
                createStr.append("){\n")
                createStr.append(m.name)
                createStr.append("((")
                createStr.append(c.name)
                createStr.append(")event);}\n")
            }
        }
        createStr.append("}")
        info.project.logger.error "onEventHandle method : \n" + createStr.toString()
        return createStr.toString()
    }

    /**
     * 获取onEventHandler方法的body，例如(if event instanceof x){a(x)} a为你注解的方法名称
     * @param info
     * @return
     */
    static String getEventHandleMethodBodyStr(JavassistInfo info){
        MethodInfo methodInfo = info.eventHandleMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute()
        LocalVariableAttribute attribute = (LocalVariableAttribute)codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if(attribute == null)return ""
        int pos = Modifier.isStatic(info.eventHandleMethod.getModifiers()) ? 0 : 1;

        for (int i=0; i < info.eventHandleMethod.getParameterTypes().length;i++) {
            info.project.logger.error "LocalVariable : " +  attribute.variableName(i+pos)
        }
        String variableName = ""
        if(info.eventHandleMethod.getParameterTypes().length == 1) {
            variableName = attribute.variableName(pos)
        }
        StringBuffer createStr = new StringBuffer()
        for(CtMethod m : info.busEventMethods){
            CtClass[] parameterTypes = m.getParameterTypes()
            if(parameterTypes.length > 0){
                CtClass c = parameterTypes[0];
                createStr.append("if(")
                createStr.append(variableName)
                createStr.append(" instanceof ")
                createStr.append(c.name)
                createStr.append("){\n")
                createStr.append(m.name)
                createStr.append("((")
                createStr.append(c.name)
                createStr.append(")")
                createStr.append(variableName)
                createStr.append(");}\n")
            }
        }
        info.project.logger.error "onEventHandle body : \n" +  createStr.toString()
        return createStr.toString()
    }
}
