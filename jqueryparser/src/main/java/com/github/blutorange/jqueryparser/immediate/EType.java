package com.github.blutorange.jqueryparser.immediate;

import java.util.Locale;
import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

public enum EType {
	STRING {
		@Override
		public boolean operate(final String operatorString, final Optional<String> leftString, @NonNull final String[] right)
				throws QueryBuilderEvaluatorException {
			final EStringOperator operator;
			try {
				operator = EStringOperator.valueOf(operatorString.toUpperCase(Locale.ROOT));
			}
			catch (final IllegalArgumentException e) {
				throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_OPERATOR, operatorString, e);
			}
			if (!operator.allowsCount(right.length))
				throw new QueryBuilderEvaluatorException(Codes.ILLEGAL_NUMBER_OF_VALUES, String.valueOf(right.length));
			return operator.operate(leftString, right);
		}
	},
	INTEGER {
		@Override
		public boolean operate(final String operatorString, final Optional<String> leftString, @NonNull final String[] rightStrings)
				throws QueryBuilderEvaluatorException {
			final EIntegerOperator operator;
			try {
				operator = EIntegerOperator.valueOf(operatorString.toUpperCase(Locale.ROOT));
			}
			catch (final IllegalArgumentException e) {
				throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_OPERATOR, operatorString, e);
			}
			if (!operator.allowsCount(rightStrings.length))
				throw new QueryBuilderEvaluatorException(Codes.ILLEGAL_NUMBER_OF_VALUES, String.valueOf(rightStrings.length));
			final Optional<Integer> left = EIntegerOperator.convert(leftString);
			final int[] right = new int[rightStrings.length];
			for (int i = rightStrings.length; i-->0;)
				right[i] = EIntegerOperator.convert(rightStrings[i]);
			return operator.operate(left, right);
		}
	},
	DOUBLE {
		@Override
		public boolean operate(final String operatorString, final Optional<String> leftString, @NonNull final String[] rightStrings)
				throws QueryBuilderEvaluatorException {
			final EDoubleOperator operator;
			try {
				operator = EDoubleOperator.valueOf(operatorString.toUpperCase(Locale.ROOT));
			}
			catch (final IllegalArgumentException e) {
				throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_OPERATOR, operatorString, e);
			}
			if (!operator.allowsCount(rightStrings.length))
				throw new QueryBuilderEvaluatorException(Codes.ILLEGAL_NUMBER_OF_VALUES, String.valueOf(rightStrings.length));
			final Optional<Double> left = EDoubleOperator.convert(leftString);
			final double[] right = new double[rightStrings.length];
			for (int i = rightStrings.length; i-->0;)
				right[i] = EDoubleOperator.convert(rightStrings[i]);
			return operator.operate(left, right);
		}
	}
	;

	public abstract boolean operate(final String operatorString, final Optional<String> left, @NonNull final String[] right)
			throws QueryBuilderEvaluatorException;

	public <C> IType<@NonNull C> getType() {
		return (context, operator, left ,right) -> operate(operator, left, right);
	}
}
