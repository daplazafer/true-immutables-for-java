package com.dpf.ti4j.processor;

import com.dpf.ti4j.core.Immutable;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

public class ImmutableValidatorProcessor {

    public static void activateValidation(Class<?> clazz) {
        System.out.println("Instalando ByteBuddy agent...");
        ByteBuddyAgent.install();

        // Aplicar Advice a las clases que son anotadas con @Immutable
        new ByteBuddy()
                .redefine(clazz)  // Redefinir dinámicamente la clase Car
                .visit(Advice.to(ConstructorAdvice.class).on(ElementMatchers.isConstructor()))  // Interceptar el constructor sin llamar a super
                .make()
                .load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

        System.out.println("ByteBuddy agent instalado.");
    }

    // Clase para validar el constructor
    public static class ConstructorAdvice {
        @Advice.OnMethodExit
        public static void validate(@Advice.This Object instance) {
            System.out.println("Validando instancia de: " + instance.getClass().getName());
            if (instance.getClass().isAnnotationPresent(Immutable.class)) {
                com.dpf.ti4j.core.ImmutableValidator.validate(instance);
            }
        }
    }
}