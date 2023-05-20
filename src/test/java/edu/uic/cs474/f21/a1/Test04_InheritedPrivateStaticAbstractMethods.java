package edu.uic.cs474.f21.a1;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class  Test04_InheritedPrivateStaticAbstractMethods {

    @Test
    public void findAbstractMethods() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test04_InheritedPrivateStaticAbstractMethods");

        {
            // The program `C c = ...;  c.abstractMethod();` cannot call any method
            Set<String> result = main.explain("C", "abstractMethod");
            Assert.assertEquals(Set.of(), result);
        }

        {
            // The program `B b = ...;  b.abstractMethod();` cannot call any method
            Set<String> result = main.explain("B", "abstractMethod");
            Assert.assertEquals(Set.of(), result);
        }

        {
            // The program `A a = ...;  a.abstractMethod();` cannot call any method
            Set<String> result = main.explain("A", "abstractMethod");
            Assert.assertEquals(Set.of(), result);
        }
    }

    @Test
    public void findPrivateMethods() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test04_InheritedPrivateStaticAbstractMethods");

        {
            // The program `A a = ...;  a.privateMethod();` can only call method `A.privateMethod`
            Set<String> result = main.explain("A", "privateMethod");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `B b = ...;  b.privateMethod();` can only call method `B.privateMethod`
            Set<String> result = main.explain("B", "privateMethod");
            Assert.assertEquals(Set.of("B"), result);
        }

        {
            // The program `C c = ...;  c.privateMethod();` cannot call any method
            Set<String> result = main.explain("C", "privateMethod");
            Assert.assertEquals(Set.of(), result);
        }
    }

    @Test
    public void findStaticMethods() {
        Main main = new Main();
        main.processClass("edu.uic.cs474.f21.a1.Test04_InheritedPrivateStaticAbstractMethods");

        {
            // The program `A a = ...;  a.staticMethod();` can only call method `A.staticMethod()`
            Set<String> result = main.explain("A", "staticMethod");
            Assert.assertEquals(Set.of("A"), result);
        }

        {
            // The program `B b = ...;  b.staticMethod();` can only call method `B.staticMethod()`
            Set<String> result = main.explain("B", "staticMethod");
            Assert.assertEquals(Set.of("B"), result);
        }

        {
            // The program `C c = ...;  c.staticMethod();` cannot call any method
            Set<String> result = main.explain("C", "staticMethod");
            Assert.assertEquals(Set.of(), result);
        }
    }

    public abstract static class A {
        private void privateMethod() { System.out.println("A.privateMethod()"); }
        public static void staticMethod() { System.out.println("A.staticMethod()"); }
        public abstract void abstractMethod();

        public void abc() { System.out.println("A.abc()"); }
        public void bc()  { System.out.println("A.bc()");  }
        public void ab()  { System.out.println("A.ab()");  }
    }

    public abstract static class B extends A {
        private void privateMethod() { System.out.println("B.privateMethod()"); }
        public static void staticMethod() { System.out.println("B.staticMethod()"); }
        public abstract void abstractMethod();

        public void abc() { System.out.println("A.abc()"); }
        public void bc()  { System.out.println("A.bc()");  }
        public void ab()  { System.out.println("A.ab()");  }
    }

    public abstract static class C extends B {
        public void abc() { System.out.println("A.abc()"); }
        public void ac()  { System.out.println("A.ac()");  }
        public void bc()  { System.out.println("A.bc()");  }
    }
}
