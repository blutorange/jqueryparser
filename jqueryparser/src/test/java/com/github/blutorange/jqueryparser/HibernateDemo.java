package com.github.blutorange.jqueryparser;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Criterion;

import com.alibaba.fastjson.JSONObject;
import com.github.blutorange.jqueryparser.hibernate.HibernateEvaluatorBuilder;

@SuppressWarnings({ "nls", "null" })
public class HibernateDemo extends AbstractDemo {
	public static void main(final String[] args) throws QueryBuilderEvaluatorException, IOException {
		// Configure the evaluator.
		final HibernateEvaluatorBuilder builder = new HibernateEvaluatorBuilder().defaults();
		builder.addEntityAlias("parent", "p");
		final IQueryBuilderEvaluator<Criterion, ?> evaluator = builder.build();

		// Get test data
		final JSONObject group = getGroup("com/github/blutorange/jqueryparser/hibernateDemo.json");

		// Evaluate the test data.
		final Criterion criterion = evaluator.evaluate(group);
		System.out.println(criterion);

		// Insert some test data.
		try (final SessionFactory sf = setUp(); final Session s = sf.openSession()) {
			insertData(s);
		}

		// Now run the query.
		try (final SessionFactory sf = setUp(); final Session s = sf.openSession()) {
			final List<?> result = s.createCriteria(HibernateChild.class).createAlias("hibernateParent", "p")
					.add(criterion).list();
			for (final Object o : result) {
				System.out.println(o.getClass().getCanonicalName() + ": " + o.toString());
			}
		}
	}

	private static void insertData(final Session s) {
		s.beginTransaction();
		s.save(new HibernateChild().setAge(19).setFirstName("Andre").setLastName("Wachsmuth")
				.setHibernateParent(new HibernateParent()));
		s.save(new HibernateChild().setAge(19).setFirstName("andre").setLastName("wachsmuth")
				.setHibernateParent(new HibernateParent()));
		s.save(new HibernateChild().setAge(19).setFirstName("Steve").setLastName("Munzer")
				.setHibernateParent(new HibernateParent()));
		s.save(new HibernateChild().setAge(13).setFirstName("Andre").setLastName("Wachsmuth")
				.setHibernateParent(new HibernateParent()));
		s.save(new HibernateChild().setAge(5).setFirstName("Andre").setLastName("Wachsmuth")
				.setHibernateParent(null));
		s.save(new HibernateChild().setAge(5).setFirstName("Andre").setLastName("Wachsmuth")
				.setHibernateParent(new HibernateParent()));
		s.getTransaction().commit();
	}

	private static SessionFactory setUp() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			return new MetadataSources(registry).buildMetadata().buildSessionFactory();
		}
		catch (final Exception e) {
			e.printStackTrace();
			StandardServiceRegistryBuilder.destroy(registry);
			return null;
		}
	}
}
