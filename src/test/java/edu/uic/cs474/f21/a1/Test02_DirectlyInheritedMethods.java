package edu.uic.cs474.f21.a1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class Test02_DirectlyInheritedMethods {

    @Test
    public void findDeclaredMethod() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test02_DirectlyInheritedMethods");

        {
            // The program `B b = ...;  b.onlyInB();` can only call method `B.onlyInB()`
            Set<String> result = main.explain("B", "onlyInB");
            Assert.assertEquals(Set.of("B"), result);
        }
    }

    @Test
    public void findOverridenMethod() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test02_DirectlyInheritedMethods");

        {
            // The program `B b = ...;  b.overriden();` can only call method `B.overriden()`
            Set<String> result = main.explain("B", "overriden");
            Assert.assertEquals(Set.of("B"), result);
        }

//        {
//            // The program `A a = ...;  a.overriden();` can only methods `A.overriden()` when a = new A(); or method `B.overriden()` when a = new B();
//            Set<String> result = main.explain("B", "overriden");
//            Assert.assertEquals(Set.of("B"), result);
//        }
    }

    @Test
    public void findInherited() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test02_DirectlyInheritedMethods");

        {
            // The program `A a = ...;  a.inherited();` can only call method `A.inherited()`
            Set<String> result = main.explain("A", "inherited");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `B b = ...;  b.inherited();` can only call method `A.inherited()`
            Set<String> result = main.explain("B", "inherited");
            Assert.assertEquals(Set.of("A"), result);
        }
    }

    public static class A {
        public void inherited() { System.out.println("A.inherited()"); }
        public void overriden() { System.out.println("A.overriden()"); }
    }

    public static class B extends A {
        public void overriden() { System.out.println("B.overriden()"); }
        public void onlyInB()   { System.out.println("B.onlyInB()"); }
    }
}
