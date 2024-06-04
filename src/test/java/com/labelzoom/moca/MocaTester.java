package com.labelzoom.moca;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class MocaTester
{
    private final String url = System.getenv("MOCA_URL");
    private final String userId = System.getenv("MOCA_USER");
    private final String password = System.getenv("MOCA_PASS");
    protected MocaConnection conn;

    @BeforeEach
    protected void setUp() throws Exception
    {
        assertNotNull(url);
        assertNotNull(userId);
        assertNotNull(password);
        conn = new HttpMocaConnection(url, userId, password);
    }

    @AfterEach
    protected void tearDown()
    {
        conn.close();
    }
}
