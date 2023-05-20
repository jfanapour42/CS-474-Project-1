package edu.uic.cs474.f21.a1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class Test07_DirectSubclasses {

    @Test
    public void findOverridenMethod() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test07_DirectSubclasses");

        {
            // The program `B b = ...;  b.overriden();` can only call method `B.overriden()`
            Set<String> result = main.explain("B", "overriden");
            Assert.assertEquals(Set.of("B"), result);
        }

        {
            // The program `A a = ...;  a.overriden();` can call methods `A.overriden()` when a = new A(); or `B.overriden()` when a = new B();
            Set<String> result = main.explain("A", "overriden");
            Assert.assertEquals(Set.of("A", "B"), result);
        }
    }

    public static class A {
        public void inherited() { System.out.println("A.inherited()"); }
        public void overriden() { System.out.println("A.overriden()"); }
    }

    public static class B extends A {
        public void overriden() { System.out.println("B.overriden()"); }
    }

}
