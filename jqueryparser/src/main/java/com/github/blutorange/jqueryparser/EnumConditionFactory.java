package com.github.blutorange.jqueryparser;

import java.util.Locale;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

public class EnumConditionFactory<@NonNull T, @NonNull C, E extends Enum<E> & ICondition<T, C>> implements IConditionFactory<T, C> {

	private final Class<E> clazz;

	public EnumConditionFactory(final Class<E> clazz) {
		this.clazz = clazz;
	}

	@Override
	public ICondition<T, C> getFor(final C context, final String conditionString)
			throws QueryBuilderEvaluatorException {
		try {
			return Enum.valueOf(clazz, conditionString.toUpperCase(Locale.ROOT));
		}
		catch (final IllegalArgumentException e) {
			throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_CONDITION, conditionString, e);
		}
	}
}