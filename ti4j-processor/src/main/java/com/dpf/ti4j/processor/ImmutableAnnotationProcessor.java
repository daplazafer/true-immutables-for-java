package com.dpf.ti4j.processor;

import com.dpf.ti4j.core.Immutable;
import com.google.auto.service.AutoService;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.LoaderClassPath;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class)
public class ImmutableAnnotationProcessor extends AbstractProcessor {

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of("com.dpf.ti4j.core.Immutable");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Immutable.class)) {
            try {
                String className = element.toString();
                ClassPool classPool = ClassPool.getDefault();
                classPool.appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));

                CtClass ctClass = classPool.get(className);
                for (CtConstructor constructor : ctClass.getDeclaredConstructors()) {
                    constructor.insertAfter("com.dpf.ti4j.core.ImmutableValidator.validate(this);");
                }

                ctClass.writeFile();

            } catch (Exception e) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Error processing @Immutable for class " + element.getSimpleName() + ": " + e.getMessage());
            }
        }
        return true;
    }
}