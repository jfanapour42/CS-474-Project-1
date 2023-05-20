package edu.uic.cs474.f21.a1;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import edu.uic.cs474.f21.a1.Solution.A1Solution;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {

    public static DynamicDispatchExplainer getExplainer() {

        return new A1Solution();
    }

    private Map<String, ClassOrInterfaceDeclaration> classes = new HashMap<>();

    public void processClass(String className) {
        // Get file for target class
        Path baseDir = Paths.get(System.getProperty("user.dir"));
        Path testFolder = Paths.get(baseDir.toAbsolutePath().toString(), "src", "test", "java");
        Path filePath = Paths.get(testFolder.toAbsolutePath().toString(), className.replace(".", File.separator) + ".java");

        // Parse Java file
        CompilationUnit cu;
        try {
            cu = new JavaParser().parse(filePath).getResult().orElseThrow();
        } catch (IOException e) {
            throw new Error(e);
        }


        // Get nested classes
        Map<String, ClassOrInterfaceDeclaration> classes = new HashMap<>();
        for (TypeDeclaration<?> t : cu.getTypes()) {
            for (BodyDeclaration<?> b : t.getMembers()) {
                if (b.isClassOrInterfaceDeclaration()) {
                    ClassOrInterfaceDeclaration c = b.asClassOrInterfaceDeclaration();
                    this.classes.put(c.getNameAsString(), c);
                }
            }
        }
    }

    /*default*/ Map<String, ClassOrInterfaceDeclaration> getClasses() {
        if (!new Exception().getStackTrace()[1].getClassName().startsWith("edu.uic.cs474.f21.a1.Test"))
            throw new Error("Calling getClasses is not allowed in student code");
        return this.classes;
    }

    /*default*/ void setClasses(Map<String, ClassOrInterfaceDeclaration> classes) {
        if (!new Exception().getStackTrace()[1].getClassName().startsWith("edu.uic.cs474.f21.a1.Test"))
            throw new Error("Calling getClasses is not allowed in student code");
        this.classes = classes;
    }

    public Set<String> explain(String receiverType, String methodName, String ... argumentTypes) {
        return getExplainer().explain(this.classes, receiverType, methodName, argumentTypes);
    }
}
