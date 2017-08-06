package com.github.blutorange.jqueryparser;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

public class OperatorRuleFactory<@NonNull T, @NonNull S, @NonNull C extends IRuleContextProviding<C, S>> implements IRuleFactory<T, C> {
	protected final Map<String, Map<String, IOperator<T,C,S>>> map;

	OperatorRuleFactory(final Map<String, Map<String, IOperator<T,C,S>>> map) {
		this.map = map;
	}

	@Override
	public IRule<T, C> getFor(final C context, final String id)
			throws QueryBuilderEvaluatorException {
		try {
			return new EnumRule(context.getFor(context, id));
		}
		catch (final IllegalArgumentException e) {
			throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_RULE, id, e);
		}
	}

	protected class EnumRule implements IRule<T, C> {
		private final S s;

		public EnumRule(final S s) {
			this.s = s;
		}

		@Override
		public T map(final C context, final String type, final String operatorString, @Nullable final String input,
				@NonNull final String... values) throws QueryBuilderEvaluatorException {
			@Nullable
			final Map<String, IOperator<T,C,S>> operators = map.get(type);
			if (operators == null)
				throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_TYPE, type);
			final IOperator<T, C, S> operator = operators.get(operatorString);
			if (operator == null)
				throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_OPERATOR, operatorString);
			return operator.operate(s, values);
		}
	}
}
