package com.github.blutorange.jqueryparser.immediate;

public class ImmediateResult {
	private static final ImmediateResult TRUE = new ImmediateResult(true);
	private static final ImmediateResult FALSE = new ImmediateResult(false);
	private final boolean result;
	private ImmediateResult(final boolean result) {
		this.result = result;
	}
	public boolean getResult() {
		return result;
	}
	public static ImmediateResult valueOf(final boolean result) {
		return result ? TRUE : FALSE;
	}

}
