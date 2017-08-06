package com.github.blutorange.jqueryparser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.blutorange.jqueryparser.SwitchRuleFactory.SwitchRuleFactoryBuilder;
import com.github.blutorange.jqueryparser.immediate.ImmediateEvaluatorBuilder;
import com.github.blutorange.jqueryparser.immediate.ImmediateResult;
import com.github.blutorange.jqueryparser.immediate.ImmediateRuleFactoryBuilder;

@SuppressWarnings({ "nls" })
public class ImmediateDemo {
	public static void main(final String[] args) throws QueryBuilderEvaluatorException, IOException {
		// Mocks some arbitrary context object retrieving value from somewhere.
		// All filters use this mock for retrieving data.
		final IRuleFactory<ImmediateResult, Mock> ruleFactoryMock = new ImmediateRuleFactoryBuilder<Mock>()
				.setDefaultRule((mock, fieldName) -> mock.resolve(fieldName)).build();

		// Make some static filters with constant values.
		final IRuleFactory<ImmediateResult, Mock> ruleFactoryConstants = new ImmediateRuleFactoryBuilder<Mock>()
				.addRule("0", "0")
				.addRule("0", "1")
				.addRule("false", "false")
				.addRule("true", "true").build();

		// Use scope "mock" and "constants" for the two filter types above,
		// respectively.
		final IRuleFactory<ImmediateResult, Mock> ruleFactory = new SwitchRuleFactoryBuilder<ImmediateResult, Mock>()
				.addFactory("mock", ruleFactoryMock)
				.addFactory("constants", ruleFactoryConstants).build();

		// Now build the evaluator.
		final IQueryBuilderEvaluator<ImmediateResult, Mock> evaluatorBuilder = new ImmediateEvaluatorBuilder<Mock>()
				.setRuleFactory(ruleFactory)
				.setContextSupplier(new Mock())
				.build();

		// Get test data...
		final String json = IOUtils.toString(
				ImmediateDemo.class.getClassLoader().getResourceAsStream("com/github/blutorange/jqueryparser/immediateDemo.json"));
		final JSONObject group = JSON.parseObject(json);
		if (group == null)
			throw new QueryBuilderEvaluatorException("JSON parse error", "");

		// ...and evaluate the it.
		final ImmediateResult result = evaluatorBuilder.evaluate(group);
		System.out.println(result.getResult());
	}

	protected static class Mock {
		private static Map<String, String> MAP = new HashMap<>();
		static {
			MAP.put("hello", "world");
			MAP.put("foo", "bar");
			MAP.put("foobar", "42");
		}
		public Optional<String> resolve(final String name) {
			return Optional.ofNullable(MAP.get(name));
		}
	}
}
