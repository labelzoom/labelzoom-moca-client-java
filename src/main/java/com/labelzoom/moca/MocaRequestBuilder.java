package com.labelzoom.moca;

class MocaRequestBuilder
{
    private final boolean isHttp;

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

    public MocaRequest command(final String command)
    {
        if (isHttp)
        {
            return new HttpMocaRequest(command);
        }
        else
        {
            return new LegacyMocaRequest(command);
        }
    }
}
