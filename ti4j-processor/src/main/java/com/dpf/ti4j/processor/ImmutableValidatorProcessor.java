package com.dpf.ti4j.processor;

import com.dpf.ti4j.core.Immutable;
import com.dpf.ti4j.core.ImmutableValidator;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.This;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import org.reflections.Reflections;

import java.util.Set;

public class ImmutableValidatorProcessor {

    public static void activateImmutableValidation() {

        System.out.println("Installing ByteBuddy agent...");

        ByteBuddyAgent.install();

        Reflections reflections = new Reflections("");
        Set<Class<?>> immutableClasses = reflections.getTypesAnnotatedWith(Immutable.class);

        for (Class<?> immutableClass : immutableClasses) {
            try {
                System.out.println("Transforming class: " + immutableClass.getName());

                new ByteBuddy()
                        .redefine(immutableClass)
                        .visit(Advice.to(ConstructorAdvice.class).on(ElementMatchers.isConstructor()))
                        .make()
                        .load(immutableClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

            } catch (Exception e) {
                System.out.println("Error transforming class: " + immutableClass.getName());
            }
        }

        System.out.println("ByteBuddy agent installed successfully");
    }

    public static class ConstructorAdvice {

        @OnMethodExit
        public static void validate(@This Object instance) {

            System.out.println("Validating: " + instance.getClass().getName());
            if (instance.getClass().isAnnotationPresent(Immutable.class)) {
                ImmutableValidator.validate(instance);
            }
        }

    }
}