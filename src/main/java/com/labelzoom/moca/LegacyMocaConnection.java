package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

@Slf4j
public class LegacyMocaConnection extends MocaConnection
{
    private final String host;
    private final int port;

    public LegacyMocaConnection(final String host, final int port, final String userId, final String password) throws MocaException
    {
        this.host = host;
        this.port = port;
        this.login(userId, password);
    }

    protected MocaResponse send(String command) throws MocaException
    {
        command = "V103^45^^~0~~0~~-1^^002050^get encryption information"; // TODO: Get rid of this
        try (final Socket socket = new Socket(this.host, this.port))
        {
            socket.getOutputStream().write(command.getBytes(StandardCharsets.US_ASCII));

            while (socket.getInputStream().available() == 0)
            {
                Thread.sleep(100L);
            }

            final byte[] data = new byte[socket.getInputStream().available()];
            final int bytes = socket.getInputStream().read(data, 0, data.length);
            final String responseData = new String(data, 0 , bytes, StandardCharsets.US_ASCII);
            return MocaResponse.fromLegacy(responseData);
        }
        catch (final IOException | InterruptedException ex)
        {
            log.error("error sending MOCA via socket", ex);
        }
        return null;
    }

    @Override
    protected MocaRequestBuilder getRequestBuilder()
    {
        return MocaRequestBuilder.legacy();
    }
}
