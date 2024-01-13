package com.labelzoom.moca;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class MocaTester
{
    private static final String url = "http://BYWMS202011QA.robfaust.com:4600/service";
    private static final String userId = "RFAUST";
    private static final String password = "RFaust123";
    protected MocaConnection conn;

    @BeforeEach
    protected void setUp() throws Exception
    {
        conn = new HttpMocaConnection(url, userId, password);
    }

    @AfterEach
    protected void tearDown() throws Exception
    {
        conn.close();
    }
}
