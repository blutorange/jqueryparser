package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;

public interface NonNullSupplier<T> {
	@NonNull
	public T get();
}
