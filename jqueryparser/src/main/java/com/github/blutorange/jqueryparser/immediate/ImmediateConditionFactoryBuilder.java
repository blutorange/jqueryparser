package com.github.blutorange.jqueryparser.immediate;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.github.blutorange.jqueryparser.ConditionFactory;
import com.github.blutorange.jqueryparser.IBuilder;
import com.github.blutorange.jqueryparser.ICondition;
import com.github.blutorange.jqueryparser.IConditionFactory;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

/**
 * The following combinations of operands are possible:
 * <table>
 * <thead><td>left</td><td>right</td></thead>
 * <tr><td>0</td><td>0</td></tr>
 * <tr><td>0</td><td>1</td></tr>
 * <tr><td>1</td><td>0</td></tr>
 * <tr><td>1</td><td>1</td></tr>
 * </table>
 * An operator is an ordered set of 4 booleans, the result for each of the
 * above four combinations of operands. Thus, there are 16 possible binary
 * operators.
 * <table>
 * <thead><td>result</td><td>name</td></thead>
 * <tr><td>0000</td><td>NEVER</td></tr>
 * <tr><td>0001</td><td>AND</td></tr>
 * <tr><td>0011</td><td>LEFT_IDENTITY</td></tr>
 * <tr><td>0100</td><td>NOT_IMPLIES</td></tr>
 * <tr><td>0101</td><td>RIGHT_IDENTITY</td></tr>
 * <tr><td>0110</td><td>XOR</td></tr>
 * <tr><td>0111</td><td>OR</td></tr>
 * <tr><td>1000</td><td>NOR</td></tr>
 * <tr><td>1001</td><td>NXOR</td></tr>
 * <tr><td>1010</td><td>RIGHT_NEGATION</td></tr>
 * <tr><td>1011</td><td>IMPLIES</td></tr>
 * <tr><td>1100</td><td>LEFT_NEGATION</td></tr>
 * <tr><td>1110</td><td>NAND</td></tr>
 * <tr><td>1111</td><td>ALWAYS</td></tr>
 *
 * <tr><td>0110</td><td>SAME</td></tr>
 * <tr><td>1001</td><td>NOT_SAME</td></tr>
 * </table>
 * @author madgaksha
 */
public class ImmediateConditionFactoryBuilder<@NonNull C> implements IBuilder<IConditionFactory<@NonNull ImmediateResult, C>, QueryBuilderEvaluatorException> {
	@Nullable
	private Builder<String, ICondition<@NonNull ImmediateResult, C>> map;

	public ImmediateConditionFactoryBuilder<C> addName(final String name, final ICondition<@NonNull ImmediateResult, C> condition) {
		getMap().put(name, condition);
		return this;
	}

	private ImmutableMap.Builder<String, ICondition<@NonNull ImmediateResult, C>> getMap() {
		return map != null ? map : (map = new ImmutableMap.Builder<>());
	}

	@Override
	public IConditionFactory<@NonNull ImmediateResult, C> build() {
		final IConditionFactory<@NonNull ImmediateResult, C> f = new ConditionFactory<>(getMap());
		map = null;
		return f;
	}

	public static <@NonNull C> IConditionFactory<@NonNull ImmediateResult, C> getDefault() {
		final ImmediateConditionFactoryBuilder<C> b = new ImmediateConditionFactoryBuilder<>();
		for (final EImmediateCondition conn : EImmediateCondition.values())
			b.addName(conn.name(), conn.getCondition());
		return b.build();
	}
}