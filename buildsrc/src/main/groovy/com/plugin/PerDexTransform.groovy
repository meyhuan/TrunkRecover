package com.plugin

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.google.common.collect.Sets
import javassist.ClassPool
import org.apache.commons.io.FileUtils
import org.gradle.api.Project;
/**
 * User  : guanhuan
 * Date  : 2017/5/16
 */

public class PerDexTransform extends Transform{

    Project project

    public PerDexTransform(Project project) {
        this.project = project
    }


    @Override
    public String getName() {
        return "PerDexTransform";
    }

    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return Sets.immutableEnumSet(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(QualifiedContent.Scope.PROJECT, QualifiedContent.Scope.PROJECT_LOCAL_DEPS,
                QualifiedContent.Scope.SUB_PROJECTS, QualifiedContent.Scope.SUB_PROJECTS_LOCAL_DEPS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES)
    }

    @Override
    public boolean isIncremental() {
        return false;
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
        def startTime = System.currentTimeMillis();
        inputs.each { TransformInput input ->
            try {
                //对jar包文件遍历
                input.jarInputs.each {
                    PerDexInject.injectDir(it.file.getAbsolutePath(), "com", project)
                    String outputFileName = it.name.replace(".jar", "") + '-' + it.file.path.hashCode()
                    def output = outputProvider.getContentLocation(outputFileName, it.contentTypes, it.scopes, Format.JAR)
                    FileUtils.copyFile(it.file, output)
                }
            } catch (Exception e) {
                project.logger.err e.getMessage()
            }
            //对普通文件夹进行遍历，里面包含的是手动生成的类以及R.class、BuildConfig.class以及R$XXX.class等
            input.directoryInputs.each { DirectoryInput directoryInput ->
                PerDexInject.injectDir(directoryInput.file.absolutePath, "com", project)
                def dest = outputProvider.getContentLocation(directoryInput.name,
                        directoryInput.contentTypes, directoryInput.scopes,
                        Format.DIRECTORY)
                FileUtils.copyDirectory(directoryInput.file, dest)
            }
        }
        ClassPool.getDefault().clearImportedPackages();
        project.logger.error("JavassistTransform cast :" + (System.currentTimeMillis() - startTime) / 1000 + " secs");
    }
}
