package com.dpf.ti4j.processor;

import com.dpf.ti4j.core.Immutable;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@AutoService(Processor.class)
public class ImmutableAnnotationProcessor extends AbstractProcessor {

    private JavacTrees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of("com.dpf.ti4j.core.Immutable");
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Immutable.class)) {
            JCTree tree = trees.getTree(element);
            if (tree instanceof JCTree.JCClassDecl) {
                JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) tree;
                addValidationToConstructors(classDecl);
            }
        }
        return true;
    }

    private void addValidationToConstructors(JCTree.JCClassDecl classDecl) {
        for (JCTree tree : classDecl.defs) {
            if (tree instanceof JCTree.JCMethodDecl) {
                JCTree.JCMethodDecl methodDecl = (JCTree.JCMethodDecl) tree;
                if (methodDecl.name.toString().equals("<init>")) {
                    addValidationToConstructor(methodDecl, classDecl.sym);
                }
            }
        }
    }

    private void addValidationToConstructor(JCTree.JCMethodDecl constructor, Symbol.ClassSymbol classSymbol) {
        JCTree.JCExpressionStatement validationCall = treeMaker.Exec(
                treeMaker.Apply(
                        List.nil(),
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("com.dpf.ti4j.core.ImmutableValidator")),
                                names.fromString("validate")
                        ),
                        List.of(treeMaker.Ident(names.fromString("this")))
                )
        );

        constructor.body.stats = constructor.body.stats.append(validationCall);
    }
}