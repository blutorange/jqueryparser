package com.github.blutorange.jqueryparser;

public class QueryBuilderEvaluatorException extends Exception {
	public static class Codes {
		private Codes() {}
		public static final String UNSUPPORTED_CONDITION = "CONDITION_UNSUPPORTED"; //$NON-NLS-1$
		public static final String UNHANDLED = "UNHANDLED_EXCEPTION"; //$NON-NLS-1$
		public static final String BAD_FORMAT = "BAD_FORMAT"; //$NON-NLS-1$
		public static final String PRECONDITION = "PRECONDITION"; //$NON-NLS-1$
		public static final String UNSUPPORTED_RULE = "RULE_UNSUPPORTED"; //$NON-NLS-1$
		public static final String ILLEGAL_NUMBER_OF_VALUES = "ILLEGAL_NUMBER_OF_VALUES"; //$NON-NLS-1$
		public static final String UNSUPPORTED_TYPE = "TYPE_UNSUPPORTED"; //$NON-NLS-1$
		public static final String UNSUPPORTED_OPERATOR = "UNSUPPORTED_OPERATOR"; //$NON-NLS-1$
		public static final String INVALID_RULESET = "INVALID_RULESET"; //$NON-NLS-1$
		public static final String EMPTY_RULE = "EMPTY_RULE"; //$NON-NLS-1$
		public static final String UNSUPPORTED_FIELD = "UNSUPPORTED_FIELD"; //$NON-NLS-1$
	}

	private static final long serialVersionUID = 1L;
	private final String type;
    public QueryBuilderEvaluatorException(final String type, final String message, final Throwable cause) {
        super(String.format("%s: %s", type, message), cause); //$NON-NLS-1$
        this.type = type;
    }
    public QueryBuilderEvaluatorException(final String type, final String message) {
        super(String.format("%s: %s", type, message), null); //$NON-NLS-1$
        this.type = type;
    }

    public QueryBuilderEvaluatorException(final String type) {
        super(String.format("%s", type), null); //$NON-NLS-1$
        this.type = type;
    }

    public String getType() {
    	return type;
    }
}
