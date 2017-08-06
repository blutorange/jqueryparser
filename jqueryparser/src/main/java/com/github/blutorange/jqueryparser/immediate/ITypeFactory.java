package com.github.blutorange.jqueryparser.immediate;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

public interface ITypeFactory<@NonNull C> {
	IType<@NonNull C> getFor(C context, String typeString) throws QueryBuilderEvaluatorException;
}