package com.github.blutorange.jqueryparser.hibernate;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.eclipse.jdt.annotation.NonNull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.github.blutorange.jqueryparser.IOperator;
import com.github.blutorange.jqueryparser.IOperatorNameProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

enum EHibernateDateOperator implements IOperator<Criterion, HibernateContext, String>, IOperatorNameProviding {
	IS_NULL(0, (field, vals) -> Restrictions.isNull(field)),
	IS_NOT_NULL(0, (field, vals) -> Restrictions.isNotNull(field)),
	IS_EMPTY(0, (field, vals) -> Restrictions.isEmpty(field)),
	IS_NOT_EMPTY(0, (field, vals) -> Restrictions.isNotEmpty(field)),
	EQUAL(1, (field, vals) -> Restrictions.eq(field, vals[0])),
	NOT_EQUAL(1, (field, vals) -> Restrictions.ne(field, vals[0])),
	IN(0, Integer.MAX_VALUE, (field, vals) -> Restrictions.in(field, (Object[])vals)),
	NOT_IN(0, Integer.MAX_VALUE, (field, vals) -> Restrictions.not(Restrictions.in(field, (Object[])vals))),
	LESS(1, (field, vals) -> Restrictions.le(field, vals[0]))
	;

	private int min;
	private int max;
	private Mapper mapper;

	private EHibernateDateOperator(final int exactly, final Mapper mapper) {
		this(exactly, exactly, mapper);
	}

	private EHibernateDateOperator(final int min, final int max, final Mapper mapper) {
		this.min = min;
		this.max = max;
		this.mapper = mapper;
	}

	@Override
	public Criterion operate(final HibernateContext context, final String field, @NonNull final String [] values)
			throws QueryBuilderEvaluatorException {
		if (values.length < min || values.length > max)
			throw new QueryBuilderEvaluatorException(Codes.ILLEGAL_NUMBER_OF_VALUES, String.valueOf(values.length));
		final Date[] dateVals = new Date[values.length];
		for (int i = values.length; i-->0;)
			try {
				dateVals[i] = context.getDateFormat().parse(values[i]);
			}
			catch (final ParseException e) {
				throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "not a date " + values[i], e); //$NON-NLS-1$
			}
		return mapper.map(field, dateVals);
	}

	@Override
	public String getOperatorName() {
		return name().toLowerCase(Locale.ROOT);
	}

	private interface Mapper {
		Criterion map(String field, Date[] values) throws QueryBuilderEvaluatorException;
	}
}
