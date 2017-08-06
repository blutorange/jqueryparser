package com.github.blutorange.jqueryparser.immediate;

import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

public enum EDoubleOperator {
	EQUAL(true, 1, (l, r) -> l == r[0]),
	BETWEEN(true, 2, (l,r) -> l >= r[0] && l <= r[1]),
	BETWEEN_CLOSED(false, 2, (l,r) -> l >= r[0] && l <= r[1]),
	NOT_BETWEEN_CLOSED(false, 2, (l,r) -> l < r[0] || l > r[1]),
	BETWEEN_OPEN(false, 2, (l,r) -> l > r[0] && l < r[1]),
	NOT_BETWEEN_OPEN(false, 2, (l,r) -> l <= r[0] || l >= r[1]),
	IS_NULL(true, 0, (l, r) -> l == null),
	IS_NOT_NULL(true, 0, (l, r) -> l != null),
	NOT_EQUAL(true, 1, (l, r) -> l != r[0]),
	LESS(false, 1, (l, r) -> l < r[0]),
	GREATER(false, 1, (l, r) -> l > r[0]),
	LESS_OR_EQUAL(false, 1, (l, r) -> l <= r[0]),
	GREATER_OR_EQUAL(false, 1, (l, r) -> l >= r[0]),
	IN(true, 0, Integer.MAX_VALUE, (l, r) -> ArrayUtils.contains(r, l)),
	NOT_IN(true, 0, Integer.MAX_VALUE, (l, r) -> !ArrayUtils.contains(r, l)),
	;

	private int max;
	private int min;
	private BiDoublePredicate function;
	private boolean allowsNull;

	private EDoubleOperator(final boolean allowsNull, final int exactly, final BiDoublePredicate function) {
		this(allowsNull, exactly, exactly, function);
	}

	private EDoubleOperator(final boolean allowsNull, final int min, final int max, final BiDoublePredicate function) {
		this.min = min;
		this.max = max;
		this.allowsNull = allowsNull;
		this.function = function;
	}

	public boolean allowsCount(final int length) {
		return length >= min && length <= max;
	}

	public boolean operate(final Optional<Double> left, final double [] right) throws QueryBuilderEvaluatorException {
		if (!allowsNull() && !left.isPresent())
			return false;
		return function.test(left.orElse(null), right);
	}

	protected static interface BiDoublePredicate {
		boolean test(Double left, double[] right);
	}

	public static Optional<Double> convert(final Optional<String> left) throws QueryBuilderEvaluatorException {
		try {
			return left.map(Double::valueOf);
		}
		catch (final NumberFormatException e) {
			throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "not a double: " + left, e);
		}
	}

	public static double convert(final String left) throws QueryBuilderEvaluatorException {
		try {
			return Double.parseDouble(left);
		}
		catch (final NumberFormatException e) {
			throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "not an integer: " + left, e);
		}
	}


	public boolean allowsNull() {
		return allowsNull;
	}
}
