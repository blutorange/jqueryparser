package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;

public interface IRuleFactory<@NonNull T, @NonNull C> {
	public IRule<@NonNull T, @NonNull C> getFor(final C context, String id) throws QueryBuilderEvaluatorException;
}
