package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;

public interface IOperator<T, C, S> {
	T operate(final C context, final S object, @NonNull final String[] values) throws QueryBuilderEvaluatorException;
}
