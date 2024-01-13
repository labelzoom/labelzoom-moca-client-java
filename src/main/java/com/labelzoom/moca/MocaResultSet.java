package com.labelzoom.moca;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

class MocaResultSet extends AResultSet
{
    private final MocaResultSetMetaData metadata;
    private final ArrayList<ArrayList<String>> data;

    private int rowNum = -1;

    public MocaResultSet(final Node mocaResults) throws SQLException
    {
        final Node metadata = ((Element)mocaResults).getElementsByTagName("metadata").item(0);
        this.metadata = new MocaResultSetMetaData(metadata);
        final Node dataNode = ((Element) mocaResults).getElementsByTagName("data").item(0);
        final NodeList rows = dataNode.getChildNodes();
        this.data = new ArrayList<>(rows.getLength());
        for (int row = 0; row < rows.getLength(); row++)
        {
            final Node rowNode = rows.item(row);
            final NodeList fields = rowNode.getChildNodes();

            final ArrayList<String> fieldValues = new ArrayList<>(this.metadata.getColumnCount());
            for (int col = 0; col < fields.getLength(); col++)
            {
                final Node fieldNode = fields.item(col);
                fieldValues.add(fieldNode.getTextContent());
            }
            this.data.add(fieldValues);
        }
    }

    @Override
    public int findColumn(final String columnLabel)
    {
        return metadata.getColumnIndex(columnLabel);
    }

    @Override
    public ResultSetMetaData getMetaData()
    {
        return this.metadata;
    }

    @Override
    public boolean next()
    {
        rowNum++; // TODO: Do we want to keep incrementing rowNum even after developer has passed end of result set?
        if (rowNum > data.size() - 1)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean previous()
    {
        if (rowNum <= 0)
        {
            return false;
        }
        rowNum--;
        return true;
    }

    private String getValue(final int columnIndex) throws SQLException
    {
        if (columnIndex < 0 || columnIndex >= getMetaData().getColumnCount())
        {
            throw new IndexOutOfBoundsException();
        }
        return data.get(rowNum).get(columnIndex);
    }

    @Override public boolean getBoolean(final int columnIndex) throws SQLException
    {
        final int type = getMetaData().getColumnType(columnIndex);
        if (type == Types.BIT || type == Types.BOOLEAN)
        {
            return "1".equals(getValue(columnIndex));
        }
        return Boolean.parseBoolean(getValue(columnIndex));
    }
    @Override public boolean getBoolean(final String columnLabel) throws SQLException { return getBoolean(findColumn(columnLabel)); }

    @Override public double getDouble(final int columnIndex) throws SQLException { return Double.parseDouble(getValue(columnIndex)); }
    @Override public double getDouble(final String columnLabel) throws SQLException { return getDouble(findColumn(columnLabel)); }

    @Override public float getFloat(final int columnIndex) throws SQLException { return Float.parseFloat(getValue(columnIndex)); }
    @Override public float getFloat(final String columnLabel) throws SQLException { return getFloat(findColumn(columnLabel)); }

    @Override public int getInt(final int columnIndex) throws SQLException { return Integer.parseInt(getValue(columnIndex)); }
    @Override public int getInt(final String columnLabel) throws SQLException { return getInt(findColumn(columnLabel)); }

    @Override public long getLong(final int columnIndex) throws SQLException { return Long.parseLong(getValue(columnIndex)); }
    @Override public long getLong(final String columnLabel) throws SQLException { return getLong(findColumn(columnLabel)); }

    @Override public short getShort(final int columnIndex) throws SQLException { return Short.parseShort(getValue(columnIndex)); }
    @Override public short getShort(final String columnLabel) throws SQLException { return getShort(findColumn(columnLabel)); }

    @Override public String getString(final int columnIndex) throws SQLException { return getValue(columnIndex); }
    @Override public String getString(final String columnLabel) throws SQLException { return getString(findColumn(columnLabel)); }

    @Override
    public boolean isAfterLast() throws SQLException
    {
        return rowNum >= data.size();
    }

    @Override
    public boolean isBeforeFirst() throws SQLException
    {
        return rowNum < 0;
    }

    @Override
    public boolean isFirst() throws SQLException
    {
        return rowNum == 0;
    }

    @Override
    public boolean isLast() throws SQLException
    {
        return rowNum == data.size() - 1;
    }
}
