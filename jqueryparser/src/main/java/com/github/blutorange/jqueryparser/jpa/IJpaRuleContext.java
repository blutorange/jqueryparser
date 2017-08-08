package com.github.blutorange.jqueryparser.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;

import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

interface IJpaRuleContext {
	CriteriaBuilder getCriteriaBuilder();
	Path<@Nullable String> getStringPath() throws QueryBuilderEvaluatorException;
	Path<? extends @Nullable Integer> getIntegerPath() throws QueryBuilderEvaluatorException;
	Path<? extends @Nullable Double> getDoublePath() throws QueryBuilderEvaluatorException;
}