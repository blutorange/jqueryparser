package com.github.blutorange.jqueryparser.immediate;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.EvaluatorBuilder;

public class ImmediateEvaluatorBuilder<@NonNull C> extends EvaluatorBuilder<@NonNull ImmediateResult, C> {
	public ImmediateEvaluatorBuilder() {
		super();
		setConditionFactory(ImmediateConditionFactoryBuilder.getDefault());
	}
	public static ImmediateEvaluatorBuilder<EVoid> getVoidBuilder() {
		final ImmediateEvaluatorBuilder<EVoid> b = new ImmediateEvaluatorBuilder<>();
		b.setContextSupplier(EVoid.NULL);
		return b;
	}
}