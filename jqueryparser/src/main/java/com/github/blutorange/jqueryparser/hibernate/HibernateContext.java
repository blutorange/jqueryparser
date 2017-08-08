package com.github.blutorange.jqueryparser.hibernate;

import java.text.DateFormat;

import com.github.blutorange.jqueryparser.IRuleContextProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.google.common.collect.ImmutableMap;

public class HibernateContext implements IRuleContextProviding<HibernateContext, String>{

	private final ImmutableMap<String, String> entityAliasMap;
	private final ImmutableMap<String, String> fieldAliasdMap;
	private final String entityFieldSeparator;
	private final DateFormat dateFormat;

	HibernateContext(final String entityNameSeparator, final DateFormat dateFormat, final ImmutableMap.Builder<String, String> entityAliasMap, final ImmutableMap.Builder<String, String> fieldAliasMap) {
		this(entityNameSeparator, dateFormat, entityAliasMap.build(), fieldAliasMap.build());
	}

	public HibernateContext(final String entityNameSeparator, final DateFormat dateFormat, final ImmutableMap<String, String> entityAliasMap, final ImmutableMap<String, String> fieldAliasMap) {
		this.entityAliasMap = entityAliasMap;
		this.fieldAliasdMap = fieldAliasMap;
		this.entityFieldSeparator = entityNameSeparator;
		this.dateFormat = dateFormat;
	}

	@Override
	public String getFor(final HibernateContext context, final String id)
			throws QueryBuilderEvaluatorException {
		final int idx = id.indexOf(entityFieldSeparator);
		if (idx < 0) return id;
		final String entityName = id.substring(0, idx);
		final String fieldName = id.substring(idx+1);
		final String entityAlias = entityAliasMap.getOrDefault(entityName, entityName);
		final String fieldAlias = fieldAliasdMap.getOrDefault(fieldName, fieldName);
		return String.format("%s.%s", entityAlias, fieldAlias); //$NON-NLS-1$
	}

	public DateFormat getDateFormat() {
		return dateFormat;
	}
}