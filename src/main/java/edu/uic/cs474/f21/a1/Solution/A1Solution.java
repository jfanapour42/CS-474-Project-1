package edu.uic.cs474.f21.a1.Solution;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import edu.uic.cs474.f21.a1.DynamicDispatchExplainer;

import java.util.*;

public class A1Solution implements DynamicDispatchExplainer {
    //
    // creates the ClassOrInterfaceDeclaration for java.lang.Object
    //
    private ClassOrInterfaceDeclaration createObjectClass() {
        ClassOrInterfaceDeclaration obj = new ClassOrInterfaceDeclaration();
        obj.setName("java.lang.Object");

        obj.addMethod("toString", Modifier.Keyword.PUBLIC);
        obj.addMethod("hashCode", Modifier.Keyword.PUBLIC);
        obj.addMethod("clone", Modifier.Keyword.PUBLIC);
        obj.addMethod("wait", Modifier.Keyword.PUBLIC);
        obj.addMethod("notify", Modifier.Keyword.PUBLIC);
        obj.addMethod("notifyAll", Modifier.Keyword.PUBLIC);
        NodeList<Parameter> nl = new NodeList<>();
        ClassOrInterfaceType objType = new ClassOrInterfaceType();
        objType.setName("java.lang.Object");
        nl.add(new Parameter(objType, "obj"));
        obj.addMethod("equals", Modifier.Keyword.PUBLIC).setParameters(nl);

        return obj;
    }

    //
    // Adds java.lang.Object to classes and extends every class in classes without
    // a superclass by java.lang.Object
    //
    private void addObjectClass(Map<String, ClassOrInterfaceDeclaration> classes) {
        ClassOrInterfaceDeclaration obj = createObjectClass();
        ClassOrInterfaceType objType = new ClassOrInterfaceType();
        NodeList<ClassOrInterfaceType> extendedTypes = new NodeList<>();

        objType.setName(obj.getNameAsString());
        extendedTypes.add(objType);

        for(ClassOrInterfaceDeclaration c : classes.values()) {
            if(c.getExtendedTypes().isEmpty()) {
                c.setExtendedTypes(extendedTypes);
            }
        }
        classes.put(obj.getNameAsString(), obj);
    }

    //
    // Returns an ArrayList of direct subclass of the parameter superClass which
    // are found with in the Map classes.
    //
    private ArrayList<ClassOrInterfaceDeclaration> getDirectSubclasses(Map<String, ClassOrInterfaceDeclaration> classes, String superclass) {
        ArrayList<ClassOrInterfaceDeclaration> list = new ArrayList<>();
        for(ClassOrInterfaceDeclaration classDec : classes.values()) {
            if(!classDec.getExtendedTypes().isEmpty()){
                String superTypeName = classDec.getExtendedTypes().get(0).getNameAsString();
                if(superTypeName.equals(superclass)) {
                    list.add(classDec);
                }
            }
        }
        return list;
    }

    //
    // Finds all subclasses of superClass found within the map classes.
    // will recursively call itself and populate the ArrayList result until
    // the entire tree of subclasses are found.
    //
    private void getAllSubclassesRec(Map<String, ClassOrInterfaceDeclaration> classes, String superclass, ArrayList<ClassOrInterfaceDeclaration> result) {
        ArrayList<ClassOrInterfaceDeclaration> directSubclasses = getDirectSubclasses(classes, superclass);
        if(!directSubclasses.isEmpty()) {
            result.addAll(directSubclasses);
            for(ClassOrInterfaceDeclaration subclass : directSubclasses) {
                getAllSubclassesRec(classes, subclass.getNameAsString(), result);
            }
        }
    }

    //
    // Calls the recursive function getAllSubClassesRec and returns result
    //
    private ArrayList<ClassOrInterfaceDeclaration> getAllSubClasses(Map<String, ClassOrInterfaceDeclaration> classes, String superclass) {
        ArrayList<ClassOrInterfaceDeclaration> result = new ArrayList<>();
        getAllSubclassesRec(classes, superclass, result);
        return result;
    }

    @Override
    public Set<String> explain(Map<String, ClassOrInterfaceDeclaration> classes, String receiverType, String methodName, String ... argumentTypes) {
        Set<String> ret = new HashSet<>();
        // Account for inheritance from java.lang.Object
        addObjectClass(classes);
        ClassOrInterfaceDeclaration classDec = classes.get(receiverType);
        // check if receiverType exists
        if(classDec == null) {return ret;}
        List<MethodDeclaration> methods = classDec.getMethodsBySignature(methodName, argumentTypes);

        if(!methods.isEmpty()) {
            MethodDeclaration methodDec = methods.get(0);
            if(!methodDec.isAbstract()) {
                ret.add(classDec.getNameAsString());
            }
        } else {
            ClassOrInterfaceDeclaration superclassDec = classDec;
            // See if method is inherited
            while(!superclassDec.getExtendedTypes().isEmpty()) {
                ClassOrInterfaceType superType = superclassDec.getExtendedTypes().get(0);
                superclassDec = classes.get(superType.getNameAsString());
                methods = superclassDec.getMethodsBySignature(methodName, argumentTypes);

                if (!methods.isEmpty()) {
                    MethodDeclaration methodDec = methods.get(0);
                    if (methodDec.isPublic() && !methodDec.isAbstract() && !methodDec.isStatic()) {
                        ret.add(superclassDec.getName().asString());
                    }
                    break;
                }
            }
        }

        // If ret is empty, then the method is nether declared in the receiverType
        // nor is it inherited. Therefore, we would not have to check the receiverType's
        // subclasses.
        if(!ret.isEmpty()) {
            // Find method overrides in subclasses
            String superclass = classDec.getNameAsString();
            ArrayList<ClassOrInterfaceDeclaration> subclasses = getAllSubClasses(classes, superclass);
            for (ClassOrInterfaceDeclaration subclass : subclasses) {
                methods = subclass.getMethodsBySignature(methodName, argumentTypes);
                if (!methods.isEmpty()) {
                    MethodDeclaration methodDec = methods.get(0);
                    if (methodDec.isPublic() && !methodDec.isAbstract() && !methodDec.isStatic()) {
                        ret.add(subclass.getNameAsString());
                    }
                }
            }
        }

        return ret;
    }
}
