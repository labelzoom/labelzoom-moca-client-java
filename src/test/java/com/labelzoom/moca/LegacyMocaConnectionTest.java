package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/*
V103^45^^~0~~0~~-1^^002050^get encryption information
V104^83^2^^^76^^^~0~~0~~-1^USR_ID=RFAUST^001794^check single signon where usr_id = 'RFAUST'
V104^101^2^^^94^^^~0~~0~~-1^USR_ID=RFAUST^001794^login user where usr_id = 'RFAUST' and usr_pswd = 'RFaust123'
V104^426^2^^^418^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^000002^try { list les option authorizations where opt_nam = 'ORBITAL-SERVICES-LEXTEDIT' } catch(-1403) { list les options where opt_nam = 'ORBITAL-SERVICES-LEXTEDIT' } >> res| publish top rows where rows = 1 and res = @res| publish data where desc = @mls_text
V104^223^2^^^215^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^003330^list library versions where category = 'MOCAbase'
V104^210^2^^^202^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^000002^publish data where dbtype = dbtype()
 */

/**
 * TODO: LegacyMocaConnection needs some work
 */
@Disabled
public class LegacyMocaConnectionTest
{
    private final Dotenv dotenv = Dotenv.load();
    private final String host = dotenv.get("MOCA_HOST");
    private final int port = Integer.parseInt(dotenv.get("MOCA_PORT"));
    private final String userId = dotenv.get("MOCA_USER");
    private final String password = dotenv.get("MOCA_PASS");

    @Test
    public void testReadersAndWriters() throws IOException, InterruptedException
    {
        try (final Socket socket = new Socket(host, port))
        {
            try (final PrintWriter out = new PrintWriter(socket.getOutputStream(), true))
            {
                try (final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII)))
                {
                    //socket.getOutputStream().write("V103^45^^~0~~0~~-1^^002050^get encryption information".getBytes(StandardCharsets.US_ASCII));
                    out.println("V103^45^^~0~~0~~-1^^002050^get encryption information");

                    final StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = in.readLine()) != null)
                    {
                        sb.append(line);
                    }
                    final String responseData = sb.toString();
                    assertTrue(responseData.startsWith("V104^"));
                    System.out.println(responseData);
                }
            }
        }
    }

    @Test
    public void testRawV103() throws IOException, InterruptedException
    {
        try (final Socket socket = new Socket(host, port))
        {
            socket.getOutputStream().write("V103^45^^~0~~0~~-1^^002050^get encryption information".getBytes(StandardCharsets.US_ASCII));

            while (socket.getInputStream().available() == 0)
            {
                Thread.sleep(100L);
            }

            byte[] data = new byte[socket.getInputStream().available()];
            final int bytes = socket.getInputStream().read(data, 0, data.length);
            final String responseData = new String(data, 0 , bytes, "ASCII");
            assertTrue(responseData.startsWith("V104^"));
            System.out.println(responseData);
        }
    }

    @Test
    public void testRawV104() throws IOException, InterruptedException
    {
        try (final Socket socket = new Socket(host, port))
        {
            socket.getOutputStream().write("V104^101^2^^^94^^^~0~~0~~-1^USR_ID=RFAUST^001794^login user where usr_id = 'RFAUST' and usr_pswd = 'RFaust123'".getBytes(StandardCharsets.US_ASCII));

            while (socket.getInputStream().available() == 0)
            {
                Thread.sleep(100L);
            }

            byte[] data = new byte[socket.getInputStream().available()];
            final int bytes = socket.getInputStream().read(data, 0, data.length);
            final String responseData = new String(data, 0 , bytes, "ASCII");
            assertTrue(responseData.startsWith("V104^"));
            System.out.println(responseData);
        }
    }

    @Test
    public void testLegacyMocaConnection() throws MocaException
    {
        try (final MocaConnection conn = new LegacyMocaConnection(host, port, userId, password))
        {
            final ResultSet res = conn.execute("create policy where polcod = 'LEGACY-MOCA-CLIENT' and polvar = 'LEGACY-MOCA-CLIENT' and polval = 'LEGACY-MOCA-CLIENT' and rtstr1 = 'success'");
            res.next();
            System.out.println("Message: " + res.getString("message"));
        }
        catch (final SQLException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
