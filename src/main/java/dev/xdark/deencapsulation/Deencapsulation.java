package dev.xdark.deencapsulation;

import sun.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class Deencapsulation {

	private Deencapsulation() {
	}

	public static MethodHandles.Lookup lookup(Class<?> lookupClass) {
		try {
			Constructor<?> ctor = ReflectionFactory.getReflectionFactory()
					.newConstructorForSerialization(MethodHandles.Lookup.class, MethodHandles.Lookup.class.getDeclaredConstructor(Class.class));
			return (MethodHandles.Lookup) ctor.newInstance(lookupClass);
		} catch (ReflectiveOperationException e) {
			Throwable t;
			if (e instanceof InvocationTargetException) {
				t = ((InvocationTargetException) e).getTargetException();
			} else {
				t = e;
			}
			throw new IllegalStateException(t);
		}
	}
}
