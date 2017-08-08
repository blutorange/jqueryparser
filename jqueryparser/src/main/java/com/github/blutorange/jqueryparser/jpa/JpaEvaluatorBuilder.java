package com.github.blutorange.jqueryparser.jpa;

import java.util.function.Consumer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.Constants;
import com.github.blutorange.jqueryparser.EnumConditionFactory;
import com.github.blutorange.jqueryparser.EvaluatorBuilder;
import com.github.blutorange.jqueryparser.OperatorRuleFactoryBuilder;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class JpaEvaluatorBuilder extends EvaluatorBuilder<Predicate, IJpaContext> {
	@Nullable
	private Builder<String, Path<?>> pathMap;

	@Nullable
	private CriteriaBuilder criteriaBuilder;

	@Nullable
	private String pathFieldSeparator;

	public JpaEvaluatorBuilder() {
	}

	public JpaEvaluatorBuilder defaultRuleFactory() throws QueryBuilderEvaluatorException {
		return defaultRuleFactory(null);
	}

	public JpaEvaluatorBuilder defaultRuleFactory(
			@Nullable final Consumer<OperatorRuleFactoryBuilder<Predicate, IJpaRuleContext, IJpaContext>> consumer)
			throws QueryBuilderEvaluatorException {
		setRuleFactory(OperatorRuleFactoryBuilder.create(builder -> {
			builder.addOperators(Constants.TYPE_STRING, EJpaStringOperator.class)
					.addOperators(Constants.TYPE_INTEGER, EJpaIntegerOperator.class)
					.addOperators(Constants.TYPE_DOUBLE, EJpaDoubleOperator.class);
			if (consumer != null)
				consumer.accept(builder);
		}));
		return this;
	}

	public JpaEvaluatorBuilder defaultConditionFactory() {
		setConditionFactory(new EnumConditionFactory<>(EJpaCondition.class));
		return this;
	}

	public JpaEvaluatorBuilder defaults() throws QueryBuilderEvaluatorException {
		defaultConditionFactory();
		defaultRuleFactory();
		pathFieldSeparator = ".";
		return this;
	}

	public JpaEvaluatorBuilder addPath(final String pathName, final Path<?> path) {
		getPathMap().put(pathName, path);
		return this;
	}

	public JpaEvaluatorBuilder setCriteriaBuilder(final CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
		return this;
	}

	public JpaEvaluatorBuilder setPathFieldSeparator(final String pathFieldSeparator) {
		this.pathFieldSeparator = pathFieldSeparator;
		return this;
	}

	public JpaEvaluatorBuilder setDefaultPath(final Path<?> path) {
		return addPath(StringUtils.EMPTY, path);
	}

	private ImmutableMap.Builder<String, Path<?>> getPathMap() {
		return pathMap != null ? pathMap : (pathMap = new ImmutableMap.Builder<>());
	}

	@Override
	protected void beforeBuild() throws QueryBuilderEvaluatorException {
		final String pathFieldSeparator = this.pathFieldSeparator;
		final CriteriaBuilder criteriaBuilder = this.criteriaBuilder;
		if (pathFieldSeparator == null)
			throw new QueryBuilderEvaluatorException(Codes.PRECONDITION, "pathFieldSeparator must not be null");
		if (criteriaBuilder == null)
			throw new QueryBuilderEvaluatorException(Codes.PRECONDITION, "criteriaBuilder must not be null");
		setContextSupplier(new JpaContext(pathFieldSeparator, criteriaBuilder, getPathMap()));
		this.pathMap = null;
		this.criteriaBuilder = null;
		this.pathFieldSeparator = null;
	}
}