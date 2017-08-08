package com.github.blutorange.jqueryparser.xfc;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.IRuleContextProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;

public class XfcContext implements IRuleContextProviding<XfcContext, String> {
	XfcContext() {
	}
	
	@Override
	public @NonNull String getFor(@NonNull final XfcContext context, @NonNull final String id)
			throws QueryBuilderEvaluatorException {
		return id;
	}
}