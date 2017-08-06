package com.github.blutorange.jqueryparser;

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public abstract class AbstractDemo {
	public static JSONObject getGroup(final String path) throws QueryBuilderEvaluatorException, IOException {
		final String json = IOUtils.toString(AbstractDemo.class.getClassLoader()
				.getResourceAsStream(path));
		final JSONObject group = JSON.parseObject(json);
		if (group == null)
			throw new QueryBuilderEvaluatorException("JSON parse error", "returned null");
		return group;
	}
}
