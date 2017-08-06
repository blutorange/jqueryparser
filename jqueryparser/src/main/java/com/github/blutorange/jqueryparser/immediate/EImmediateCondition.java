package com.github.blutorange.jqueryparser.immediate;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.ICondition;

public enum EImmediateCondition {
	NEVER((l,r) -> false),
	AND((l,r) -> l && r),
	REVERSE_NOT_IMPLIES((l,r) -> !r && l),
	LEFT_IDENTITY((l,r) -> l),
	NOT_IMPLIES((l,r) -> !l && r),
	RIGHT_IDENTITY((l,r) -> r),
	XOR((l,r) -> l ^ r),
	OR((l,r) -> l || r),
	NOR((l,r) -> !(l||r)),
	NXOR((l,r) -> !(l ^ r)),
	RIGHT_NEGATION((l,r) -> !r),
	IMPLIES((l,r) -> l || !r),
	LEFT_NEGATION((l,r) -> !l),
	REVERSE_IMPLIES((l,r) -> r || !l),
	NAND((l,r) -> !(l && r)),
	ALWAYS((l,r) -> true),
	;
	private BiBooleanPredicate predicate;

	private EImmediateCondition(final BiBooleanPredicate predicate) {
		this.predicate = predicate;
	}

	public <C> ICondition<@NonNull ImmediateResult, @NonNull C> getCondition() {
		return (context,l,r) -> {
			final boolean result = predicate.test(l.getResult(), r.getResult());
			return ImmediateResult.valueOf(result);
		};
	}

	protected static interface BiBooleanPredicate {
		boolean test(boolean l, boolean r);
	}
}