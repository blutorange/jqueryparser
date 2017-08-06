package com.github.blutorange.jqueryparser.hibernate;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.hibernate.criterion.Criterion;

import com.github.blutorange.jqueryparser.Constants;
import com.github.blutorange.jqueryparser.EnumConditionFactory;
import com.github.blutorange.jqueryparser.EvaluatorBuilder;
import com.github.blutorange.jqueryparser.OperatorRuleFactoryBuilder;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class HibernateEvaluatorBuilder extends EvaluatorBuilder<Criterion, HibernateContext> {
	/**
	 * Maps between the ID prefix and the entity alias.
	 */
	@Nullable
	private Builder<String, String> map;

	@Nullable
	private String entityNameSeparator;

	public HibernateEvaluatorBuilder() {
		this("."); //$NON-NLS-1$
	}

	public HibernateEvaluatorBuilder(final String entityNamePrefix) {
		this.entityNameSeparator = entityNamePrefix;
	}

	private ImmutableMap.Builder<String, String> getMap() {
		return map != null ? map : (map = new ImmutableMap.Builder<>());
	}

	public HibernateEvaluatorBuilder defaultRuleFactory() throws QueryBuilderEvaluatorException {
		return defaultRuleFactory(null);
	}

	public HibernateEvaluatorBuilder addAssociationAlias(final String associationName, final String alias) {
		getMap().put(associationName, alias);
		return this;
	}

	public HibernateEvaluatorBuilder defaultRuleFactory(
			@Nullable final Consumer<OperatorRuleFactoryBuilder<Criterion, String, HibernateContext>> consumer)
			throws QueryBuilderEvaluatorException {
		setRuleFactory(OperatorRuleFactoryBuilder.create(builder -> {
			builder.addOperators(Constants.TYPE_STRING, EHibernateOperator.class)
					.addOperators(Constants.TYPE_INTEGER, EHibernateOperator.class)
					.addOperators(Constants.TYPE_DOUBLE, EHibernateOperator.class);
			if (consumer != null)
				consumer.accept(builder);
		}));
		return this;
	}

	public HibernateEvaluatorBuilder defaultConditionFactory() {
		setConditionFactory(new EnumConditionFactory<>(EHibernateCondition.class));
		return this;
	}

	public HibernateEvaluatorBuilder defaults() throws QueryBuilderEvaluatorException {
		defaultConditionFactory();
		defaultRuleFactory();
		return this;
	}

	@Override
	protected void beforeBuild() {
		setContextSupplier(new HibernateContext(entityNameSeparator != null ? entityNameSeparator : ".", getMap())); //$NON-NLS-1$
		map = null;
		entityNameSeparator = null;
	}
}