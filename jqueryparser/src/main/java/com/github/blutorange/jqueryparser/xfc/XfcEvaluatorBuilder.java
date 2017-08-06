package com.github.blutorange.jqueryparser.xfc;

import java.util.function.Consumer;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.Constants;
import com.github.blutorange.jqueryparser.EnumConditionFactory;
import com.github.blutorange.jqueryparser.EvaluatorBuilder;
import com.github.blutorange.jqueryparser.OperatorRuleFactoryBuilder;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

import de.xima.cmn.criteria.FilterCriterion;

public class XfcEvaluatorBuilder extends EvaluatorBuilder<@NonNull FilterCriterion, @NonNull XfcContext> {
	public XfcEvaluatorBuilder() {
	}

	public XfcEvaluatorBuilder defaultConditionFactory() {
		setConditionFactory(new EnumConditionFactory<>(EXfcCondition.class));
		return this;
	}

	public XfcEvaluatorBuilder defaultRuleFactory() throws QueryBuilderEvaluatorException {
		return defaultRuleFactory(null);
	}

	public XfcEvaluatorBuilder defaultRuleFactory(
			@Nullable final Consumer<OperatorRuleFactoryBuilder<FilterCriterion, String, XfcContext>> consumer)
			throws QueryBuilderEvaluatorException {
		setRuleFactory(OperatorRuleFactoryBuilder.create(builder -> {
			builder.addOperators(Constants.TYPE_STRING, EXfcStringOperator.class)
					.addOperators(Constants.TYPE_INTEGER, EXfcStringOperator.class)
					.addOperators(Constants.TYPE_DOUBLE, EXfcStringOperator.class);
			if (consumer != null)
				consumer.accept(builder);
		}));
		return this;
	}

	public XfcEvaluatorBuilder defaults() throws QueryBuilderEvaluatorException {
		defaultConditionFactory();
		defaultRuleFactory();
		return this;
	}
}