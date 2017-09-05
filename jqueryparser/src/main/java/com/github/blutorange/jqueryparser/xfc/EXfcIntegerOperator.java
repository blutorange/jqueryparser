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

enum EXfcIntegerOperator implements IOperator<FilterCriterion, XfcContext, String>, IOperatorNameProviding {
  IS_NULL(0, (field, vals) -> new FilterCriterion(field, Operator.IS_NULL)),
  IS_NOT_NULL(0, (field, vals) -> new FilterCriterion(field, Operator.IS_NULL)),
  IS_EMPTY(
      0,
      (field, vals) -> new FilterCriterion(field, Operator.IS_NULL).or(new FilterCriterion(field, StringUtils.EMPTY, Operator.EQUAL))),
  IS_NOT_EMPTY(
      0,
      (field, vals) -> new FilterCriterion(field, Operator.NOT_NULL).andNot(new FilterCriterion(field, StringUtils.EMPTY, Operator.EQUAL))),
  EQUAL(1, (field, vals) -> new FilterCriterion(field, vals[0], Operator.EQUAL)),
  NOT_EQUAL(1, (field, vals) -> new FilterCriterion(field, vals[0], Operator.NOT_EQUAL)),
  IN(0, Integer.MAX_VALUE, (field, vals) -> new FilterCriterion(field, vals, Operator.IN)),
  NOT_IN(0, Integer.MAX_VALUE, (field, vals) -> new FilterCriterion(field, vals, Operator.NOT_IN)),
  LESS(1, (fields, vals) -> new FilterCriterion(fields, vals[0], Operator.LESS_THAN)),
  LESS_OR_EQUAL(1, (fields, vals) -> new FilterCriterion(fields, vals[0], Operator.LESS_OR_EQUAL)),
  GREATER(1, (fields, vals) -> new FilterCriterion(fields, vals[0], Operator.GREATER_THAN)),
  GREATER_OR_EQUAL(1, (fields, vals) -> new FilterCriterion(fields, vals[0], Operator.GREATER_OR_EQUAL)),
  // TODO Does this work???
  BETWEEN(2, (fields, vals) -> new FilterCriterion(fields, vals, Operator.BETWEEN)),
  NOT_BETWEEN(2, (fields, vals) -> new FilterCriterion(fields, vals[0], Operator.LESS_THAN).or(new FilterCriterion(fields, vals[1], Operator.GREATER_THAN))),
  ;
  private int min;
  private int max;
  private Mapper mapper;

  private EXfcIntegerOperator(final int exactly, final Mapper mapper) {
    this(exactly, exactly, mapper);
  }

  private EXfcIntegerOperator(final int min, final int max, final Mapper mapper) {
    this.min = min;
    this.max = max;
    this.mapper = mapper;
  }

  @Override
  public FilterCriterion operate(final XfcContext context, final String field, @NonNull final String[] values)
      throws QueryBuilderEvaluatorException {
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
    return mapper.map(field, intValues);
  }

  @Override
  public String getOperatorName() {
    return name().toLowerCase(Locale.ROOT);
  }

  private interface Mapper {
    FilterCriterion map(String field, Integer[] values) throws QueryBuilderEvaluatorException;
  }
}
