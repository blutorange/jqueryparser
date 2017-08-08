package com.github.blutorange.jqueryparser.jpa;

import java.util.function.Consumer;

import javax.persistence.criteria.Predicate;

import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.Constants;
import com.github.blutorange.jqueryparser.EnumConditionFactory;
import com.github.blutorange.jqueryparser.EvaluatorBuilder;
import com.github.blutorange.jqueryparser.OperatorRuleFactoryBuilder;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

public class JpaEvaluatorBuilder extends EvaluatorBuilder<Predicate, IJpaContext> {
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
		return this;
	}
}