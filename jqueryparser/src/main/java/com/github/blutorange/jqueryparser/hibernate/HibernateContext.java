package com.github.blutorange.jqueryparser.hibernate;

import com.github.blutorange.jqueryparser.IRuleContextProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

public class HibernateContext implements IRuleContextProviding<HibernateContext, String>{
	@Override
	public String getFor(final HibernateContext context, final String id)
			throws QueryBuilderEvaluatorException {
		return id;
	}
}
