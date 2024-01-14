package com.labelzoom.moca;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

class MocaResultSetMetaData extends AbstractResultSetMetaData
{
    private final ArrayList<MocaMetaData> columns = new ArrayList<>();
    private final Map<String, Integer> columnIndex = new TreeMap<>();

    public MocaResultSetMetaData(final Node metaDataNode)
    {
        final Element element = (Element) metaDataNode;
        final NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++)
        {
            final Node column = nodes.item(i);
            if (column.getNodeType() == Node.ELEMENT_NODE)
            {
                final MocaMetaData metaData = new MocaMetaData((Element)column);
                columns.add(metaData);
                columnIndex.put(metaData.getColumnName(), i);
            }
        }
    }

    private static class MocaMetaData
    {
        private final String columnName;
        private final char type;
        private final int length;
        private final boolean nullable;

        public MocaMetaData(final Element element)
        {
            this.columnName = element.getAttribute("name");
            this.type = element.getAttribute("type").charAt(0);
            this.length = Integer.parseInt(element.getAttribute("length"));
            this.nullable = Boolean.parseBoolean(element.getAttribute("nullable"));
        }

        public String getColumnName() { return this.columnName; }
        public char getType() { return this.type; }
        public int getLength() { return this.length; }
        public boolean isNullable() { return this.nullable; }
    }

    @Override
    public int getColumnCount() { return this.columns.size(); }

    private MocaMetaData getColumn(final int columnIndex)
    {
        if (columnIndex < 0 || columnIndex >= getColumnCount())
        {
            throw new ArrayIndexOutOfBoundsException();
        }

        return columns.get(columnIndex);
    }

    @Override
    public String getColumnName(final int columnIndex)
    {
        return getColumn(columnIndex).getColumnName();
    }

    /**
     *
     * @param columnIndex
     * @return
     */
    @Override
    public int getColumnType(final int columnIndex)
    {
        char type = getColumn(columnIndex).getType();
        switch (type)
        {
            case 'O':
                return Types.BOOLEAN;
            case 'I':
            case 'P':
                return Types.INTEGER;
            case 'F':
            case 'X':
                return Types.DOUBLE;
            case 'D':
                return Types.TIMESTAMP;
            case 'S':
            case 'Z':
                return Types.VARCHAR;
            case 'V':
                return Types.BINARY;
            case 'R':
            case 'J':
            case 'G':
            case '?':
                return Types.OTHER;
        }
        throw new RuntimeException("Unknown column type");
    }

    @Override
    public int isNullable(final int columnIndex)
    {
        return getColumn(columnIndex).isNullable() ? columnNullable : columnNoNulls;
    }

    public int getColumnIndex(final String columnName)
    {
        final Integer colNum = columnIndex.get(columnName);
        if (colNum == null)
        {
            throw new IndexOutOfBoundsException();
        }
        return colNum;
    }
}
