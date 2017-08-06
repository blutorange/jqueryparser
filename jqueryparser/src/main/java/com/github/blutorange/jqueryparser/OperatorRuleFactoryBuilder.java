package com.github.blutorange.jqueryparser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

public class OperatorRuleFactoryBuilder<@NonNull T, @NonNull S, @NonNull C extends IRuleContextProviding<C, S>>
		implements IBuilder<IRuleFactory<@NonNull T, @NonNull C>, QueryBuilderEvaluatorException> {

	@Nullable
	private Map<String, Map<String, IOperator<T, C, S>>> map;

	public <Q extends Enum<Q> & IOperator<T, C, S> & ITypeNameProviding & IOperatorNameProviding> OperatorRuleFactoryBuilder<T, S, C> addOperators(
			final Class<Q> types) {
		for (final Q enumConstant : types.getEnumConstants())
			getSubMap(enumConstant.getTypeName()).put(enumConstant.getOperatorName(), enumConstant);
		return this;
	}

	public <Q extends Enum<Q> & IOperator<T, C, S> & ITypeNameProviding & IOperatorNameProviding> OperatorRuleFactoryBuilder<T, S, C> addOperators(
			@SuppressWarnings("unchecked") final Class<Q>... types) {
		for (final Class<Q> classQ : types)
			addOperators(classQ);
		return this;
	}

	public <Q extends Enum<Q> & IOperator<T, C, S> & IOperatorNameProviding> OperatorRuleFactoryBuilder<T, S, C> addOperators(
			final String type, final Class<Q> types) {
		final Map<String, IOperator<T, C, S>> subMap = getSubMap(type);
		for (final Q enumConstant : types.getEnumConstants())
			subMap.put(enumConstant.getOperatorName(), enumConstant);
		return this;
	}

	public OperatorRuleFactoryBuilder<T, S, C> addOperator(final String typeName, final String operatorName,
			final IOperator<T, C, S> operator) {
		getSubMap(typeName).put(operatorName, operator);
		return this;
	}

	public OperatorRuleFactoryBuilder<T, S, C> addOperators(final String typeName,
			final Map<String, IOperator<T, C, S>> operators) {
		getSubMap(typeName).putAll(operators);
		return this;
	}

	public <Q extends IOperator<T, C, S> & IOperatorNameProviding> OperatorRuleFactoryBuilder<T, S, C> addOperators(
			final String typeName, final Iterable<Q> operators) {
		final Map<String, IOperator<T, C, S>> subMap = getSubMap(typeName);
		for (final Q q : operators)
			subMap.put(q.getOperatorName(), q);
		return this;
	}

	public <Q extends IOperator<T, C, S> & IOperatorNameProviding> OperatorRuleFactoryBuilder<T, S, C> addOperators(
			final String typeName, @SuppressWarnings("unchecked") final Q... operators) {
		final Map<String, IOperator<T, C, S>> subMap = getSubMap(typeName);
		for (final Q q : operators)
			subMap.put(q.getOperatorName(), q);
		return this;
	}

	public <Q extends IOperator<T, C, S> & ITypeNameProviding & IOperatorNameProviding> OperatorRuleFactoryBuilder<T, S, C> addOperators(
			@SuppressWarnings("unchecked") final Q... operators) {
		for (final Q q : operators)
			getSubMap(q.getTypeName()).put(q.getOperatorName(), q);
		return this;
	}

	public <Q extends IOperator<T, C, S> & ITypeNameProviding & IOperatorNameProviding> OperatorRuleFactoryBuilder<T, S, C> addOperators(
			final Iterable<Q> operators) {
		for (final Q q : operators)
			getSubMap(q.getTypeName()).put(q.getOperatorName(), q);
		return this;
	}

	@Override
	public IRuleFactory<T, C> build() {
		final IRuleFactory<T, C> factory = new OperatorRuleFactory<>(getMap());
		map = null;
		return factory;
	}

	private Map<String, IOperator<T, C, S>> getSubMap(final String type) {
		final Map<String, Map<String, IOperator<T, C, S>>> map = getMap();
		Map<String, IOperator<T, C, S>> subMap = map.get(type);
		if (subMap == null)
			map.put(type, subMap = new HashMap<>());
		return subMap;
	}

	private Map<String, Map<String, IOperator<T, C, S>>> getMap() {
		return map != null ? map : (map = new HashMap<>());
	}

	public static <@NonNull T, @NonNull S, @NonNull C extends IRuleContextProviding<C, S>> IBuilder<IRuleFactory<@NonNull T, @NonNull C>, QueryBuilderEvaluatorException> create(
			final Consumer<OperatorRuleFactoryBuilder<T, S, C>> consumer) {
		final OperatorRuleFactoryBuilder<T, S, C> factory = new OperatorRuleFactoryBuilder<>();
		consumer.accept(factory);
		return factory;
	}
}
