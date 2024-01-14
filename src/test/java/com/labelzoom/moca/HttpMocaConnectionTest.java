package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

public class HttpMocaConnectionTest
{
    private final Dotenv dotenv = Dotenv.load();
    private final String url = dotenv.get("MOCA_URL");
    private final String userId = dotenv.get("MOCA_USER");
    private final String password = dotenv.get("MOCA_PASS");

    @Test
    public void testHttpMocaConnection() throws MocaException
    {
        try (final MocaConnection conn = new HttpMocaConnection(url, userId, password))
        {
            final ResultSet res = conn.execute("publish data where message = 'My name is Rob'");
            res.next();
            System.out.println("Message: " + res.getString("message"));
        }
        catch (final SQLException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testMultipleRows() throws MocaException
    {
        try (final MocaConnection conn = new HttpMocaConnection(url, userId, password))
        {
            final ResultSet res = conn.execute("publish data where a = 1 and b = 2 & publish data where a = 3 and b = 4");
            while (res.next())
            {
                System.out.println(String.format("a = %d\tb = %d", res.getInt("a"), res.getInt("b")));
            }
        }
        catch (final SQLException | IOException e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testRawHttp() throws IOException
    {
        final int TIMEOUT = 5000;
        final String command = "<moca-request><query>get encryption information</query></moca-request>";
        final URL url = new URL(this.url);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/moca-xml");
        byte[] bytes = command.getBytes(StandardCharsets.UTF_8);
        con.setFixedLengthStreamingMode(bytes.length);
        try (final OutputStream os = con.getOutputStream())
        {
            os.write(bytes);
        }
        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);
        int status = con.getResponseCode();
        try
        {
            if (status != 200)
            {
                try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getErrorStream())))
                {
                    final String responseBody = in.lines().collect(Collectors.joining());
                    System.err.println(responseBody);
                }
                throw new RuntimeException("HTTP status was NOT OK");
            }

            try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
            {
                final String responseBody = in.lines().collect(Collectors.joining());
                System.out.println("Response:");
                System.out.println(responseBody);
            }
        }
        finally
        {
            con.disconnect();
        }
    }
}
