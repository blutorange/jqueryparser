package com.github.blutorange.jqueryparser.hibernate;

import com.github.blutorange.jqueryparser.IRuleContextProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.google.common.collect.ImmutableMap;

public class HibernateContext implements IRuleContextProviding<HibernateContext, String>{

	private final ImmutableMap<String, String> map;
	private final String entityNameSeparator;

	HibernateContext(final String entityNameSeparator, final ImmutableMap.Builder<String, String> map) {
		this(entityNameSeparator, map.build());
	}

	public HibernateContext(final String entityNameSeparator, final ImmutableMap<String, String> map) {
		this.map = map;
		this.entityNameSeparator = entityNameSeparator;
	}

	@Override
	public String getFor(final HibernateContext context, final String id)
			throws QueryBuilderEvaluatorException {
		final int idx = id.indexOf(entityNameSeparator);
		if (idx < 0) return id;
		final String entityName = id.substring(0, idx);
		final String fieldName = id.substring(idx+1);
		final String entityAlias = map.get(entityName);
		return String.format("%s.%s", entityAlias != null ? entityAlias : entityName, fieldName); //$NON-NLS-1$
	}
}