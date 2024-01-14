package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class HttpMocaConnection extends MocaConnection
{
    private static final int TIMEOUT = 5000;
    private final String url;

    public HttpMocaConnection(final String url, final String userId, final String password) throws MocaException
    {
        this.url = url;
        this.login(userId, password);
    }

    protected MocaResponse send(final String command) throws MocaException
    {
        try
        {
            final URL url = new URL(this.url);
            final HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/moca-xml");
            final byte[] bytes = command.getBytes(StandardCharsets.UTF_8);
            con.setFixedLengthStreamingMode(bytes.length);
            try (final OutputStream os = con.getOutputStream())
            {
                os.write(bytes);
            }
            con.setConnectTimeout(TIMEOUT);
            con.setReadTimeout(TIMEOUT);
            final int status = con.getResponseCode();
            try
            {
                if (status != 200)
                {
                    throw new RuntimeException("HTTP status was NOT OK");
                }

                try (final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
                {
                    final String responseBody = in.lines().collect(Collectors.joining());
                    return MocaResponse.fromXml(responseBody);
                }
            }
            finally
            {
                con.disconnect();
            }
        }
        catch (final IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected MocaRequestBuilder getRequestBuilder()
    {
        return MocaRequestBuilder.http();
    }
}
