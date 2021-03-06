package com.github.blutorange.jqueryparser.hibernate;

import java.util.Locale;

import org.eclipse.jdt.annotation.NonNull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.github.blutorange.jqueryparser.IOperator;
import com.github.blutorange.jqueryparser.IOperatorNameProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

enum EHibernateStringOperator implements IOperator<Criterion, HibernateContext, String>, IOperatorNameProviding {
	IS_NULL(0, (field, vals) -> Restrictions.isNull(field)),
	IS_NOT_NULL(0, (field, vals) -> Restrictions.isNotNull(field)),
	IS_EMPTY(0, (field, vals) -> Restrictions.isEmpty(field)),
	IS_NOT_EMPTY(0, (field, vals) -> Restrictions.isNotEmpty(field)),
	EQUAL(1, (field, vals) -> Restrictions.eq(field, vals[0])),
	NOT_EQUAL(1, (field, vals) -> Restrictions.ne(field, vals[0])),
	IN(0, Integer.MAX_VALUE, (field, vals) -> Restrictions.in(field, (Object[])vals)),
	NOT_IN(0, Integer.MAX_VALUE, (field, vals) -> Restrictions.not(Restrictions.in(field, (Object[])vals))),
	CONTAINS(1, (field, vals) -> Restrictions.ilike(field, vals[0], MatchMode.ANYWHERE)),
	BEGINS_WITH(1, (field, vals) -> Restrictions.ilike(field, vals[0], MatchMode.START)),
	ENDS_WITH(1, (field, vals) -> Restrictions.ilike(field, vals[0], MatchMode.END)),
	NOT_CONTAINS(1, (field, vals) -> Restrictions.not(Restrictions.ilike(field, vals[0], MatchMode.ANYWHERE))),
	NOT_BEGINS_WITH(1, (field, vals) -> Restrictions.not(Restrictions.ilike(field, vals[0], MatchMode.START))),
	NOT_ENDS_WITH(1, (field, vals) -> Restrictions.not(Restrictions.ilike(field, vals[0], MatchMode.END))),
	;

	private int min;
	private int max;
	private Mapper mapper;

	private EHibernateStringOperator(final int exactly, final Mapper mapper) {
		this(exactly, exactly, mapper);
	}

	private EHibernateStringOperator(final int min, final int max, final Mapper mapper) {
		this.min = min;
		this.max = max;
		this.mapper = mapper;
	}

	@Override
	public Criterion operate(final HibernateContext context, final String field, @NonNull final String [] values)
			throws QueryBuilderEvaluatorException {
		if (values.length < min || values.length > max)
			throw new QueryBuilderEvaluatorException(Codes.ILLEGAL_NUMBER_OF_VALUES, String.valueOf(values.length));
		return mapper.map(field, values);
	}

	@Override
	public String getOperatorName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private interface Mapper {
		Criterion map(String field, String[] values) throws QueryBuilderEvaluatorException;
	}
}
