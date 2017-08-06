package com.github.blutorange.jqueryparser;

import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;
import com.google.common.collect.ImmutableMap;

@Immutable
public class ConditionFactory<@NonNull T, @NonNull C> implements IConditionFactory<@NonNull T, @NonNull C> {
	private final ImmutableMap<String, ICondition<@NonNull T, @NonNull C>> map;
	public ConditionFactory(final ImmutableMap<String, ICondition<@NonNull T, @NonNull C>> map) {
		this.map = map;
	}

	public ConditionFactory(final ImmutableMap.Builder<String, ICondition<@NonNull T, @NonNull C>> map) {
		this(map.build());
	}

	public ConditionFactory(final Map<String, ICondition<@NonNull T, @NonNull C>> map) {
		this(ImmutableMap.copyOf(map));
	}

	@Override
	public ICondition<@NonNull T, @NonNull C> getFor(final C c, final String conditionString) throws QueryBuilderEvaluatorException {
		final @Nullable ICondition<@NonNull T, @NonNull C> condition = map.get(conditionString);
		if (condition == null) throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_CONDITION, conditionString);
		return condition;
	}
}