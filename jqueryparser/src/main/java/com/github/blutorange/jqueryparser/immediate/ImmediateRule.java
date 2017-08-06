package com.github.blutorange.jqueryparser.immediate;

import java.util.Optional;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.IRule;
import com.github.blutorange.jqueryparser.NonNullFunction;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

public class ImmediateRule<@NonNull C> implements IRule<@NonNull ImmediateResult, @NonNull C> {

	private final NonNullFunction<C, @NonNull Optional<String>> valueSupplier;
	private final ITypeFactory<@NonNull C> typeFactory;

	public ImmediateRule(final ITypeFactory<@NonNull C> typeFactory,
			final NonNullFunction<@NonNull C, Optional<String>> valueSupplier) {
		this.valueSupplier = valueSupplier;
		this.typeFactory = typeFactory;
	}

	@Override
	public ImmediateResult map(final C context, final String typeString, final String operatorString,
			@Nullable final String input, @NonNull final String... value) throws QueryBuilderEvaluatorException {
		final IType<@NonNull C> type = typeFactory.getFor(context, typeString);
		final Optional<String> left = valueSupplier.apply(context);
		final boolean result = type.operate(context, operatorString, left, value);
		return ImmediateResult.valueOf(result);
	}
}