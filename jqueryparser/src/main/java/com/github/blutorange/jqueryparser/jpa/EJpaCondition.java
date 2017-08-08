package com.github.blutorange.jqueryparser.jpa;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;

import com.github.blutorange.jqueryparser.ICondition;

public enum EJpaCondition implements ICondition<Predicate, IJpaContext> {
	AND((cb,l,r) -> cb.and(l,r)),
	OR((cb,l,r) -> cb.or(l,r)),
	NOR((cb,l,r) -> cb.or(l,r).not()),
	NAND((cb,l,r) -> cb.and(l,r).not()),
	LEFT_IDENTITY((cb,l,r) -> l),
	RIGHT_IDENTITY((cb,l,r) -> r),
	LEFT_NEGATION((cb,l,r) -> l.not()),
	RIGHT_NEGATION((cb,l,r) -> r.not()),
	IMPLIES((cb,l,r) -> cb.or(l, r.not())),
	NOT_IMPLIES((cb,l,r) -> cb.and(l.not(),r)),
	REVERSE_IMPLIES((cb,l,r) -> cb.or(r, l.not())),
	REVERSE_NOT_IMPLIES((cb,l,r) -> cb.and(r.not(), l)),
	NEVER((cb,l,r) -> cb.and(l, l.not())),
	ALWAYS((cb,l,r) -> cb.or(l, l.not())),
	XOR((cb,l,r) -> cb.equal(l, r).not()),
	NXOR((cb,l,r) -> cb.equal(l, r)),
	;

	private Reducer reducer;

	private EJpaCondition(final Reducer reducer) {
		this.reducer = reducer;
	}

	@Override
	public Predicate reduce(final IJpaContext context, final Predicate left, final Predicate right) {
		return reducer.apply(context.getCriteriaBuilder(), left, right);
	}
	
	protected static interface Reducer {
		Predicate apply(CriteriaBuilder criteriaBuilder, Predicate left, Predicate right);
	}
}
