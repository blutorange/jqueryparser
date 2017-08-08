package com.github.blutorange.jqueryparser.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

class JpaContext implements IJpaContext {
	private final CriteriaBuilder criteriaBuilder;
	private final Root<?> root;

	JpaContext(final CriteriaBuilder criteriaBuilder, final Root<?> root) {
		this.criteriaBuilder = criteriaBuilder;
		this.root = root;
	}
	
	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return criteriaBuilder;
	}

	@Override
	public IJpaRuleContext getFor(final IJpaContext context, final String id)
			throws QueryBuilderEvaluatorException {
		return new JpaRuleContext(id);
	}
	
	protected class JpaRuleContext implements IJpaRuleContext {
		private final String id;

		public JpaRuleContext(final String id) {
			this.id = id;
		}

		@Override
		public CriteriaBuilder getCriteriaBuilder() {
			return criteriaBuilder;
		}

		@Override
		public Path<@Nullable String> getStringPath() {
			return root.get(id);
		}
		
		@Override
		public Path<? extends Integer> getIntegerPath() {
			return root.get(id);
		}

		@Override
		public Path<? extends @Nullable Double> getDoublePath() throws QueryBuilderEvaluatorException {
			return root.get(id);
		}
	}

	protected static <Q> Path<Q> assertPath(final Path<Q> path, final Class<Q> clazz) throws QueryBuilderEvaluatorException {
		final Class<?> isClazz = path.getJavaType();
		if (isClazz != clazz) {
			throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_FIELD, "expected " + clazz + " but got " + isClazz);
		}
		return path;
	}
}