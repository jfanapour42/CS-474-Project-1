package edu.uic.cs474.f21.a1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class Test01_DirectlyDeclaredMethods {

    @Test
    public void findSingleDeclaredMethod() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test01_DirectlyDeclaredMethods");

        {
            // The program `A a = new A();  a.methodA();` can only call method `A.methodA()`
            Set<String> result = main.explain("A", "methodA");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `B b = new B();  b.methodB();` can only call method `B.methodB()`
            Set<String> result = main.explain("B", "methodB");
            Assert.assertEquals(Set.of("B"), result);
        }
    }

    @Test
    public void findMethodWithSameName() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test01_DirectlyDeclaredMethods");

        {
            // The program `A a = new A();  a.m();` can only call method `A.m()`
            Set<String> result = main.explain("A", "m");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `B b = new B();  b.m();` can only call method `B.m()`
            Set<String> result = main.explain("B", "m");
            Assert.assertEquals(Set.of("B"), result);
        }
    }

    public static class A {
        public void methodA() { }

        public void m() { }
    }

    public static class B {
        public void methodB() { }

        public void m() { }
    }
}
