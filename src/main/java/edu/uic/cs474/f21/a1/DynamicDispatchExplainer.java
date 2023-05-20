package edu.uic.cs474.f21.a1;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;

import java.util.Map;
import java.util.Set;

public interface DynamicDispatchExplainer {

    /**
     * Lists all methods that be called due to dynamic dispatch in Java
     *
     * Ex.  List l = ...
     *      l.add("Hello");
     *      |  |    |
     *      |  |     ->  argumentTypes is [ "String" ]
     *      |   -> methodName is "add"
     *       -> receiverType is "LinkedList"
     *
     *       In the following hierarchy:
     *          class LinkedList extends List       { void add(String s); }
     *          class List       extends Collection { }
     *          class Collection                    { void add(String s); }
     *
     *       Calling this method should return { "Collection", "LinkedList" }
     *                                                 |             |
     *                                                 |              -> List l = new LinkedList();
     *                                                  -> List l = new List();
     *
     * @param classes         All known classes
     * @param receiverType    The class name of the receiver object
     * @param methodName      The name of the method called
     * @param argumentTypes   The class name of each method argument
     * @return                A set containing all the classes in the hierarchy that have a method that may be called
     */
    Set<String> explain(Map<String, ClassOrInterfaceDeclaration> classes, String receiverType, String methodName, String ... argumentTypes);

}
