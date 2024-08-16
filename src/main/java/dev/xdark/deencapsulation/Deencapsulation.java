package dev.xdark.deencapsulation;

import sun.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

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

	public static void deencapsulate(Class<?> caller) {
		Set<Module> modules = new HashSet<>();
		Module base = caller.getModule();
		ModuleLayer baseLayer = base.getLayer();
		if (baseLayer != null)
			modules.addAll(baseLayer.modules());
		modules.addAll(ModuleLayer.boot().modules());
		for (ClassLoader cl = caller.getClassLoader(); cl != null; cl = cl.getParent()) {
			modules.add(cl.getUnnamedModule());
		}
		try {
			MethodHandle export = lookup(Module.class).findVirtual(Module.class, "implAddOpens", MethodType.methodType(void.class, String.class));
			for (Module module : modules) {
				for (String name : module.getPackages()) {
					export.invokeExact(module, name);
				}
			}
		} catch (Throwable t) {
			throw new IllegalStateException("Could not export packages", t);
		}
	}
}
