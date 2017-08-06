package com.github.blutorange.jqueryparser.querydsl;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.IOperator;
import com.github.blutorange.jqueryparser.IOperatorNameProviding;
import com.github.blutorange.jqueryparser.ITypeNameProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

enum EQuerySqlOperator implements IOperator<BooleanExpression, QuerySqlContext, SimpleExpression<Object>>, ITypeNameProviding, IOperatorNameProviding {
	EQUAL(1, (expr, vals) -> expr.eq(vals[0])),
	NOT_IN(0, Integer.MAX_VALUE, (expr, vals) -> expr.notIn((@NonNull Object[])vals)),
	IN(0, Integer.MAX_VALUE, (expr, vals) -> expr.in((@NonNull Object[])vals)),
	NOT_EQUAL(1, (expr, vals) -> expr.ne(vals[0])),
	IS_EMPTY(0, (expr, vals) -> expr.isNull().or(expr.eq(StringUtils.EMPTY))),
	IS_NOT_EMPTY(0, (expr, vals) -> expr.isNotNull().and(expr.ne(StringUtils.EMPTY))),
	IS_NULL(0, (expr, vals) -> expr.isNull()),
	IS_NOT_NULL(0, (expr, vals) -> expr.isNotNull()),
	LESS(1, (expr, vals) -> asComparable(expr).lt(vals[0])),
	GREATER(1, (expr, vals) -> asComparable(expr).gt(vals[0])),
	LESS_OR_EQUAL(1, (expr, vals) -> asComparable(expr).loe(vals[0])),
	GREATER_OR_EQUAL(1, (expr, vals) -> asComparable(expr).goe(vals[0])),
	BETWEEN(2, (expr, vals) -> asComparable(expr).between(vals[0], vals[1])),
	NOT_BETWEEN(2, (expr, vals) -> asComparable(expr).notBetween(vals[0], vals[1])),
	BEGINS_WITH(1, (expr, vals) -> asString(expr).startsWith(vals[0])),
	NOT_BEGINS_WITH(1, (expr, vals) -> asString(expr).startsWith(vals[0]).not()),
	ENDS_WITH(1, (expr, vals) -> asString(expr).endsWith(vals[0])),
	NOT_ENDS_WITH(1, (expr, vals) -> asString(expr).endsWith(vals[0]).not()),
	CONTAINS(1, (expr, vals) -> asString(expr).contains(vals[0])),
	NOT_CONTAINS(1, (expr, vals) -> asString(expr).contains(vals[0]).not()),
	;

	private int min;
	private int max;
	private Mapper mapper;

	private EQuerySqlOperator(final int exactly, final Mapper mapper) {
		this(exactly, exactly, mapper);
	}

	private static StringExpression asString(final SimpleExpression expr) throws QueryBuilderEvaluatorException {
		if (expr instanceof StringExpression) return (StringExpression)expr;
		throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_OPERATOR, "not string expression");
	}

	private static ComparableExpression<Comparable> asComparable(final SimpleExpression expr) throws QueryBuilderEvaluatorException {
		if (expr instanceof ComparableExpression) return (ComparableExpression<Comparable>)expr;
		throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_OPERATOR, "not comparable");
	}

	private EQuerySqlOperator(final int min, final int max, final Mapper mapper) {
		this.min = min;
		this.max = max;
		this.mapper = mapper;
	}

	private interface Mapper {
		BooleanExpression apply(SimpleExpression<Object> expr, @NonNull String[] values) throws QueryBuilderEvaluatorException;
	}

	@Override
	public BooleanExpression operate(final SimpleExpression<Object> expr, @NonNull final String[] value)
			throws QueryBuilderEvaluatorException {
		if (value.length < min || value.length > max)
			throw new QueryBuilderEvaluatorException(Codes.ILLEGAL_NUMBER_OF_VALUES, String.valueOf(value.length));
		return mapper.apply(expr, value);
	}

	@Override
	public String getOperatorName() {
		return name().toLowerCase(Locale.ROOT);
	}

	@Override
	public String getTypeName() {
		return "string"; //$NON-NLS-1$
	}
}