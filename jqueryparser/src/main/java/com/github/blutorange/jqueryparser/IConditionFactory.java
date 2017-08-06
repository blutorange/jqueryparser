package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;

public interface IConditionFactory<@NonNull T, @NonNull C> {
	ICondition<@NonNull T, @NonNull C> getFor(C context, String conditionString) throws QueryBuilderEvaluatorException;
}
