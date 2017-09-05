package com.github.blutorange.jqueryparser.jpa;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;
import com.google.common.collect.ImmutableMap;

class JpaContext implements IJpaContext {
	private final CriteriaBuilder criteriaBuilder;
	private final ImmutableMap<String, Path<?>> pathMap;
	private final String pathFieldSeparator;

	JpaContext(final String pathFieldSeparator, final CriteriaBuilder criteriaBuilder, final ImmutableMap<String, Path<?>> pathMap) {
		this.criteriaBuilder = criteriaBuilder;
		this.pathMap = pathMap;
		this.pathFieldSeparator = pathFieldSeparator;
	}

	JpaContext(final String pathFieldSeparator, final CriteriaBuilder criteriaBuilder, final ImmutableMap.Builder<String, Path<?>> pathMap) {
		this(pathFieldSeparator, criteriaBuilder, pathMap.build());
	}

	JpaContext(final String pathFieldSeparator, final CriteriaBuilder criteriaBuilder, final Map<String, Path<?>> pathMap) {
		this(pathFieldSeparator, criteriaBuilder, ImmutableMap.copyOf(pathMap));
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}

	@Override
	public IJpaRuleContext getFor(final IJpaContext context, final String id)
			throws QueryBuilderEvaluatorException {
		final int idx = id.indexOf(pathFieldSeparator);
		final String pathName;
		final String fieldName;
		if (idx >= 0) {
			pathName = id.substring(0, idx);
			fieldName = id.substring(idx+1);
		}
		else {
			pathName = StringUtils.EMPTY;
			fieldName = id;
		}
		final Path<?> path = pathMap.get(pathName);
		if (path == null)
			throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_FIELD, "no path set for " + pathName); //$NON-NLS-1$
		return new JpaRuleContext(path, fieldName);
	}

	protected class JpaRuleContext implements IJpaRuleContext {
		private final String fieldName;
		private final Path<?> path;

		public JpaRuleContext(final Path<?> path, final String fieldName) {
			this.fieldName = fieldName;
			this.path = path;
		}

		@Override
		public CriteriaBuilder getCriteriaBuilder() {
			return criteriaBuilder;
		}

		@Override
		public Path<@Nullable String> getStringPath() throws QueryBuilderEvaluatorException {
			return assertPath(path.get(fieldName), String.class);
		}

		@Override
		public Path<? extends Integer> getIntegerPath() throws QueryBuilderEvaluatorException {
			return assertPath(path.get(fieldName), Integer.class);
		}

		@Override
		public Path<? extends @Nullable Double> getDoublePath() throws QueryBuilderEvaluatorException {
			return assertPath(path.get(fieldName), Double.class);
		}
	}

	protected static <Q> Path<Q> assertPath(final Path<Q> path, final Class<Q> clazz) throws QueryBuilderEvaluatorException {
		final Class<?> isClazz = path.getJavaType();
		if (!clazz.isAssignableFrom(isClazz)) {
			throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_FIELD, "expected " + clazz + " but got " + isClazz);
		}
		return path;
	}
}