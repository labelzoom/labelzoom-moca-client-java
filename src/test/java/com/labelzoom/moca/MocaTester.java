package com.labelzoom.moca;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class MocaTester
{
    private final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private final String url = Optional.ofNullable(dotenv.get("MOCA_URL")).orElse(System.getenv("MOCA_URL"));
    private final String userId = Optional.ofNullable(dotenv.get("MOCA_USER")).orElse(System.getenv("MOCA_USER"));
    private final String password = Optional.ofNullable(dotenv.get("MOCA_PASS")).orElse(System.getenv("MOCA_PASS"));
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
    protected void tearDown() throws Exception
    {
        conn.close();
    }
}
