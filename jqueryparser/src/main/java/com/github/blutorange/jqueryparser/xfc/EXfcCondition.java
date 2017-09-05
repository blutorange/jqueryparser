package com.github.blutorange.jqueryparser.xfc;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.ICondition;
import com.github.blutorange.jqueryparser.NonNullBiFunction;

import de.xima.cmn.criteria.FilterCriterion;

/**
 * !l = (l||!l)&&!l
 */
enum EXfcCondition implements ICondition<FilterCriterion, XfcContext> {
  AND((l, r) -> l.and(r)),
  OR((l, r) -> l.or(r)),
  NOR((l, r) -> l.orNot(l).andNot(l).andNot(r)),
  NAND((l, r) -> l.orNot(l).andNot(l).orNot(r)),
  LEFT_IDENTITY((l, r) -> l),
  RIGHT_IDENTITY((l, r) -> r),
  LEFT_NEGATION((l, r) -> l.orNot(l).andNot(l)),
  RIGHT_NEGATION((l, r) -> r.orNot(r).andNot(r)),
  IMPLIES((l, r) -> l.orNot(r)),
  NOT_IMPLIES((l, r) -> r.andNot(l)),
  REVERSE_IMPLIES((l, r) -> r.orNot(l)),
  REVERSE_NOT_IMPLIES((l, r) -> l.andNot(r)),
  NEVER((l, r) -> l.andNot(l)),
  ALWAYS((l, r) -> l.orNot(l)),
  XOR((l, r) -> l.andNot(r).or(r.andNot(l))),
  NXOR((l, r) -> l.and(r).orNot(l).and(l.and(r).orNot(r))),
  ;

  private NonNullBiFunction<@NonNull FilterCriterion, @NonNull FilterCriterion, @NonNull FilterCriterion> function;

  private EXfcCondition(final NonNullBiFunction<FilterCriterion, FilterCriterion, FilterCriterion> function) {
    this.function = function;
  }

  @Override
  public FilterCriterion reduce(final XfcContext context, final FilterCriterion left, final FilterCriterion right) {
    return function.apply(left, right);
  }
}
