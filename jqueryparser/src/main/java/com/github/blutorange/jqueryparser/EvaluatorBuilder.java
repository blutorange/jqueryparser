package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

public class EvaluatorBuilder<@NonNull T, @NonNull C>
		implements IBuilder<IQueryBuilderEvaluator<@NonNull T, @NonNull C>, QueryBuilderEvaluatorException> {

	@Nullable
	private IConditionFactory<@NonNull T, @NonNull C> conditionFactory;

	@Nullable
	private IRuleFactory<@NonNull T, @NonNull C> ruleFactory;

	@Nullable
	private NonNullSupplier<@NonNull C> contextSupplier;

	public EvaluatorBuilder() {

	}

	public final EvaluatorBuilder<@NonNull T, @NonNull C> setConditionFactory(
			final IConditionFactory<@NonNull T, @NonNull C> conditionFactory) {
		this.conditionFactory = conditionFactory;
		return this;
	}

	public final EvaluatorBuilder<@NonNull T, @NonNull C> setConditionFactory(
			final IBuilder<IConditionFactory<@NonNull T, @NonNull C>, QueryBuilderEvaluatorException> conditionFactoryBuilder)
			throws QueryBuilderEvaluatorException {
		return setConditionFactory(conditionFactoryBuilder.build());
	}

	public final EvaluatorBuilder<@NonNull T, @NonNull C> setRuleFactory(
			final IRuleFactory<@NonNull T, @NonNull C> ruleFactory) {
		this.ruleFactory = ruleFactory;
		return this;
	}

	public final EvaluatorBuilder<@NonNull T, @NonNull C> setRuleFactory(
			final IBuilder<IRuleFactory<@NonNull T, @NonNull C>, QueryBuilderEvaluatorException> ruleFactoryBuilder)
			throws QueryBuilderEvaluatorException {
		return setRuleFactory(ruleFactoryBuilder.build());
	}

	public final EvaluatorBuilder<@NonNull T, @NonNull C> setContextSupplier(final C context) {
		return setContextSupplier(() -> context);
	}

	public final EvaluatorBuilder<@NonNull T, @NonNull C> setContextSupplier(
			final NonNullSupplier<@NonNull C> contextSupplier) {
		this.contextSupplier = contextSupplier;
		return this;
	}

	@Override
	public IQueryBuilderEvaluator<@NonNull T, @NonNull C> build() throws QueryBuilderEvaluatorException {
		beforeBuild();
		final IRuleFactory<@NonNull T, @NonNull C> ruleFactory = this.ruleFactory;
		final IConditionFactory<@NonNull T, @NonNull C> conditionFactory = this.conditionFactory;
		final NonNullSupplier<@NonNull C> contextSupplier = this.contextSupplier;
		if (ruleFactory == null)
			throw new QueryBuilderEvaluatorException(Codes.PRECONDITION, "rule factory is null"); //$NON-NLS-1$
		if (conditionFactory == null)
			throw new QueryBuilderEvaluatorException(Codes.PRECONDITION, "condition factory is null"); //$NON-NLS-1$
		if (contextSupplier == null)
			throw new QueryBuilderEvaluatorException(Codes.PRECONDITION, "context supplier is null"); //$NON-NLS-1$
		final QueryBuilderEvaluator<@NonNull T, @NonNull C> queryBuilderEvaluator = new QueryBuilderEvaluator<>(conditionFactory,
				ruleFactory, contextSupplier);
		this.conditionFactory = null;
		this.ruleFactory = null;
		this.contextSupplier = null;
		afterBuild(queryBuilderEvaluator);
		return queryBuilderEvaluator;
	}

	/**
	 * @param queryBuilderEvaluator
	 */
	protected void afterBuild(final QueryBuilderEvaluator<T, C> queryBuilderEvaluator) {
		// May be overridden.
	}

	protected void beforeBuild() {
		// May be overridden.
	}
}
