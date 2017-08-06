package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;

public interface NonNullBiFunction<R,S,T> {
	@NonNull
	T apply(R argument1, S argument2);
}
