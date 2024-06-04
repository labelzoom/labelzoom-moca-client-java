package com.labelzoom.moca.exceptions;

import java.sql.ResultSet;

public class MocaExceptionFactory
{
    private MocaExceptionFactory() {}

    public static MocaException produce(final String message, final int code) { return produce(message, code, null); }
    public static MocaException produce(final String message, final int code, final ResultSet results)
    {
        switch (code)
        {
            case 510:
                final MocaException ex = new NotFoundException(message, code);
                ex.setResults(results);
                return ex;
            default:
                return new MocaException(message, code);
        }
    }
}
