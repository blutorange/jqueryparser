package com.github.blutorange.jqueryparser.immediate;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;
import com.google.common.collect.ImmutableMap;

public class TypeFactory<@NonNull C> implements ITypeFactory<C> {

	private final ImmutableMap<String, IType<C>> map;

	public TypeFactory(final ImmutableMap<String, IType<C>> map) {
		this.map = map;
	}

	public TypeFactory(final ImmutableMap.Builder<String, IType<C>> map) {
		this(map.build());
	}

	public TypeFactory(final Map<String, IType<C>> map) {
		this(ImmutableMap.copyOf(map));
	}

	@Override
	public IType<C> getFor(final C context, final String typeString) throws QueryBuilderEvaluatorException {
		final IType<C> type = map.get(typeString);
		if (type == null) throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_TYPE, typeString);
		return type;
	}
}