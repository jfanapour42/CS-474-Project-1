package edu.uic.cs474.f21.a1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Test10_ManyHierarchiesSubclasses {

    @Test
    public void testStraightHierarchy() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test10_ManyHierarchiesSubclasses");

        {
            // The program `A a = ...;  a.abc();` can call methods `A.abc()`, `B.abc()`, and `C.abc()`
            Set<String> result = main.explain("A", "abc");
            Assert.assertEquals(Set.of("A", "B", "C"), result);
        }

        {
            // The program `A a = ...;  a.ac();` can call methods `A.ac()`, and `C.ac()`
            Set<String> result = main.explain("A", "ac");
            Assert.assertEquals(Set.of("A", "C"), result);
        }

        {
            // The program `A a = ...;  a.ab();` can call methods `A.ab()`, and `B.ab()`
            Set<String> result = main.explain("A", "ab");
            Assert.assertEquals(Set.of("A", "B"), result);
        }

        {
            // The program `B b = ...;  b.abc();` can call methods `B.abc()` and `C.abc()`
            Set<String> result = main.explain("B", "abc");
            Assert.assertEquals(Set.of("B", "C"), result);
        }

        {
            // The program `B b = ...;  b.ac();` can call methods `A.ac()` and `C.ac()`
            Set<String> result = main.explain("B", "ac");
            Assert.assertEquals(Set.of("A", "C"), result);
        }

        {
            // The program `B b = ...;  b.bc();` can call methods `B.bc()` and `C.bc()`
            Set<String> result = main.explain("B", "bc");
            Assert.assertEquals(Set.of("B", "C"), result);
        }

        {
            // The program `C c = ...;  c.abc();` can only call method `A.abc()`
            Set<String> result = main.explain("C", "abc");
            Assert.assertEquals(Set.of("C"), result);
        }

        {
            // The program `C c = ...;  c.ab();` can only call method `B.ab()`
            Set<String> result = main.explain("C", "ab");
            Assert.assertEquals(Set.of("B"), result);
        }

        {
            // The program `C c = ...;  c.bc();` can only call method `C.bc()`
            Set<String> result = main.explain("C", "bc");
            Assert.assertEquals(Set.of("C"), result);
        }

        {
            // The program `C c = ...;  c.ac();` can only call method `C.ac()`
            Set<String> result = main.explain("C", "ac");
            Assert.assertEquals(Set.of("C"), result);
        }

    }

    @Test
    public void testSideHierarchies() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test10_ManyHierarchiesSubclasses");

        {
            // The program `Amiddle a = ...;  a.abc();` can call methods from all classes in that hierarchy
            Set<String> result = main.explain("Amiddle", "abc");
            Assert.assertEquals(Set.of("Amiddle", "Bleft", "Bright", "Cleft", "Cright"), result);
        }

        {
            Set<String> result = main.explain("Amiddle", "ac");
            Assert.assertEquals(Set.of("Amiddle", "Cleft", "Cright"), result);
        }

        {
            Set<String> result = main.explain("Amiddle", "ab");
            Assert.assertEquals(Set.of("Amiddle", "Bleft", "Bright"), result);
        }

        {
            Set<String> result = main.explain("Bleft", "abc");
            Assert.assertEquals(Set.of("Bleft", "Cleft"), result);
        }

        {
            Set<String> result = main.explain("Bleft", "ac");
            Assert.assertEquals(Set.of("Amiddle", "Cleft"), result);
        }

        {
            Set<String> result = main.explain("Bleft", "ab");
            Assert.assertEquals(Set.of("Bleft"), result);
        }

        {
            Set<String> result = main.explain("Bleft", "bc");
            Assert.assertEquals(Set.of("Bleft", "Cleft"), result);
        }

        {
            Set<String> result = main.explain("Bright", "abc");
            Assert.assertEquals(Set.of("Bright", "Cright"), result);
        }

        {
            Set<String> result = main.explain("Bright", "ac");
            Assert.assertEquals(Set.of("Amiddle", "Cright"), result);
        }

        {
            Set<String> result = main.explain("Bright", "ab");
            Assert.assertEquals(Set.of("Bright"), result);
        }

        {
            Set<String> result = main.explain("Bright", "bc");
            Assert.assertEquals(Set.of("Bright", "Cright"), result);
        }

    }

    /*
           Amiddle          A
              |             |
          ---------         |
          |       |         |
        Bleft   Bright      B
          |       |         |
        Cleft   Cright      C
     */

    public static class A {
        public void abc() { System.out.println("A.abc()"); }
        public void ab() { System.out.println("A.ab()"); }
        public void ac() { System.out.println("C.ac()"); }
    }

    public static class B extends A {
        public void abc() { System.out.println("B.abc()"); }
        public void ab() { System.out.println("B.ab()"); }
        public void bc() { System.out.println("B.ab()"); }
    }

    public static class C extends B {
        public void abc() { System.out.println("C.abc()"); }
        public void ac() { System.out.println("C.ac()"); }
        public void bc() { System.out.println("C.bc()"); }
    }

    public static class Amiddle {
        public void abc() { System.out.println("Amiddle.abc()"); }
        public void ab() { System.out.println("Amiddle.ac()"); }
        public void ac() { System.out.println("Amiddle.ac()"); }
    }

    public static class Bleft extends Amiddle {
        public void abc() { System.out.println("Bleft.abc()"); }
        public void ab() { System.out.println("Bleft.ac()"); }
        public void bc() { System.out.println("Bleft.bc()"); }
    }

    public static class Cleft extends Bleft {
        public void abc() { System.out.println("Cleft.abc()"); }
        public void ac() { System.out.println("Cleft.ac()"); }
        public void bc() { System.out.println("Cleft.bc()"); }
    }

    public static class Bright extends Amiddle {
        public void abc() { System.out.println("Bright.abc()"); }
        public void ab() { System.out.println("Bright.ac()"); }
        public void bc() { System.out.println("Bright.bc()"); }
    }

    public static class Cright extends Bright {
        public void abc() { System.out.println("Cright.abc()"); }
        public void ac() { System.out.println("Cright.ac()"); }
        public void bc() { System.out.println("Cright.bc()"); }
    }
}
