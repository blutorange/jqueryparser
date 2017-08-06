package com.github.blutorange.jqueryparser.querydsl;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.ICondition;
import com.github.blutorange.jqueryparser.NonNullBiFunction;
import com.querydsl.core.types.dsl.BooleanExpression;

public enum EQuerySqlCondition implements ICondition<BooleanExpression, QuerySqlContext> {
	AND((l,r) -> l.and(r)),
	OR((l,r) -> l.or(r)),
	XOR((l,r) -> l.eq(r).not()),
	NAND((l,r) -> l.and(r).not()),
	NOR((l,r) -> l.or(r).not()),
	NXOR((l,r) -> l.eq(r)),
	IMPLIES((l,r) -> l.or(r.not())),
	NOT_IMPLIES((l,r) -> l.not().and(r)),
	REVERSE_IMPLIES((l,r) -> r.or(l.not())),
	REVERSE_NOT_IMPLIES((l,r) -> r.not().and(l)),
	LEFT_NEGATION((l,r) -> l.not()),
	RIGHT_NEGATION((l,r) -> r.not()),
	LEFT_IDENTITY((l,r) -> l),
	RIGHT_IDENTITY((l,r) -> r),
	NEVER((l,r) -> l.and(l.not())),
	ALWAYS((l,r) -> l.or(l.not())),
	;

	private NonNullBiFunction<@NonNull BooleanExpression, @NonNull BooleanExpression, @NonNull BooleanExpression> function;

	private EQuerySqlCondition(final NonNullBiFunction<BooleanExpression, BooleanExpression, BooleanExpression> function) {
		this.function = function;
	}

	@Override
	public BooleanExpression reduce(@NonNull final QuerySqlContext context, final BooleanExpression left, final BooleanExpression right) {
		return function.apply(left, right);
	}
}
