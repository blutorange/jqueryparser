package com.github.blutorange.jqueryparser.immediate;

import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

public enum EStringOperator {
	EQUAL(true, 1, (l, r) -> l.equals(r[0])),
	NOT_EQUAL(true, 1, (l, r) -> !l.equals(r[0])),
	BEGINS_WITH(false, 1, (l, r) -> l.startsWith(r[0])),
	NOT_BEGINS_WITH(false, 1, (l, r) -> !l.startsWith(r[0])),
	ENDS_WITH(false, 1, (l, r) -> l.endsWith(r[0])),
	NOT_ENDS_WITH(false, 1, (l, r) -> !l.endsWith(r[0])),
	IS_EMPTY(true, 0, (l, r) -> l == null || l.isEmpty()),
	IS_NOT_EMPTY(true, 0, (l, r) -> l != null && !l.isEmpty()),
	IS_NULL(true, 0, Integer.MAX_VALUE, (l, r) -> l == null),
	IS_NOT_NULL(true, 0, Integer.MAX_VALUE, (l, r) -> l != null),
	CONTAINS(false, 1, (l, r) -> l.indexOf(r[0]) >= 0),
	IN(true, 0, Integer.MAX_VALUE, (l, r) -> ArrayUtils.contains(r, l)),
	NOT_IN(true, 0, Integer.MAX_VALUE, (l, r) -> !ArrayUtils.contains(r, l)),
	;

	private int max;
	private int min;
	private boolean allowsNull;
	private BiStringPredicate function;

	private EStringOperator(final boolean allowsNull, final int exactly, final BiStringPredicate function) {
		this(allowsNull, exactly, exactly, function);
	}

	private EStringOperator(final boolean allowsNull, final int min, final int max, final BiStringPredicate function) {
		this.min = min;
		this.max = max;
		this.allowsNull = allowsNull;
		this.function = function;
	}

	public boolean allowsCount(final int length) {
		return length >= min && length <= max;
	}

	public boolean operate(final Optional<String> left, @NonNull final String [] right) throws QueryBuilderEvaluatorException {
		if (!allowsNull() && !left.isPresent())
			return false;
		return function.test(left.orElse(null), right);
	}

	public boolean allowsNull() {
		return allowsNull;
	}

	protected static interface BiStringPredicate {
		boolean test(String left, @NonNull String[] right);
	}

}
