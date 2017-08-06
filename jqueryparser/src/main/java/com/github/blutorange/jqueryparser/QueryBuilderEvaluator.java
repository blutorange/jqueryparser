package com.github.blutorange.jqueryparser;

import java.util.Iterator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;

class QueryBuilderEvaluator<@NonNull T, @NonNull C> implements IQueryBuilderEvaluator<@NonNull T, @NonNull C> {

	private static final String KEY_GROUP_CONDITION = "condition"; //$NON-NLS-1$
	private static final String KEY_GROUP_RULES = "rules"; //$NON-NLS-1$
	private static final String KEY_RULE_ID = "id"; //$NON-NLS-1$
	private static final String KEY_RULE_TYPE = "type"; //$NON-NLS-1$
	private static final String KEY_RULE_OPERATOR = "operator"; //$NON-NLS-1$
	private static final String KEY_RULE_VALUE = "value"; //$NON-NLS-1$
	private static final String KEY_RULE_INPUT = "input"; //$NON-NLS-1$
	private static final String KEY_VALID = "valid"; //$NON-NLS-1$

	private final IConditionFactory<@NonNull T, @NonNull C> conditionFactory;
	private final IRuleFactory<@NonNull T, @NonNull C> ruleFactory;
	private final NonNullSupplier<@NonNull C> contextSupplier;

	QueryBuilderEvaluator(final IConditionFactory<@NonNull T, @NonNull C> conditionFactory,
			final IRuleFactory<@NonNull T, @NonNull C> ruleFactory, final NonNullSupplier<@NonNull C> contextSupplier) {
		this.conditionFactory = conditionFactory;
		this.ruleFactory = ruleFactory;
		this.contextSupplier = contextSupplier;
	}

	@Override
	public T evaluate(final JSONObject group) throws QueryBuilderEvaluatorException {
		try {
			final C context = contextSupplier.get();
			if (!group.getBooleanValue(KEY_VALID)) {
				throw new QueryBuilderEvaluatorException(Codes.INVALID_RULESET);
			}
			return evalGroup(context, group);
		}
		catch (final QueryBuilderEvaluatorException e) {
			throw e;
		}
		catch (final Exception e) {
			throw new QueryBuilderEvaluatorException(Codes.UNHANDLED, "unhandled exception during evaluation", e); //$NON-NLS-1$
		}
	}

	private T evalGroup(final C context, final JSONObject group) throws QueryBuilderEvaluatorException {
		return consumeGroup(context, group, (condition, rules) -> {
			@Nullable T current = null;
			final Iterator<@Nullable Object> it = rules.iterator();
			if (!it.hasNext())
				throw new QueryBuilderEvaluatorException(Codes.EMPTY_RULE);
			do {
				final Object perhapsARule = it.next();
				if (!(perhapsARule instanceof JSONObject))
					throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "rule not a JSON object"); //$NON-NLS-1$
				final JSONObject rule = (JSONObject) perhapsARule;
				final T result;
				if (rule.containsKey(KEY_GROUP_CONDITION)) {
					result = evalGroup(context, rule);
				}
				else {
					result = evalRule(context, rule);
				}
				current = current == null ? result : condition.reduce(context, current, result);
			} while(it.hasNext());
			return current;
		});
	}

	private T evalRule(final C context, final JSONObject ruleObject) throws QueryBuilderEvaluatorException {
		return consumeRule(context, ruleObject, (rule, type, operator, input, values) -> {
			return rule.map(context, type, operator, input, values);
		});
	}

	private T consumeGroup(final C context, final JSONObject group, final IGroupConsumer<@NonNull T, @NonNull C> consumer)
			throws QueryBuilderEvaluatorException {
		final String conditionString = group.getString(KEY_GROUP_CONDITION);
		final JSONArray rules = group.getJSONArray(KEY_GROUP_RULES);
		if (conditionString == null)
			throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "group missing condition"); //$NON-NLS-1$
		if (rules == null)
			throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "group missing rules"); //$NON-NLS-1$
		final ICondition<@NonNull T, @NonNull C> condition = conditionFactory.getFor(context, conditionString);
		return consumer.accept(condition, rules);
	}

	private T consumeRule(final C context, final JSONObject ruleObject, final IRuleConsumer<@NonNull T, @NonNull C> consumer)
			throws QueryBuilderEvaluatorException {
		if (!ruleObject.containsKey(KEY_RULE_VALUE))
			throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "rule missing value"); //$NON-NLS-1$
		final String id = ruleObject.getString(KEY_RULE_ID);
		final String type = ruleObject.getString(KEY_RULE_TYPE);
		final String input = ruleObject.getString(KEY_RULE_INPUT);
		final String operator = ruleObject.getString(KEY_RULE_OPERATOR);
		final Object value = ruleObject.get(KEY_RULE_VALUE);
		if (id == null)
			throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "rule missing id"); //$NON-NLS-1$
		if (type == null)
			throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "rule missing type"); //$NON-NLS-1$
		if (operator == null)
			throw new QueryBuilderEvaluatorException(Codes.BAD_FORMAT, "rule missing operator"); //$NON-NLS-1$
		final IRule<@NonNull T, @NonNull C> rule = ruleFactory.getFor(context, id);
		if (value instanceof String)
			return consumer.accept(rule, type, operator, input, (String) value);
		if (value instanceof JSONArray) {
			final String[] values = ((JSONArray) value).stream().map(String::valueOf).toArray(String[]::new);
			return consumer.accept(rule, type, operator, input, values);
		}
		if (value == null)
			return consumer.accept(rule, type, operator, input);
		return consumer.accept(rule, type, operator, input, value.toString());
	}

	private static interface IGroupConsumer<@NonNull S, @NonNull Q> {
		S accept(ICondition<@NonNull S, @NonNull Q> condition, JSONArray rules) throws QueryBuilderEvaluatorException;
	}

	private static interface IRuleConsumer<@NonNull S, @NonNull Q> {
		S accept(IRule<@NonNull S, @NonNull Q> rule, String type, String operator, @Nullable String input, String... values)
				throws QueryBuilderEvaluatorException;
	}
}