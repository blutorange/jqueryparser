package com.github.blutorange.jqueryparser.immediate;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

@FunctionalInterface
public interface IType<@NonNull C> {
	boolean operate(C context, String operatorString, Optional<String> left, @NonNull String  [] right) throws QueryBuilderEvaluatorException;
}