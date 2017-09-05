package com.github.blutorange.jqueryparser;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import com.alibaba.fastjson.JSONObject;
import com.github.blutorange.jqueryparser.jpa.JpaEvaluatorBuilder;
/**
 * Illustrates how to setup a conditional query with JPA.
 * You can configure some options, but the most common use case looks
 * as follows. Given a JPA path for some entity:
 * <pre>
 * Predicate predicate = new JpaEvaluatorBuilder()
 *   .setCriteriaBuilder(criteriaBuilder)
 *   .setDefaultPath(jpaPath)
 *   .build()
 *   .evaluate(jQueryBuilderJson);
 * </code>
 * Now you can apply the predicate to the where clause of your query.
 * @author madgaksha
 */
@SuppressWarnings({ "nls", "null" })
public class JpaDemo extends AbstractDemo {
	public static void main(final String[] args) throws QueryBuilderEvaluatorException, IOException {
		try (final SessionFactory sf = setUp(); final Session s = sf.openSession()) {
			final List<?> result = perform(s.getEntityManagerFactory().createEntityManager());
			for (final Object o : result) {
				System.out.println(o.getClass().getCanonicalName() + ": " + o.toString());
			}
		}
	}

	private static List<?> perform(final EntityManager em) throws QueryBuilderEvaluatorException, IOException {
		// Prepare the query.
		final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		final CriteriaQuery<HibernateChild> queryChild = criteriaBuilder.createQuery(HibernateChild.class);
		final Root<HibernateChild> rootChild = queryChild.from(HibernateChild.class);
		final Join<HibernateChild, HibernateParent> joinChildParent = rootChild.<HibernateChild, HibernateParent>join("parent");

		// Configure the evaluator.
		final IQueryBuilderEvaluator<Predicate, ?> evaluator = new JpaEvaluatorBuilder()
				// Set some default configuration you most likely don't want to change.
				.defaults()
				.setCriteriaBuilder(criteriaBuilder)
				.setDefaultPath(rootChild)
				.addPath("parent", joinChildParent)
				.build();

		// Get test data
		final JSONObject group = getGroup("com/github/blutorange/jqueryparser/hibernateDemo.json");

		// Evaluate the test data.
		// This returns a predicate we can use in the where clause.
		final Predicate predicate = evaluator.evaluate(group);
		System.out.println(predicate);

		// Insert some test data into the database.
		try (final SessionFactory sf = setUp(); final Session s = sf.openSession()) {
			insertData(s);
		}

		// Now apply the predicate and run the query.
		queryChild.where(predicate);
		final TypedQuery<HibernateChild> typedQuery = em.createQuery(queryChild.select(rootChild));
		return typedQuery.getResultList();
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
		s.save(new HibernateChild().setAge(5).setFirstName("Andre").setLastName("Wachsmuth").setHibernateParent(null));
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
			throw e;
		}
	}
}
