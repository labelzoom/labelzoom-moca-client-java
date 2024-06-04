package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public abstract class MocaConnection extends AbstractConnection
{
    protected boolean isClosed = false;
    protected final Map<String, String> environment = new TreeMap<>();
    protected String sessionKey;

    protected abstract MocaResponse send(final String command) throws MocaException;

    protected abstract MocaRequestBuilder getRequestBuilder();

    public ResultSet execute(final String command) throws MocaException
    {
        if (isClosed)
        {
            throw new IllegalStateException("Connection is closed");
        }
        if (sessionKey == null)
        {
            throw new IllegalStateException("Not logged in");
        }

        // TODO: Expand on environment
        final Map<String, String> env = new TreeMap<>();
        env.put("SESSION_KEY", sessionKey);
        final MocaRequest request = getRequestBuilder().command(command).environment(env);
        return send(request.toString()).getResults();
    }

    protected void login(final String userId, final String password) throws MocaException
    {
        if (isClosed)
        {
            throw new IllegalStateException("Connection is closed");
        }
        final Map<String, String> context = new TreeMap<>();
        context.put("usr_id", userId);
        context.put("usr_pswd", password);
        final MocaRequest req = getRequestBuilder().command("login user").context(context);
        final MocaResponse response = send(req.toString());
        if (response.getStatusCode() == 0)
        {
            try
            {
                final ResultSet res = response.getResults();
                res.next();
                sessionKey = res.getString("session_key");
            }
            catch (final SQLException ex)
            {
                throw new RuntimeException(ex);
            }
        }
        else
        {
            // We should never get here. The send() method will throw a MocaException if the user supplies invalid creds
            throw new RuntimeException("Login failed");
        }
    }

    /**
     * @return true if logout succeeded, false otherwise
     */
    protected boolean logout()
    {
        if (isClosed)
        {
            throw new IllegalStateException("Connection is closed");
        }
        try
        {
            execute("logout user");
            return true;
        }
        catch (final MocaException ex)
        {
            return false;
        }
        finally
        {
            this.sessionKey = null;
        }
    }

    @Override
    public void close()
    {
        if (!isClosed)
        {
            this.logout();
            isClosed = true;
        }
    }
}
