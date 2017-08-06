package com.github.blutorange.jqueryparser.immediate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.IBuilder;
import com.github.blutorange.jqueryparser.IRule;
import com.github.blutorange.jqueryparser.IRuleFactory;
import com.github.blutorange.jqueryparser.NonNullBiFunction;
import com.github.blutorange.jqueryparser.NonNullFunction;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

public class ImmediateRuleFactoryBuilder<@NonNull C>
		implements IBuilder<IRuleFactory<ImmediateResult, C>, QueryBuilderEvaluatorException> {

	@Nullable
	private Map<String, NonNullFunction<@NonNull C, Optional<String>>> map;

	@Nullable
	private ITypeFactory<@NonNull C> typeFactory;

	@Nullable
	private NonNullBiFunction<C, String, Optional<String>> defaultValueSupplier;

	public ImmediateRuleFactoryBuilder<C> addRule(final String id,
			final NonNullFunction<C, Optional<String>> valueSupplier) {
		getMap().put(id, valueSupplier);
		return this;
	}

	public ImmediateRuleFactoryBuilder<C> addRule(final Map<String, NonNullFunction<C, Optional<String>>> map) {
		getMap().putAll(map);
		return this;
	}

	public ImmediateRuleFactoryBuilder<C> addRule(final String id, final Optional<String> string) {
		return addRule(id, c -> string);
	}

	public ImmediateRuleFactoryBuilder<C> addRule(final String id, @Nullable final String string) {
		return addRule(id, c -> Optional.ofNullable(string));
	}

	public ImmediateRuleFactoryBuilder<C> setDefaultRule(
			final NonNullBiFunction<C, String, Optional<String>> defaultValueSupplier) {
		this.defaultValueSupplier = defaultValueSupplier;
		return this;
	}

	public ImmediateRuleFactoryBuilder<C> addRules(final Map<String, String> rules) {
		for (final Entry<String, String> entry : rules.entrySet())
			addRule(entry.getKey(), entry.getValue());
		return this;
	}

	public ImmediateRuleFactoryBuilder<C> setTypeFactory(final ITypeFactory<@NonNull C> typeFactory) {
		this.typeFactory = typeFactory;
		return this;
	}

	@Override
	public IRuleFactory<@NonNull ImmediateResult, C> build() throws QueryBuilderEvaluatorException {
		final ITypeFactory<@NonNull C> typeFactory = this.typeFactory != null ? this.typeFactory
				: TypeFactoryBuilder.getVoidFactory();
		final Map<@NonNull String, @NonNull IRule<@NonNull ImmediateResult, C>> ruleMap = getMap().entrySet().stream()
				.collect(Collectors.toMap(Entry::getKey, e -> new ImmediateRule<>(typeFactory, e.getValue())));
		final NonNullBiFunction<C, String, Optional<String>> defaultValueSupplier = this.defaultValueSupplier;
		final BiFunction<C, String, @Nullable IRule<ImmediateResult, C>> defaultRuleFunction = (context, id) -> {
			if (defaultValueSupplier == null)
				return null;
			return new ImmediateRule<>(typeFactory, c -> defaultValueSupplier.apply(c, id));
		};
		final IRuleFactory<@NonNull ImmediateResult, C> factory = new ImmediateRuleFactory<>(ruleMap, defaultRuleFunction);
		this.defaultValueSupplier = null;
		this.map = null;
		this.typeFactory = null;
		return factory;
	}

	private Map<String, NonNullFunction<C, Optional<String>>> getMap() {
		return map != null ? map : (map = new HashMap<>());
	}
}
