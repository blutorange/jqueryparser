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

enum EJpaStringOperator implements IOperator<Predicate, IJpaContext, IJpaRuleContext>, IOperatorNameProviding {
	IS_NULL(0, (cb, field, vals) -> cb.isNull(field)),
	IS_NOT_NULL(0, (cb, field, vals) -> cb.isNotNull(field)),
	IS_EMPTY(0, (cb, field, vals) -> cb.or(cb.isNull(field), cb.equal(field, ""))),
	IS_NOT_EMPTY(0, (cb, field, vals) -> cb.and(cb.isNotNull(field), cb.notEqual(field, ""))),
	EQUAL(1, (cb, field, vals) -> cb.equal(field, vals[0])),
	NOT_EQUAL(1, (cb, field, vals) -> cb.notEqual(field, vals[0])),
	IN(0, Integer.MAX_VALUE, (cb, field, vals) -> field.in((Object[])vals)),
	NOT_IN(0, Integer.MAX_VALUE, (cb, field, vals) -> field.in((Object[])vals).not()),
	CONTAINS(1, (cb, field, vals) -> cb.like(field, vals[0])),
	BEGINS_WITH(1, (cb, field, vals) -> cb.like(field, "%" + escape(vals[0]))),
	ENDS_WITH(1, (cb, field, vals) -> cb.like(field, escape(vals[0]) + "%")),
	NOT_CONTAINS(1, (cb, field, vals) -> cb.notLike(field, vals[0])),
	NOT_BEGINS_WITH(1, (cb, field, vals) -> cb.notLike(field, "%" + escape(vals[0]))),
	NOT_ENDS_WITH(1, (cb, field, vals) -> cb.notLike(field, escape(vals[0]) + "%")),;

	private int min;
	private int max;
	private Mapper mapper;

	private EJpaStringOperator(final int exactly, final Mapper mapper) {
		this(exactly, exactly, mapper);
	}

	private static String escape(final String string) {
		return string.replace("%", "\\%");
	}

	private EJpaStringOperator(final int min, final int max, final Mapper mapper) {
		this.min = min;
		this.max = max;
		this.mapper = mapper;
	}

	@Override
	public Predicate operate(final IJpaContext context, final IJpaRuleContext path, @NonNull final String[] values) throws QueryBuilderEvaluatorException {
		if (values.length < min || values.length > max)
			throw new QueryBuilderEvaluatorException(Codes.ILLEGAL_NUMBER_OF_VALUES, String.valueOf(values.length));
		return mapper.map(path.getCriteriaBuilder(), path.getStringPath(), values);
	}

	@Override
	public String getOperatorName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private interface Mapper {
		Predicate map(CriteriaBuilder criteriaBuilder, Path<@Nullable String> field, @NonNull String[] values)
				throws QueryBuilderEvaluatorException;
	}
}