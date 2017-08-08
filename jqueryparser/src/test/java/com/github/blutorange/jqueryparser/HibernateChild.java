package com.github.blutorange.jqueryparser;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.eclipse.jdt.annotation.Nullable;

@Entity
@Table(name = "child")
public class HibernateChild {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	public int id;

	@Column(name = "age")
	public int age;

	@Column(name = "first_name")
	public String firstName = "andre";

	@Column(name = "last_name")
	public String lastName = "wachsmuth";

	@Nullable
	@OneToOne(cascade = CascadeType.ALL)
	public HibernateParent hibernateParent = new HibernateParent();

	/**
	 * @return the id
	 */
	protected int getId() {
		return id;
	}

	/**
	 * @return the firstName
	 */
	protected String getFirstName() {
		return firstName;
	}

	@Override
	public String toString() {
		return String.format("Child[id=%s](%s,%s,%s) <- %s", id, firstName, lastName, age, hibernateParent);
	}

	/**
	 * @return the lastName
	 */
	protected String getLastName() {
		return lastName;
	}

	/**
	 * @return the hibernateParent
	 */
	@Nullable
	protected HibernateParent getHibernateParent() {
		return hibernateParent;
	}

	/**
	 * @param id the id to set
	 */
	protected HibernateChild setId(final int id) {
		this.id = id;
		return this;
	}

	/**
	 * @param firstName the firstName to set
	 */
	protected HibernateChild setFirstName(final String firstName) {
		this.firstName = firstName;
		return this;
	}

	/**
	 * @param lastName the lastName to set
	 */
	protected HibernateChild setLastName(final String lastName) {
		this.lastName = lastName;
		return this;
	}

	/**
	 * @param hibernateParent the hibernateParent to set
	 */
	protected HibernateChild setHibernateParent(@Nullable final HibernateParent hibernateParent) {
		this.hibernateParent = hibernateParent;
		return this;
	}

	/**
	 * @return the age
	 */
	protected int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	protected HibernateChild setAge(final int age) {
		this.age = age;
		return this;
	}


}