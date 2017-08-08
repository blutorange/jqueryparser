package com.github.blutorange.jqueryparser.jpa;

import java.util.Locale;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.IOperator;
import com.github.blutorange.jqueryparser.IOperatorNameProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

public enum EJpaIntegerOperator implements IOperator<Predicate, IJpaContext, IJpaRuleContext>, IOperatorNameProviding {
	IS_NULL(0, (cb, field, vals) -> cb.isNull(field)),
	IS_NOT_NULL(0, (cb, field, vals) -> cb.isNotNull(field)),
	IS_EMPTY(0, (cb, field, vals) -> cb.or(cb.isNull(field), cb.equal(field, ""))),
	IS_NOT_EMPTY(0, (cb, field, vals) -> cb.and(cb.isNotNull(field), cb.notEqual(field, ""))),
	EQUAL(1, (cb, field, vals) -> cb.equal(field, vals[0])),
	NOT_EQUAL(1, (cb, field, vals) -> cb.notEqual(field, vals[0])),
	IN(0, Integer.MAX_VALUE, (cb, field, vals) -> field.in((Object[])vals)),
	NOT_IN(0, Integer.MAX_VALUE, (cb, field, vals) -> field.in((Object[])vals).not()),
	LESS(1, (cb, field, vals) -> cb.le(field, vals[0])),
	LESS_THAN(1, (cb, field, vals) -> cb.lt(field, vals[0])),
	GREATER(1, (cb, field, vals) -> cb.ge(field, vals[0])),
	GREATER_THAN(1, (cb, field, vals) -> cb.gt(field, vals[0])),
	BETWEEN(2, (cb, field, vals) -> cb.between(field, vals[0], vals[1])),
	NOT_BETWEEN(2, (cb, field, vals) -> cb.between(field, vals[0], vals[1]).not()),
	;

	private int min;
	private int max;
	private Mapper mapper;

	private EJpaIntegerOperator(final int exactly, final Mapper mapper) {
		this(exactly, exactly, mapper);
	}

	private EJpaIntegerOperator(final int min, final int max, final Mapper mapper) {
		this.min = min;
		this.max = max;
		this.mapper = mapper;
	}

	@Override
	public Predicate operate(final IJpaRuleContext path, @NonNull final String[] values) throws QueryBuilderEvaluatorException {
		if (values.length < min || values.length > max)
			throw new QueryBuilderEvaluatorException(Codes.ILLEGAL_NUMBER_OF_VALUES, String.valueOf(values.length));
		final Integer[] intValues = new Integer[values.length];
		for (int i = values.length; i-- > 0;)
			try {
				intValues[i] = Integer.valueOf(values[i], 10);
			}
			catch (final NumberFormatException e) {
				throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "not a number: " + values[i], e);
			}
		return mapper.map(path.getCriteriaBuilder(), path.getIntegerPath(), intValues);
	}

	@Override
	public String getOperatorName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private interface Mapper {
		Predicate map(CriteriaBuilder criteriaBuilder, Path<? extends @Nullable Integer> field, @NonNull Integer[] values)
				throws QueryBuilderEvaluatorException;
	}
}