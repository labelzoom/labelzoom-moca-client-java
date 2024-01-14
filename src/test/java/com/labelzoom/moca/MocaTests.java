package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;
import com.labelzoom.moca.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class MocaTests extends MocaTester
{
    @Test
    public void testSyntaxError()
    {
        try
        {
            conn.execute("publish data a = 1");
            fail("Expected exception but none was thrown");
        }
        catch (final MocaException ex)
        {
            System.err.println(ex.getMessage());
            assertEquals(505, ex.getErrorCode());
        }
    }

    @Test
    public void testCommandNotFound()
    {
        try
        {
            conn.execute("this command doesnt exist");
            fail("Expected exception but none was thrown");
        }
        catch (final MocaException ex)
        {
            System.err.println(ex.getMessage());
            assertEquals(501, ex.getErrorCode());
        }
    }

    @Test
    public void testNoRowsAffected() throws SQLException
    {
        try
        {
            conn.execute("[select polcod, polvar, polval from poldat where 1 = 2]");
            fail("Expected exception but none was thrown");
        }
        catch (final MocaException ex)
        {
            System.err.println(ex.getMessage());
            assertEquals(NotFoundException.class, ex.getClass());
            assertEquals(510, ex.getErrorCode());
            assertNotNull(ex.getResults());
            assertEquals(0, ex.getResults().findColumn("polcod"));
            assertEquals(1, ex.getResults().findColumn("polvar"));
            assertEquals(2, ex.getResults().findColumn("polval"));
        }
    }

    @Test
    public void testInvalidColumn() throws SQLException
    {
        try
        {
            conn.execute("[select qw9io0ejoower from poldat where 1 = 2]");
            fail("Expected exception but none was thrown");
        }
        catch (final MocaException ex)
        {
            System.err.println(ex.getMessage());
            assertEquals(511, ex.getErrorCode());
        }
    }
}
