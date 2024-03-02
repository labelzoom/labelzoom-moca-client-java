package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import static org.junit.jupiter.api.Assertions.*;

public class MocaResultSetTest extends MocaTester
{
    @Test
    public void testIteration() throws MocaException, SQLException
    {
        try (final ResultSet res = conn.execute("publish data where a = 1 and b = 2 & publish data where a = 3 and b = 4"))
        {
            assertFalse(res.previous());

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

            // rowNum == 1
            assertTrue(res.previous());
            assertFalse(res.isBeforeFirst());
            assertFalse(res.isFirst());
            assertTrue(res.isLast());
            assertFalse(res.isAfterLast());
        }
    }

    @Test
    public void testDataTypes() throws MocaException, SQLException
    {
        try (final ResultSet res = conn.execute("publish data" +
                " where intcol = 82332" +
                "   and strcol = 'IOJWExriojomwe'" +
                "   and boolcol = true" +
                "   and floatcol = 819233.389901782"))
        {
            assertTrue(res.next());

            assertEquals(82332, res.getInt("intcol"));
            assertEquals(Types.INTEGER, res.getMetaData().getColumnType(0));

            assertEquals("IOJWExriojomwe", res.getString("strcol"));
            assertEquals(Types.VARCHAR, res.getMetaData().getColumnType(1));

            assertTrue(res.getBoolean("boolcol"));
            assertEquals(Types.BOOLEAN, res.getMetaData().getColumnType(2));

            assertEquals((float) 819233.389901782, res.getFloat("floatcol"));
            assertEquals((double) 819233.389901782, res.getDouble("floatcol"));
            assertEquals(Types.DOUBLE, res.getMetaData().getColumnType(3));

            assertThrows(IndexOutOfBoundsException.class, () -> res.getMetaData().getColumnType(4));
            assertThrows(IndexOutOfBoundsException.class, () -> res.findColumn("invalidcol"));
            assertThrows(IndexOutOfBoundsException.class, () -> res.getString(-1));
            assertThrows(IndexOutOfBoundsException.class, () -> res.getString(4));
        }
    }
}
