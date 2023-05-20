package edu.uic.cs474.f21.a1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

public class Test09_JavaLangObjectDown {

    @Test
    public void testOverridenObjectMethods() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test09_JavaLangObjectDown");

        {
            // The program `A a = ...;  a.toString();` can call `Object.toString()` when `a = new A()` or `B.toString()` when `a = new B()`
            Set<String> result = main.explain("A", "toString");
            Assert.assertEquals(Set.of("B", "java.lang.Object"), result);
        }

        {
            // The program `A a = ...;  a.equals(new A());` can call method `Object.equals(Object)` when `a = new A()` or method `B.equals(Object)` when `a = new B()`
            Set<String> result = main.explain("B", "equals", "java.lang.Object");
            Assert.assertEquals(Set.of("B"), result);
        }

        {
            // The program `A a = ...;  a.equals();` can only call `A.equals`
            Set<String> result = main.explain("A", "equals");
            Assert.assertEquals(Set.of("A"), result);
        }
    }

    @Test
    public void testObjectReceiver() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test09_JavaLangObjectDown");

        {
            // The program `Object o = ...;  o.toString();` can call `Object.toString()` when `o = new C()` or `a = new A()` and `B.toString()` when `o = new B()`
            Set<String> result = main.explain("java.lang.Object", "toString");
            Assert.assertEquals(Set.of("B", "java.lang.Object"), result);
        }
        {
            // The program `Object o = ...;  o.hashCode();` can call method `Object.hashcode()` or `A.hashCode()` for `o = new A()` or `B.hashCode()` for `o = new B()` or `C.hashCode()` for `o = new C()`
            Set<String> result = main.explain("java.lang.Object", "hashCode");
            Assert.assertEquals(Set.of("java.lang.Object", "A", "B", "C"), result);
        }

        {
            // The program `Object o = ...;  o.equals(new Object());` can call method `Object.equals(Object)` for `o = new A()` and `o = new C()` or method `B.equals(Object)` for `o = new B()`
            Set<String> result = main.explain("java.lang.Object", "equals", "java.lang.Object");
            Assert.assertEquals(Set.of("java.lang.Object", "B"), result);
        }

        {
            // The program `Object o = ...;  o.wait();` can only call method `Object.wait()`
            Set<String> result = main.explain("java.lang.Object", "wait");
            Assert.assertEquals(Set.of("java.lang.Object"), result);
        }

        {
            // The program `Object o = ...;  o.notify();` can only call method `Object.notify()`
            Set<String> result = main.explain("java.lang.Object", "notify");
            Assert.assertEquals(Set.of("java.lang.Object"), result);
        }

        {
            // The program `Object o = ...;  object.notifyAll();` can only call method `Object.notifyAll()`
            Set<String> result = main.explain("java.lang.Object", "notifyAll");
            Assert.assertEquals(Set.of("java.lang.Object"), result);
        }

        {
            // The program `Object o = ...;  object.clone();` can only call method `Object.clone()`
            Set<String> result = main.explain("java.lang.Object", "clone");
            Assert.assertEquals(Set.of("java.lang.Object"), result);
        }
    }

    public static class A {
        public boolean equals() { return false; }

        public int hashCode() { return 0; }
    }

    public static class B extends A {
        public int hashCode() { return 0; }

        public boolean equals(java.lang.Object obj) { return false; }

        public String toString() { return ""; }
    }

    public static class C {
        public int hashCode() { return 0; }

        public void notify(java.lang.Object o) { }
    }

}
