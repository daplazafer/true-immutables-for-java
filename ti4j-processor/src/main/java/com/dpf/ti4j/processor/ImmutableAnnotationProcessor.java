package com.dpf.ti4j.processor;

import com.dpf.ti4j.core.Immutable;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ImmutableAnnotationProcessor {

    public static final Logger logger = LoggerFactory.getLogger(ImmutableAnnotationProcessor.class);

    public static void processImmutableAnnotations() {

        logger.debug("Processing Immutable annotations...");

        ByteBuddyAgent.install();

        final var classGraph = new ClassGraph()
                .enableAllInfo();

        try (ScanResult scanResult = classGraph.scan()) {
            scanResult.getClassesWithAnnotation(Immutable.class)
                    .forEach(ImmutableAnnotationProcessor::updateClass);
        }

        logger.debug("All Immutable annotations processed successfully");
    }

    private static void updateClass(ClassInfo immutableClassInfo) {
        final var immutableClass = immutableClassInfo.loadClass();

        try {

            logger.debug("Updating class '{}'", immutableClass.getName());

            new ByteBuddy()
                    .redefine(immutableClass)
                    .visit(Advice.to(ConstructorAdvice.class).on(ElementMatchers.isConstructor()))
                    .make()
                    .load(immutableClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

            logger.debug("Class '{}' updated successfully", immutableClass.getName());

        } catch (Exception e) {
            logger.error("Error updating class '{}'", immutableClass.getName());
        }
    }
}