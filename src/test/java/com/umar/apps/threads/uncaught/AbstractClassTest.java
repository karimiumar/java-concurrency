package com.umar.apps.threads.uncaught;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Demonstrates when an abstract class constructor is invoked using Reflection then {@link InstantiationException}
 * is thrown.
 */
public class AbstractClassTest {

    @Test
    void givenAbstractClass_invokeConstructorUsingReflection() {
        var constructor = AbstractClass.class.getDeclaredConstructors()[0];
        assertThatThrownBy(constructor::newInstance).isInstanceOf(InstantiationException.class);
    }
}

abstract class AbstractClass {
    AbstractClass(String test) {

    }
}
