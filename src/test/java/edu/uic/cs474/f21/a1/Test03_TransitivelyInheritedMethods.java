package edu.uic.cs474.f21.a1;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class Test03_TransitivelyInheritedMethods {

    @Test
    public void findOverridenMethods() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test03_TransitivelyInheritedMethods");

        {
            // The program `G g = ...;  g.overriden();` can only call method `G.overriden()`
            Set<String> result = main.explain("G", "overriden");
            Assert.assertEquals(Set.of("G"), result);
        }

    }

    @Test
    public void findInheritedMethods() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test03_TransitivelyInheritedMethods");

        {
            // The program `C c = ...;  c.inherited();` can only call method `A.inherited()`
            Set<String> result = main.explain("C", "inherited");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `B b = ...;  b.inherited();` can only call method `A.inherited()`
            Set<String> result = main.explain("B", "inherited");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `A a = ...;  a.inherited();` can only call method `A.inherited()`
            Set<String> result = main.explain("A", "inherited");
            Assert.assertEquals(Set.of("A"), result);
        }
    }

    @Test
    public void findDeepInheritedMethods() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test03_TransitivelyInheritedMethods");

        Map<String, ClassOrInterfaceDeclaration> originalMap = main.getClasses();
        List<String> classes = new LinkedList<>(List.of("A", "B", "C", "D", "E", "F", "G"));

        for (int i = 0 ; i < 5_000 ; i++) {
            // Random order for all the classes
            Collections.shuffle(classes);

            // Random traversal order for the map from class names to class declarations
            TreeMap<String, ClassOrInterfaceDeclaration> randomMap = new TreeMap<String, ClassOrInterfaceDeclaration>((c1, c2) -> {
                int i1 = classes.indexOf(c1);
                int i2 = classes.indexOf(c2);

                return Integer.compare(i1, i2);
            });

            randomMap.putAll(originalMap);
            main.setClasses(randomMap);

            for (String c : classes) {
                Set<String> result = main.explain(c, "inherited");
                Assert.assertEquals(Set.of("A"), result);
            }
        }

    }

    public static class A {
        public void inherited() { System.out.println("A.inherited()"); }
        public void overriden() { System.out.println("A.overriden()"); }
    }

    public static class B extends A {
        public void overriden() { System.out.println("B.overriden()"); }
    }

    public static class C extends B {
        public void overriden() { System.out.println("C.overriden()"); }
    }

    public static class D extends C {
        public void overriden() { System.out.println("D.overriden()"); }
    }

    public static class E extends D {
        public void overriden() { System.out.println("E.overriden()"); }
    }

    public static class F extends E {
        public void overriden() { System.out.println("F.overriden()"); }
    }

    public static class G extends F {
        public void overriden() { System.out.println("G.overriden()"); }
    }
}
