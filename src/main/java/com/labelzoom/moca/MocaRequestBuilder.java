package com.labelzoom.moca;

class MocaRequestBuilder
{
    private final boolean isHttp;
    private boolean autoCommit = true;

    private MocaRequestBuilder(final boolean isHttp)
    {
        this.isHttp = isHttp;
    }

    public static MocaRequestBuilder http()
    {
        return new MocaRequestBuilder(true);
    }

    public static MocaRequestBuilder legacy()
    {
        return new MocaRequestBuilder(false);
    }

    public MocaRequestBuilder withAutoCommit(final boolean autoCommit)
    {
        this.autoCommit = autoCommit;
        return this;
    }

    public MocaRequest command(final String command)
    {
        if (isHttp)
        {
            return new HttpMocaRequest(command, autoCommit);
        }
        else
        {
            return new LegacyMocaRequest(command);
        }
    }
}
