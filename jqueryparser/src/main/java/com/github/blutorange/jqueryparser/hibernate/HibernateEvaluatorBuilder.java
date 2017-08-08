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
	private Builder<String, String> entityAliasMap;

	@Nullable
	private Builder<String, String> fieldAliasMap;

	@Nullable
	private String entityFieldSeparator;

	public HibernateEvaluatorBuilder() {
		this("."); //$NON-NLS-1$
	}

	public HibernateEvaluatorBuilder(final String entityFieldPrefix) {
		this.entityFieldSeparator = entityFieldPrefix;
	}

	private ImmutableMap.Builder<String, String> getEntityMap() {
		return entityAliasMap != null ? entityAliasMap : (entityAliasMap = new ImmutableMap.Builder<>());
	}

	private ImmutableMap.Builder<String, String> getFieldMap() {
		return fieldAliasMap != null ? fieldAliasMap : (fieldAliasMap = new ImmutableMap.Builder<>());
	}

	public HibernateEvaluatorBuilder addEntityAlias(final String entityName, final String alias) {
		getEntityMap().put(entityName, alias);
		return this;
	}

	public HibernateEvaluatorBuilder addFieldAlias(final String fieldName, final String fieldAlias) {
		getFieldMap().put(fieldName, fieldAlias);
		return this;
	}

	public HibernateEvaluatorBuilder defaultRuleFactory() throws QueryBuilderEvaluatorException {
		return defaultRuleFactory(null);
	}

	public HibernateEvaluatorBuilder defaultRuleFactory(
			@Nullable final Consumer<OperatorRuleFactoryBuilder<Criterion, String, HibernateContext>> consumer)
			throws QueryBuilderEvaluatorException {
		setRuleFactory(OperatorRuleFactoryBuilder.create(builder -> {
			builder.addOperators(Constants.TYPE_STRING, EHibernateStringOperator.class)
					.addOperators(Constants.TYPE_INTEGER, EHibernateIntegerOperator.class)
					.addOperators(Constants.TYPE_DOUBLE, EHibernateDoubleOperator.class);
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
		setContextSupplier(new HibernateContext(entityFieldSeparator != null ? entityFieldSeparator : ".", getEntityMap(), getFieldMap())); //$NON-NLS-1$
		entityAliasMap = null;
		entityFieldSeparator = null;
	}
}