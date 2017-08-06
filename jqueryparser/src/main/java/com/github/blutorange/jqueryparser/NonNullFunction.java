package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;

public interface NonNullFunction<S,T> {
	@NonNull
	T apply(S argument);
}
