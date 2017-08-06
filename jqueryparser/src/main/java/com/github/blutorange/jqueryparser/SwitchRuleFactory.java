package com.github.blutorange.jqueryparser;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class SwitchRuleFactory<@NonNull T, @NonNull C> implements IRuleFactory<@NonNull T, @NonNull C> {
	private final ImmutableMap<String, IRuleFactory<@NonNull T, @NonNull C>> map;
	private final String typeSeparator;

	public SwitchRuleFactory(final String typeSeparator, final ImmutableMap<String, IRuleFactory<@NonNull T, @NonNull C>> map) {
		this.typeSeparator = typeSeparator;
		this.map = map;
	}

	public SwitchRuleFactory(final String typeSeparator, final ImmutableMap.Builder<String, IRuleFactory<@NonNull T, @NonNull C>> map) {
		this(typeSeparator, map.build());
	}

	public SwitchRuleFactory(final String typeSeparator, final Map<String, IRuleFactory<@NonNull T, @NonNull C>> map) {
		this(typeSeparator, ImmutableMap.copyOf(map));
	}

	@Override
	public IRule<@NonNull T, @NonNull C> getFor(final C context, final String id) throws QueryBuilderEvaluatorException {
		final int idx = id.indexOf(typeSeparator);
		IRule<@NonNull T, @NonNull C> rule = null;
		if (idx >= 0) {
			final String type = id.substring(0, idx);
			@Nullable final IRuleFactory<@NonNull T, @NonNull C> factory = map.get(type);
			if (factory != null) {
				final String subId = id.substring(idx+1);
				rule = factory.getFor(context, subId);
			}
		}
		if (rule == null)
			rule = getDefaultRule();
		if (rule == null)
			throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_RULE, id);
		return rule;
	}

	@Nullable
	private IRule<@NonNull T, @NonNull C> getDefaultRule() {
		return null;
	}

	public static class SwitchRuleFactoryBuilder<@NonNull T, @NonNull C> implements IBuilder<IRuleFactory<@NonNull T, @NonNull C>, QueryBuilderEvaluatorException> {
		@Nullable
		private String typeSeparator;

		@Nullable
		private Builder<String, IRuleFactory<@NonNull T, @NonNull C>> map;

		public SwitchRuleFactoryBuilder<@NonNull T, @NonNull C> addFactory(final String prefix, final IRuleFactory<@NonNull T, @NonNull C> ruleFactory) {
			getMap().put(prefix, ruleFactory);
			return this;
		}

		public SwitchRuleFactoryBuilder<@NonNull T, @NonNull C> addFactory(final String prefix, final IBuilder<IRuleFactory<@NonNull T, @NonNull C>, QueryBuilderEvaluatorException> ruleFactoryBuilder) throws QueryBuilderEvaluatorException {
			return addFactory(prefix, ruleFactoryBuilder.build());
		}


		public SwitchRuleFactoryBuilder<@NonNull T, @NonNull C> setSeparator(final String typeSeparator) {
			this.typeSeparator = typeSeparator;
			return this;
		}

		private ImmutableMap.Builder<String, IRuleFactory<@NonNull T, @NonNull C>> getMap() {
			return map != null ? map : (map = new ImmutableMap.Builder<>());
		}

		@Override
		public SwitchRuleFactory<@NonNull T, @NonNull C> build() throws QueryBuilderEvaluatorException {
			String typeSeparator = this.typeSeparator;
			if (typeSeparator == null) typeSeparator = "_"; //$NON-NLS-1$
			final SwitchRuleFactory<@NonNull T, @NonNull C> factory = new SwitchRuleFactory<>(typeSeparator, getMap());
			this.typeSeparator = null;
			map = null;
			return factory;
		}
	}
}