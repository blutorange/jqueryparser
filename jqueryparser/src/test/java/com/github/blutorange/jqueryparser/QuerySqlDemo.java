package com.github.blutorange.jqueryparser;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Provider;

import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.blutorange.jqueryparser.querydsl.QuerySqlEvaluatorBuilder;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.BeanPath;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.PrimaryKey;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQueryFactory;

@SuppressWarnings({ "nls" })
public class QuerySqlDemo {
	@SuppressWarnings("null")
	public static void main(final String[] args) throws QueryBuilderEvaluatorException, IOException {
		// Now build the evaluator.
		final RelationalPath<QPerson> path = new QPerson("p");

		// Get test data...
		final String json = IOUtils.toString(QuerySqlDemo.class.getClassLoader()
				.getResourceAsStream("com/github/blutorange/jqueryparser/querysqlDemo.json"));
		final JSONObject group = JSON.parseObject(json);
		if (group == null)
			throw new QueryBuilderEvaluatorException("JSON parse error", "");

		// Evaluate the test data.
		// Keep a reference to the evaluator builder if you want to use it multiple times.
		final BooleanExpression whereClause = new QuerySqlEvaluatorBuilder<>(path).defaults().build().evaluate(group);
//		System.out.println(whereClause.toString());
//		System.exit(0);

		// Now try it out...
		final H2Templates templates = new H2Templates(true);
		final Configuration configuration = new Configuration(templates);
		final Provider<Connection> provider = () -> {
			try {
				final Connection conn = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "", "");
				prepareDb(conn);
				return conn;
			}
			catch (final SQLException e) {
				throw new RuntimeException(e);
			}
		};
		final SQLQueryFactory factory = new SQLQueryFactory(configuration, provider);
		System.out.println(factory.selectFrom(path).getResults());
//		System.out.println(factory.selectFrom(path).where(whereClause).fetchResults());
	}

	private static void prepareDb(final Connection conn) throws SQLException {
		conn.nativeSQL("create table PERSON (firstname varchar(255), lastname varchar(255));");
	}

	protected static class QPerson extends RelationalPathBase<QPerson> {

	    public static final QPerson person = new QPerson("PERSON");

	    public final StringPath firstname = createString("FIRSTNAME");

	    public final StringPath lastname = createString("LASTNAME");

	    public final NumberPath<Integer> id = createNumber("ID", Integer.class);

	    public final StringPath securedid = createString("SECUREDID");

	    public final PrimaryKey<QPerson> sysIdx118 = createPrimaryKey(id);

	    public QPerson(final String variable) {
	        super(QPerson.class, PathMetadataFactory.forVariable(variable), null, "PERSON");
	    }

	    public QPerson(final BeanPath<? extends QPerson> entity) {
	        super(entity.getType(), entity.getMetadata(), null, "PERSON");
	    }

	    public QPerson(final PathMetadata metadata) {
	        super(QPerson.class, metadata, null, "PERSON");
	    }

	}
}
