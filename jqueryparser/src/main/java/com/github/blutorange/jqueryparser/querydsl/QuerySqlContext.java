package com.github.blutorange.jqueryparser.querydsl;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

import com.github.blutorange.jqueryparser.IRuleContextProviding;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException;
import com.github.blutorange.jqueryparser.QueryBuilderEvaluatorException.Codes;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.sql.RelationalPath;

public class QuerySqlContext implements IRuleContextProviding<QuerySqlContext, SimpleExpression<Object>> {

	private Map<String, SimpleExpression<Object>> map;

	@SuppressWarnings("null")
	QuerySqlContext(final RelationalPath<?> path) throws QueryBuilderEvaluatorException {
		try {
			map = path.getColumns().stream()
				.map(Path::getMetadata).map(PathMetadata::getName)
				.collect(Collectors.toMap(Function.identity(), name -> {
					final Object o;
					try {
						o = path.getClass().getField(name).get(path);
					}
					catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
						throw new RuntimeException("cannot acquire column " + name, e);
					}
					if (o == null)
						throw new NullPointerException("column must not be null: " + name);
					return (SimpleExpression<Object>)o;
				}));
		}
		catch (final RuntimeException e) {
			throw new QueryBuilderEvaluatorException(Codes.PRECONDITION, "unable to acquire field", e);
		}
	}

	@Override
	public SimpleExpression<@NonNull Object> getFor(final QuerySqlContext context, final String id) throws QueryBuilderEvaluatorException {
		final SimpleExpression<Object> expr = map.get(id);
		if (expr == null)
			throw new QueryBuilderEvaluatorException(Codes.UNSUPPORTED_RULE, id);
		return expr;
	}
}
