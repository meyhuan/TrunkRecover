package com.example;

import javax.annotation.processing.RoundEnvironment;

/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public interface IProcessor {
    void process(RoundEnvironment roundEnv, AnnotationProcessor mAbstractProcessor);
}
