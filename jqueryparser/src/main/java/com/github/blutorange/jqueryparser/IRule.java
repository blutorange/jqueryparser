package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public interface IRule<@NonNull T, @NonNull C> {
	T map(C context, String type, String operator, @Nullable String input, @NonNull String... value) throws QueryBuilderEvaluatorException;
}