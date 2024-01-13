package com.labelzoom.moca;

import java.util.Map;

abstract class MocaRequest
{
    protected final String command;
    protected Map<String, String> context;
    protected Map<String, String> environment;

    protected MocaRequest(final String command)
    {
        this.command = command;
    }

    public MocaRequest context(final Map<String, String> context)
    {
        this.context = context;
        return this;
    }

    public MocaRequest environment(final Map<String, String> environment)
    {
        this.environment = environment;
        return this;
    }

    @Override
    public abstract String toString();
}
