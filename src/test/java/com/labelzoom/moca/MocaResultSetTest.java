package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class MocaResultSetTest extends MocaTester
{
    @Test
    public void testIteration() throws MocaException, SQLException
    {
        final ResultSet res = conn.execute("publish data where a = 1 and b = 2 & publish data where a = 3 and b = 4");

        // rowNum == -1
        assertTrue(res.isBeforeFirst());
        assertFalse(res.isFirst());
        assertFalse(res.isLast());
        assertFalse(res.isAfterLast());

        // rowNum == 0
        assertTrue(res.next());
        assertFalse(res.isBeforeFirst());
        assertTrue(res.isFirst());
        assertFalse(res.isLast());
        assertFalse(res.isAfterLast());

        // rowNum == 1
        assertTrue(res.next());
        assertFalse(res.isBeforeFirst());
        assertFalse(res.isFirst());
        assertTrue(res.isLast());
        assertFalse(res.isAfterLast());

        // rowNum == 2
        assertFalse(res.next());
        assertFalse(res.isBeforeFirst());
        assertFalse(res.isFirst());
        assertFalse(res.isLast());
        assertTrue(res.isAfterLast());
    }

    @Test
    public void testDataTypes() throws MocaException, SQLException
    {
        final ResultSet res = conn.execute("publish data" +
                " where intcol = 82332" +
                "   and strcol = 'IOJWExriojomwe'" +
                "   and boolcol = true" +
                "   and floatcol = 819233.389901782");

        assertTrue(res.next());
        assertEquals(82332, res.getInt("intcol"));
        assertEquals("IOJWExriojomwe", res.getString("strcol"));
        assertTrue(res.getBoolean("boolcol"));
        assertEquals((float)819233.389901782, res.getFloat("floatcol"));
        assertEquals((double)819233.389901782, res.getDouble("floatcol"));
    }
}
