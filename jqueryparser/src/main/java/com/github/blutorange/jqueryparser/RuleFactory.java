package com.github.blutorange.jqueryparser;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;
import com.google.common.collect.ImmutableMap;

public class RuleFactory<T,C> implements IRuleFactory<@NonNull T, @NonNull C> {

	private final ImmutableMap<String, IRule<@NonNull T, @NonNull C>> map;

	public RuleFactory(final ImmutableMap<String, IRule<@NonNull T, @NonNull C>> map) {
		this.map = map;
	}

	public RuleFactory(final ImmutableMap.Builder<String, IRule<@NonNull T, @NonNull C>> map) {
		this(map.build());
	}

	public RuleFactory(final Map<String, IRule<@NonNull T, @NonNull C>> map) {
		this(ImmutableMap.copyOf(map));
	}

	@Override
	public final @NonNull IRule<@NonNull T, @NonNull C> getFor(final C context, @NonNull final String id) throws QueryBuilderEvaluatorException {
		@Nullable IRule<@NonNull T, @NonNull C> rule = map.get(id);
		if (rule == null)
			rule = getDefaultRule(context, id);
		if (rule == null)
			throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_RULE, id);
		return rule;
	}

	/**
	 * @param context
	 * @param id
	 */
	protected @Nullable IRule<@NonNull T, @NonNull C> getDefaultRule(final C context, final String id) {
		return null;
	}
}