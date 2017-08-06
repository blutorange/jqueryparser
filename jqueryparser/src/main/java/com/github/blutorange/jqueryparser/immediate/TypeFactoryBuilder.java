package com.github.blutorange.jqueryparser.immediate;

import java.util.Locale;
import java.util.Map;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.IBuilder;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

public class TypeFactoryBuilder<@NonNull C> implements IBuilder<ITypeFactory<@NonNull C>, QueryBuilderEvaluatorException> {

	@Nullable
	private Builder<String, IType<@NonNull C>> map;

	public TypeFactoryBuilder<@NonNull C> addType(final String identifier, final IType<@NonNull C> type) {
		getMap().put(identifier, type);
		return this;
	}

	public TypeFactoryBuilder<@NonNull C> addTypes(final Map<String, IType<@NonNull C>> types) {
		types.entrySet().forEach(entry -> addType(entry.getKey(), entry.getValue()));
		return this;
	}

	private ImmutableMap.Builder<String, IType<C>> getMap() {
		return map != null ? map : (map = new ImmutableMap.Builder<>());
	}

	@Override
	public ITypeFactory<@NonNull C> build() throws QueryBuilderEvaluatorException {
		final ITypeFactory<@NonNull C> factory = new TypeFactory<>(getMap());
		map = null;
		return factory;
	}

	public static <@NonNull C> ITypeFactory<@NonNull C> getVoidFactory() throws QueryBuilderEvaluatorException {
		final TypeFactoryBuilder<@NonNull C> b = new TypeFactoryBuilder<>();
		for (final EType type : EType.values())
				b.addType(type.name().toLowerCase(Locale.ROOT), type.getType());
		return b.build();
	}
}
