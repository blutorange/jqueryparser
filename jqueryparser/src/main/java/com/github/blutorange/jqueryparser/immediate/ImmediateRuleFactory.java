package com.github.blutorange.jqueryparser.immediate;

import java.util.Map;
import java.util.function.BiFunction;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.IRule;
import com.github.blutorange.jqueryparser.RuleFactory;
import com.google.common.collect.ImmutableMap;

public class ImmediateRuleFactory<@NonNull C> extends RuleFactory<ImmediateResult, C> {
	final BiFunction<C, String, @Nullable IRule<@NonNull ImmediateResult, @NonNull C>> defaultRuleFunction;

	public ImmediateRuleFactory(final ImmutableMap<String, IRule<@NonNull ImmediateResult, @NonNull C>> map,
			final BiFunction<C, String, @Nullable IRule<@NonNull ImmediateResult, @NonNull C>> defaultValueSupplier) {
		super(map);
		this.defaultRuleFunction = defaultValueSupplier;
	}

	public ImmediateRuleFactory(final ImmutableMap.Builder<String, IRule<@NonNull ImmediateResult, @NonNull C>> map,
			final BiFunction<C, String, @Nullable IRule<@NonNull ImmediateResult, @NonNull C>> defaultValueSupplier) {
		super(map);
		this.defaultRuleFunction = defaultValueSupplier;
	}

	public ImmediateRuleFactory(final Map<String, IRule<@NonNull ImmediateResult, @NonNull C>> map,
			final BiFunction<C, String, @Nullable IRule<@NonNull ImmediateResult, @NonNull C>> defaultValueSupplier) {
		super(map);
		this.defaultRuleFunction = defaultValueSupplier;
	}


	@Override
	protected @Nullable IRule<@NonNull ImmediateResult, @NonNull C> getDefaultRule(final C context, final String id) {
		return defaultRuleFunction.apply(context, id);
	}
}