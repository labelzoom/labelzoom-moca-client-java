package com.labelzoom.moca;

import com.labelzoom.moca.exceptions.MocaException;
import com.labelzoom.moca.exceptions.MocaExceptionFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;

public class MocaResponse
{
    private final int statusCode;
    private final String message;
    private final ResultSet results;

    private MocaResponse(final int statusCode, final String message, final ResultSet results)
    {
        this.statusCode = statusCode;
        this.message = message;
        this.results = results;
    }

    public static MocaResponse fromXml(final String responseXml) throws MocaException
    {
        int statusCode = 0;
        String message;
        ResultSet results;
        try
        {
            final Document doc = parseXmlString(responseXml);
            final Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals("moca-response"))
            {
                throw new RuntimeException("Invalid XML response");
            }
            statusCode = Integer.parseInt(root.getElementsByTagName("status").item(0).getTextContent());
            if (statusCode != 0)
            {
                message = root.getElementsByTagName("message").item(0).getTextContent();
            }
            else
            {
                message = null;
            }

            // Retrieve result set
            if (root.getElementsByTagName("moca-results").getLength() > 0)
            {
                results = new MocaResultSet(root.getElementsByTagName("moca-results").item(0));
            }
            else
            {
                results = null;
            }
        }
        catch (final Exception ex)
        {
            throw new RuntimeException("Error parsing MOCA response: " + responseXml);
        }

        if (statusCode != 0)
        {
            throw MocaExceptionFactory.produce(message, statusCode, results);
        }

        return new MocaResponse(statusCode, message, results);
    }

    /*
    V104^572^-1^0^0^^1^14^SSSISIDIOOOSOO^~usr_id~0~0~usr_id~usr_id~locale_id~0~0~locale_id~locale_id~addon_id~0~0~addon_id~addon_id~cust_lvl~0~0~cust_lvl~cust_lvl~session_key~0~0~session_key~session_key~pswd_expir~0~0~pswd_expir~pswd_expir~pswd_expir_dte~0~0~pswd_expir_dte~pswd_expir_dte~pswd_disable~0~0~pswd_disable~pswd_disable~pswd_chg_flg~0~0~pswd_chg_flg~pswd_chg_flg~pswd_expir_flg~0~0~pswd_expir_flg~pswd_expir_flg~pswd_warn_flg~0~0~pswd_warn_flg~pswd_warn_flg~srv_typ~0~0~srv_typ~srv_typ~super_usr_flg~0~0~super_usr_flg~super_usr_flg~ext_ath_flg~0~0~ext_ath_flg~ext_ath_flg~^
    193^S6^RFAUSTS10^US_ENGLISHS10^WM,SEAMLESI2^10S99^;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0I0^D0^I4^6008O1^0O1^0O1^0S11^DEVELOPMENTO1^1O1^0

    V104^275^2^^^267^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=5e5a11cb-fede-4c4f-865a-6fe02f680d52|dt=kqu4hu3q|sec=ALL;UuwvnFG2THK1lkvirP376BVNo0:LOCALE_ID=US_ENGLISH:MOCA_APPL_ID=LEXTEDIT^001794^publish data
 where a = 1
   and b = 2 &
publish data
 where a = 3
   and b = 4
    V104^37^-1^0^0^^2^2^II^~a~0~0~a~a~b~0~0~b~b~^
    8^I1^1I1^2
    8^I1^3I1^4
     */
    public static MocaResponse fromLegacy(final String responseText)
    {
        System.out.println(responseText);
        //V104^<header length>^<command count>^<status>^
        // <message length>^<message>^<number rows>^<number colums>^
        // <data types>^<columns encoding>^
        //V104^258^-1^0^0^^1^6^SSSSSS^~name~0~0~name~name~charset~0~0~charset~charset~server_key~0~0~server_key~server_key~client_key_check_mode~0~0~client_key_check_mode~client_key_check_mode~change_version~0~0~change_version~change_version~oidcp_url~0~0~oidcp_url~oidcp_url~^38^S0^S5^UTF-8S0^S7^classicS8^512a95eaS0^
        final String[] parts = responseText.split("\\^");
        if ("V104".equals(parts[0]))
        {
            final int headerLength = Integer.parseInt(parts[1]);
            final int commandCount = Integer.parseInt(parts[2]);
            final int status = Integer.parseInt(parts[3]);
            final int messageLength = Integer.parseInt(parts[4]);
            final String message = parts[5];
            final int numRows = Integer.parseInt(parts[6]);
            final int numCols = Integer.parseInt(parts[7]);
            final String dataTypes = parts[8];
            final String columnsEncoding = parts[9];
            System.out.println("headerLength: " + headerLength);
            System.out.println("commandCount: " + commandCount);
            System.out.println("status: " + status);
            System.out.println("messageLength: " + messageLength);
            System.out.println("message: " + message);
            System.out.println("numRows: " + numRows);
            System.out.println("numCols: " + numCols);
            System.out.println("dataTypes: " + dataTypes);
            System.out.println("columnsEncoding: " + columnsEncoding);

            final String[] columns = columnsEncoding.substring(1, columnsEncoding.length() - 1).split("~");
            if (columns.length % 5 != 0)
            {
                throw new RuntimeException("Invalid columns encoding");
            }
            for (int i = 0; i < columns.length; i += 5)
            {
                System.out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s", dataTypes.charAt(i / 5), columns[i + 0], columns[i + 1], columns[i + 2], columns[i + 3], columns[i + 4]));
            }
        }
        throw new UnsupportedOperationException();
    }

    private static Document parseXmlString(final String xml) throws Exception
    {
        try (final ByteArrayInputStream input = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)))
        {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(input);
        }
    }

    public int getStatusCode()
    {
        return this.statusCode;
    }

    public String getMessage()
    {
        return this.message;
    }

    public ResultSet getResults()
    {
        return this.results;
    }
}
