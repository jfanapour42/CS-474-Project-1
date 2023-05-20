package edu.uic.cs474.f21.a1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class Test08_TransitiveSubclasses {
    // TODO add abstract methods going down

    @Test
    public void findOverridenHoleMethod() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test08_TransitiveSubclasses");

        {
            // The program `C c = ...;  c.ac();` can only call method `C.ac()`
            Set<String> result = main.explain("C", "ac");
            Assert.assertEquals(Set.of("C"), result);
        }

        {
            // The program `B b = ...;  b.ac();` can call methods `A.ac()` or `C.ac()`
            Set<String> result = main.explain("B", "ac");
            Assert.assertEquals(Set.of("A", "C"), result);
        }

        {
            // The program `A a = ...;  a.ac();` can call methods `A.ac()` when a = new A(); or `C.ac()` when a = new C();
            Set<String> result = main.explain("A", "ac");
            Assert.assertEquals(Set.of("A", "C"), result);
        }
    }

    @Test
    public void findOverridenMethod() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test08_TransitiveSubclasses");

        {
            // The program `C c = ...;  c.abc();` can only call method `C.abc()`
            Set<String> result = main.explain("C", "abc");
            Assert.assertEquals(Set.of("C"), result);
        }

        {
            // The program `B b = ...;  b.abc();` can call methods `B.abc()` when b = new B(); or `C.abc()` when b = new C();
            Set<String> result = main.explain("B", "abc");
            Assert.assertEquals(Set.of("B", "C"), result);
        }

        {
            // The program `A a = ...;  a.abc();` can call methods `A.abc()` when a = new A(); or `B.abc()` when a = new B() or `C.abc()` when a = new C();
            Set<String> result = main.explain("A", "abc");
            Assert.assertEquals(Set.of("A", "B", "C"), result);
        }
    }


    public static class A {
        public void abc() { System.out.println("A.abc()"); }
        public void ac() { System.out.println("A.ac()"); }
    }

    public static class B extends A {
        public void abc() { System.out.println("B.abc()"); }
    }

    public static class C extends B {
        public void abc() { System.out.println("C.abc()"); }
        public void ac() { System.out.println("C.ac()"); }
    }

}
