package com.github.blutorange.jqueryparser.jpa;

import javax.persistence.criteria.CriteriaBuilder;

import com.github.blutorange.jqueryparser.IRuleContextProviding;

public interface IJpaContext extends IRuleContextProviding<IJpaContext, IJpaRuleContext> {
	CriteriaBuilder getCriteriaBuilder();
}