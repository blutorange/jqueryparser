package com.github.blutorange.jqueryparser.xfc;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.IOperator;
import com.github.blutorange.jqueryparser.IOperatorNameProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

import de.xima.cmn.criteria.FilterCriterion;
import de.xima.cmn.criteria.FilterCriterion.Operator;
import de.xima.cmn.criteria.FilterCriterion.StringOperator;

public enum EXfcStringOperator implements IOperator<FilterCriterion, XfcContext, String>, IOperatorNameProviding {
	IS_NULL(0, (field, vals) -> new FilterCriterion(field, Operator.IS_NULL)),
	IS_NOT_NULL(0, (field, vals) -> new FilterCriterion(field, Operator.IS_NULL)),
	IS_EMPTY(
			0,
			(field, vals) -> new FilterCriterion(field, Operator.IS_NULL)
					.or(new FilterCriterion(field, StringUtils.EMPTY, Operator.EQUAL))),
	IS_NOT_EMPTY(
			0,
			(field, vals) -> new FilterCriterion(field, Operator.NOT_NULL)
					.andNot(new FilterCriterion(field, StringUtils.EMPTY, Operator.EQUAL))),
	EQUAL(1, (field, vals) -> new FilterCriterion(field, vals[0], Operator.EQUAL)),
	NOT_EQUAL(1, (field, vals) -> new FilterCriterion(field, vals[0], Operator.NOT_EQUAL)),
	IN(0, Integer.MAX_VALUE, (field, vals) -> new FilterCriterion(field, vals, Operator.IN)),
	NOT_IN(0, Integer.MAX_VALUE, (field, vals) -> new FilterCriterion(field, vals, Operator.NOT_IN)),
	CONTAINS(1, (field, vals) -> new FilterCriterion(field, vals[0], Operator.LIKE, StringOperator.EQUAL_LIKE_BOTH)),
	BEGINS_WITH(
			1,
			(field, vals) -> new FilterCriterion(field, vals[0], Operator.LIKE, StringOperator.EQUAL_LIKE_BEFORE)),
	ENDS_WITH(1, (field, vals) -> new FilterCriterion(field, vals[0], Operator.LIKE, StringOperator.EQUAL_LIKE_AFTER)),
	NOT_CONTAINS(
			1,
			(field, vals) -> new FilterCriterion(field, vals[0], Operator.NOT_LIKE, StringOperator.EQUAL_LIKE_BOTH)),
	NOT_BEGINS_WITH(
			1,
			(field, vals) -> new FilterCriterion(field, vals[0], Operator.NOT_LIKE, StringOperator.EQUAL_LIKE_BEFORE)),
	NOT_ENDS_WITH(
			1,
			(field, vals) -> new FilterCriterion(field, vals[0], Operator.NOT_LIKE, StringOperator.EQUAL_LIKE_AFTER)),;

	private int min;
	private int max;
	private Mapper mapper;

	private EXfcStringOperator(final int exactly, final Mapper mapper) {
		this(exactly, exactly, mapper);
	}

	private EXfcStringOperator(final int min, final int max, final Mapper mapper) {
		this.min = min;
		this.max = max;
		this.mapper = mapper;
	}

	@Override
	public FilterCriterion operate(final String field, @NonNull final String[] values)
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
		FilterCriterion map(String field, String[] values) throws QueryBuilderEvaluatorException;
	}
}
