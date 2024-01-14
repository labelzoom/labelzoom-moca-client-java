package com.labelzoom.moca;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class MocaTester
{
    private final Dotenv dotenv = Dotenv.load();
    private final String url = dotenv.get("MOCA_URL");
    private final String userId = dotenv.get("MOCA_USER");
    private final String password = dotenv.get("MOCA_PASS");
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
