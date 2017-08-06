package com.github.blutorange.jqueryparser.hibernate;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.hibernate.criterion.Criterion;

import com.github.blutorange.jqueryparser.Constants;
import com.github.blutorange.jqueryparser.EnumConditionFactory;
import com.github.blutorange.jqueryparser.EvaluatorBuilder;
import com.github.blutorange.jqueryparser.OperatorRuleFactoryBuilder;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

public class HibernateEvaluatorBuilder extends EvaluatorBuilder<Criterion, HibernateContext> {
	public HibernateEvaluatorBuilder() {
		setContextSupplier(new HibernateContext());
	}

	public HibernateEvaluatorBuilder defaultRuleFactory() throws QueryBuilderEvaluatorException {
		return defaultRuleFactory(null);
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
}