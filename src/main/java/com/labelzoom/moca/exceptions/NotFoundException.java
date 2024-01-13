package com.labelzoom.moca.exceptions;

public class NotFoundException extends MocaException
{
    public NotFoundException(final String message, final int code)
    {
        super(message, code);
    }
}
