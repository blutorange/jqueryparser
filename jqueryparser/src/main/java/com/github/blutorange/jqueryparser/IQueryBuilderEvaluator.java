package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;

// TODO remove context C, no point
public interface IQueryBuilderEvaluator<@NonNull T, @NonNull C> {
	public T evaluate(final JSONObject group) throws QueryBuilderEvaluatorException;
}
