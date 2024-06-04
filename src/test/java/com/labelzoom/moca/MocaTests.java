package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;
import com.labelzoom.moca.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MocaTests extends MocaTester
{
    @Test
    void testSyntaxError()
    {
        final MocaException ex = assertThrows(MocaException.class, () -> conn.execute("publish data a = 1"));
        System.err.println(ex.getMessage());
        assertEquals(505, ex.getErrorCode());
    }

    @Test
    void testCommandNotFound()
    {
        final MocaException ex = assertThrows(MocaException.class, () -> conn.execute("this command doesnt exist"));
        System.err.println(ex.getMessage());
        assertEquals(501, ex.getErrorCode());
    }

    @Test
    void testNoRowsAffected() throws SQLException
    {
        final MocaException ex = assertThrows(MocaException.class, () -> conn.execute("[select polcod, polvar, polval from poldat where 1 = 2]"));
        System.err.println(ex.getMessage());
        assertEquals(NotFoundException.class, ex.getClass());
        assertEquals(510, ex.getErrorCode());
        assertNotNull(ex.getResults());
        assertEquals(0, ex.getResults().findColumn("polcod"));
        assertEquals(1, ex.getResults().findColumn("polvar"));
        assertEquals(2, ex.getResults().findColumn("polval"));
    }

    @Test
    void testInvalidColumn()
    {
        final MocaException ex = assertThrows(MocaException.class, () -> conn.execute("[select qw9io0ejoower from poldat where 1 = 2]"));
        System.err.println(ex.getMessage());
        assertEquals(511, ex.getErrorCode());
    }
}
