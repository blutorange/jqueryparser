package com.github.blutorange.jqueryparser;

import javax.annotation.concurrent.Immutable;

import org.eclipse.jdt.annotation.NonNull;

@Immutable
public interface ICondition<@NonNull T, @NonNull C> {
	T reduce(C context, T left, T right);
}
