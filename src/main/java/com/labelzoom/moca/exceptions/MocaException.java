package com.labelzoom.moca.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;

public class MocaException extends Exception
{
    private final int code;

    @Getter @Setter
    private ResultSet results;

    public MocaException(final String message, final int code)
    {
        super(message);
        this.code = code;
    }

    public int getErrorCode() { return code; }

}
