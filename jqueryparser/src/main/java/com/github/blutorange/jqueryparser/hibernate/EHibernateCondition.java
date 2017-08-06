package com.github.blutorange.jqueryparser.hibernate;

import org.eclipse.jdt.annotation.NonNull;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.github.blutorange.jqueryparser.ICondition;
import com.github.blutorange.jqueryparser.NonNullBiFunction;

public enum EHibernateCondition implements ICondition<Criterion, HibernateContext> {
	AND((l,r) -> Restrictions.and(l, r)),
	OR((l,r) -> Restrictions.or(l, r)),
	NOR((l,r) -> Restrictions.not(Restrictions.or(l,r))),
	NAND((l,r) -> Restrictions.not(Restrictions.and(l,r))),
	LEFT_IDENTITY((l,r) -> l),
	RIGHT_IDENTITY((l,r) -> r),
	LEFT_NEGATION((l,r) -> Restrictions.not(l)),
	RIGHT_NEGATION((l,r) -> Restrictions.not(r)),
	IMPLIES((l,r) -> Restrictions.or(l, Restrictions.not(r))),
	NOT_IMPLIES((l,r) -> Restrictions.and(Restrictions.not(l), r)),
	REVERSE_IMPLIES((l,r) -> Restrictions.or(r, Restrictions.not(l))),
	REVERSE_NOT_IMPLIES((l,r) -> Restrictions.and(Restrictions.not(r), l)),
	NEVER((l,r) -> Restrictions.and(l, Restrictions.not(l))),
	ALWAYS((l,r) -> Restrictions.or(l, Restrictions.not(l))),
	XOR((l,r) -> Restrictions.and(Restrictions.or(l,r), Restrictions.not(Restrictions.and(l,r)))),
	NXOR((l,r) -> Restrictions.or(Restrictions.and(l,r), Restrictions.not(Restrictions.or(l,r)))),
	;

	private NonNullBiFunction<@NonNull Criterion, @NonNull Criterion, @NonNull Criterion> function;

	private EHibernateCondition(final NonNullBiFunction<Criterion, Criterion, Criterion> function) {
		this.function = function;
	}

	@Override
	public Criterion reduce(final HibernateContext context, final Criterion left, final Criterion right) {
		return function.apply(left, right);
	}
}
