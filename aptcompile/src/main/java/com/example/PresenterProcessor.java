package com.example;

import com.example.annotation.apt.PresenterFactory;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class PresenterProcessor implements IProcessor{

    @Override
    public void process(RoundEnvironment roundEnv, AnnotationProcessor mAbstractProcessor) {
        String CLASS_NAME = "PresenterFactory";
        TypeSpec.Builder tb = classBuilder(CLASS_NAME).addModifiers(PUBLIC, FINAL).addJavadoc("@ 实例化工厂 此类由apt自动生成");

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("create")
                .addJavadoc("@此方法由apt自动生成")
                .addModifiers(PUBLIC, STATIC)
                .addParameter(Class.class, "mClass")
                .addException(IllegalAccessException.class)
                .addException(InstantiationException.class)
                .returns(Object.class);
        List<ClassName> mList = new ArrayList<>();
        CodeBlock.Builder blockBuilder = CodeBlock.builder();
        blockBuilder.beginControlFlow(" switch (mClass.getSimpleName())");//括号开始
        try {
            for (TypeElement element : ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(PresenterFactory.class))) {
                mAbstractProcessor.mMessager.printMessage(Diagnostic.Kind.NOTE, "正在处理: " + element.toString());
                if (!Utils.isValidClass(mAbstractProcessor.mMessager, element)) return;
                ClassName currentType = ClassName.get(element);
                if (mList.contains(currentType)) continue;
                mList.add(currentType);
                blockBuilder.addStatement("case $S: return  new $T()", currentType.simpleName(), currentType);
            }
            blockBuilder.addStatement("default: return mClass.newInstance()");
            blockBuilder.endControlFlow();
            methodBuilder.addCode(blockBuilder.build());
            tb.addMethod(methodBuilder.build());
            JavaFile javaFile = JavaFile.builder(Utils.PresenterPackageName, tb.build()).build();
            javaFile.writeTo(mAbstractProcessor.mFiler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
