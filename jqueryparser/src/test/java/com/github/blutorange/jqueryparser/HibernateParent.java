package com.github.blutorange.jqueryparser;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parent")
public class HibernateParent {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	public int id;

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return String.format("Parent[id=%s]", id);
	}


	public void setId(final int id) {
		this.id = id;
	}
}