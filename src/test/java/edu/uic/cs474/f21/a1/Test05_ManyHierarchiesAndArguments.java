package edu.uic.cs474.f21.a1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Test05_ManyHierarchiesAndArguments {

    @Test
    public void testStraightHierarchy() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test05_ManyHierarchiesAndArguments");

        {
            // The program `A a = ...;  a.abc(A,B,C);` can only call method `A.abc(A,B,C)`
            Set<String> result = main.explain("A", "abc", "A", "B", "C");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `B b = ...;  b.abc(A,B,C);` can only call method `A.abc(A,B,C)`
            Set<String> result = main.explain("B", "abc", "A", "B", "C");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `C c = ...;  c.abc(A,B,C);` can only call method `A.abc(A,B,C)`
            Set<String> result = main.explain("C", "abc", "A", "B", "C");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `B b = ...;  b.bc(B,C);` can only call method `B.bc(B,C)`
            Set<String> result = main.explain("B", "bc", "B", "C");
            Assert.assertEquals(Set.of("B"), result);
        }

        {
            // The program `C c = ...;  c.bc(B,C);` can only call method `B.bc(B,C)`
            Set<String> result = main.explain("C", "bc", "B", "C");
            Assert.assertEquals(Set.of("B"), result);
        }

        {
            // The program `C c = ...;  c.c(C c);` can only call method `C.c(C)`
            Set<String> result = main.explain("C", "c", "C");
            Assert.assertEquals(Set.of("C"), result);
        }
    }

    @Test
    public void testSideHierarchies() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test05_ManyHierarchiesAndArguments");

        // Any program `a.abc(A,B,C);` can only call method `Amiddle.abc(A,B,C)`
        {
            List<String> types = new LinkedList<>(List.of("Amiddle", "Bleft", "Cleft", "Bright", "Cright"));
            Collections.shuffle(types);
            for(String receiverType : types) {
                Set<String> result = main.explain(receiverType, "abc", "A", "B", "C");
                Assert.assertEquals(Set.of("Amiddle"), result);
            }
        }

        {
            // The program `Bleft b = ...;  b.bc();` can only call method `Bleft.bc()`
            Set<String> result = main.explain("Bleft", "bc", "B", "C");
            Assert.assertEquals(Set.of("Bleft"), result);
        }

        {
            // The program `Cleft c = ...;  c.bc();` can only call method `Bleft.bc()`
            Set<String> result = main.explain("Cleft", "bc", "B", "C");
            Assert.assertEquals(Set.of("Bleft"), result);
        }

        {
            // The program `Cleft c = ...;  c.c();` can only call method `Cleft.c()`
            Set<String> result = main.explain("Cleft", "c", "C");
            Assert.assertEquals(Set.of("Cleft"), result);
        }

        {
            // The program `Bright b = ...;  b.bc();` can only call method `Bright.bc()`
            Set<String> result = main.explain("Bright", "bc", "B", "C");
            Assert.assertEquals(Set.of("Bright"), result);
        }

        {
            // The program `Cright c = ...;  c.bc();` can only call method `Bright.bc()`
            Set<String> result = main.explain("Cright", "bc", "B", "C");
            Assert.assertEquals(Set.of("Bright"), result);
        }

        {
            // The program `Cright c = ...;  c.c();` can only call method `Cright.c()`
            Set<String> result = main.explain("Cright", "c", "C");
            Assert.assertEquals(Set.of("Cright"), result);
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
        public void abc(A a, B b, C c) { System.out.println("A.abc(A, B, C)"); }

        public void abc() { System.out.println("A.abc()"); }
        public void bc()  { System.out.println("A.bc()");  }
        public void c()   { System.out.println("A.c()");  }
    }

    public static class B extends A {
        public void bc(B b, C c) { System.out.println("B.bc()"); }

        public void abc() { System.out.println("B.abc()"); }
        public void bc()  { System.out.println("B.bc()");  }
        public void c()   { System.out.println("B.c()");  }
    }

    public static class C extends B {
        public void c(C c) { System.out.println("C.c()"); }

        public void abc() { System.out.println("C.abc()"); }
        public void bc()  { System.out.println("C.bc()");  }
        public void c()   { System.out.println("C.c()");  }
    }

    public static class Amiddle {
        public void abc(A a, B b, C c) { System.out.println("Amiddle.abc(A,B,C)"); }

        public void abc() { System.out.println("Amiddle.abc()"); }
        public void bc()  { System.out.println("Amiddle.bc()");  }
        public void c()   { System.out.println("Amiddle.c()");  }
    }

    public static class Bleft extends Amiddle {
        public void bc(B b, C c) { System.out.println("Bleft.bc(B,C)"); }

        public void abc() { System.out.println("Amiddle.abc()"); }
        public void bc()  { System.out.println("Amiddle.bc()");  }
        public void c()   { System.out.println("Amiddle.c()");  }
    }

    public static class Cleft extends Bleft {
        public void c(C c) { System.out.println("Cleft.c(C)"); }

        public void abc() { System.out.println("Amiddle.abc()"); }
        public void bc()  { System.out.println("Amiddle.bc()");  }
        public void c()   { System.out.println("Amiddle.c()");  }
    }

    public static class Bright extends Amiddle {
        public void bc(B b, C c) { System.out.println("Bright.bc(B b, C c)"); }

        public void abc() { System.out.println("Amiddle.abc()"); }
        public void bc()  { System.out.println("Amiddle.bc()");  }
        public void c()   { System.out.println("Amiddle.c()");  }
    }

    public static class Cright extends Bright {
        public void c(C c) { System.out.println("Cright.c(C)"); }

        public void abc() { System.out.println("Amiddle.abc()"); }
        public void bc()  { System.out.println("Amiddle.bc()");  }
        public void c()   { System.out.println("Amiddle.c()");  }
    }
}
