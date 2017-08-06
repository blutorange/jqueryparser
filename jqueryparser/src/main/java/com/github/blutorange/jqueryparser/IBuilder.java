package com.github.blutorange.jqueryparser;

import org.eclipse.jdt.annotation.NonNull;

public interface IBuilder<@NonNull T, E extends Throwable> {
	/**
	 * @return The built object.
	 * @throws E When a mandatory parameter is missing or a parameter is invalid.
	 */
	public T build() throws E;
}