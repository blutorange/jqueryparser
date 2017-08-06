package com.github.blutorange.jqueryparser.querydsl;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.EnumConditionFactory;
import com.github.blutorange.jqueryparser.EvaluatorBuilder;
import com.github.blutorange.jqueryparser.OperatorRuleFactoryBuilder;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.RelationalPath;

public class QuerySqlEvaluatorBuilder<@NonNull T>
		extends EvaluatorBuilder<@NonNull BooleanExpression, @NonNull QuerySqlContext> {
	public QuerySqlEvaluatorBuilder(final RelationalPath<T> path) throws QueryBuilderEvaluatorException {
		setContextSupplier(new QuerySqlContext(path));
	}

	public QuerySqlEvaluatorBuilder<T> defaults() throws QueryBuilderEvaluatorException {
		setConditionFactory(new EnumConditionFactory<>(EQuerySqlCondition.class));
		setRuleFactory(OperatorRuleFactoryBuilder.create(b -> b.addOperators(EQuerySqlOperator.class)));
		return this;
	}
}