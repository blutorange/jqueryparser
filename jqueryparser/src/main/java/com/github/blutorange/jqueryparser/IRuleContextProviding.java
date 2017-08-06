package com.github.blutorange.jqueryparser;

public interface IRuleContextProviding<C, S> {
	S getFor(C context, String id) throws QueryBuilderEvaluatorException;
}
