package com.labelzoom.moca;

/*
RAW:
V103^45^^~0~~0~~-1^^002050^get encryption information
V104^83^2^^^76^^^~0~~0~~-1^USR_ID=RFAUST^001794^check single signon where usr_id = 'RFAUST'
V104^101^2^^^94^^^~0~~0~~-1^USR_ID=RFAUST^001794^login user where usr_id = 'RFAUST' and usr_pswd = 'RFaust123'
V104^426^2^^^418^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^000002^try { list les option authorizations where opt_nam = 'ORBITAL-SERVICES-LEXTEDIT' } catch(-1403) { list les options where opt_nam = 'ORBITAL-SERVICES-LEXTEDIT' } >> res| publish top rows where rows = 1 and res = @res| publish data where desc = @mls_text
V104^223^2^^^215^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^003330^list library versions where category = 'MOCAbase'
V104^210^2^^^202^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^000002^publish data where dbtype = dbtype()

ALIGNED:
V103^45 ^         ^~0~~0~~-1^             ^002050^get encryption information
V104^83 ^2^^^76^^ ^~0~~0~~-1^USR_ID=RFAUST^001794^check single signon where usr_id = 'RFAUST'
V104^101^2^^^94^^ ^~0~~0~~-1^USR_ID=RFAUST^001794^login user where usr_id = 'RFAUST' and usr_pswd = 'RFaust123'
V104^426^2^^^418^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^000002^try { list les option authorizations where opt_nam = 'ORBITAL-SERVICES-LEXTEDIT' } catch(-1403) { list les options where opt_nam = 'ORBITAL-SERVICES-LEXTEDIT' } >> res| publish top rows where rows = 1 and res = @res| publish data where desc = @mls_text
V104^223^2^^^215^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^003330^list library versions where category = 'MOCAbase'
V104^210^2^^^202^^^~0~~0~~-1^USR_ID=RFAUST:SESSION_KEY=;uid=RFAUST|sid=314d9b70-e08e-4a8f-ae19-3090e5ab21ba|dt=kqtyde8v|sec=ALL;vek4REudkhzDSe7quJGKcAjaT0:LOCALE_ID=US_ENGLISH^000002^publish data where dbtype = dbtype()
 */
class LegacyMocaRequest extends MocaRequest
{
    public static final String DELIMITER = "^";
    public LegacyMocaRequest(final String command)
    {
        super(command);
    }

    @Override
    public String toString()
    {
        final int version = 104;
        final StringBuilder sb = new StringBuilder(String.format("V%d^", version));
        switch (version)
        {
            case 103:
                sb.append(getV103Payload());
                break;
            case 104:
                sb.append(getV104Payload());
                break;
        }

        System.out.println(sb.toString());
        return sb.toString();
    }

    private String getV103Payload()
    {
        //V103^45^^~0~~0~~-1^^002050^get encryption information
        // Body
        final String service = "";
        final String traceInfo = "^~0~~0~~-1";
        final String environment = "";
        final String flags = "002050";
        final String body = String.join(DELIMITER, service, traceInfo, environment, flags, command);
        return String.join(DELIMITER, String.valueOf(body.length()), body);
    }

    private String getV104Payload()
    {
        //V104^101^2^^^94^^^~0~~0~~-1^USR_ID=RFAUST^001794^login user where usr_id = 'RFAUST' and usr_pswd = 'RFaust123'

        // Body
        //^^~0~~0~~-1^USR_ID=RFAUST^001794^login user where usr_id = 'RFAUST' and usr_pswd = 'RFaust123'
        final String service = "";
        final String schema = "";
        final String traceInfo = "~0~~0~~-1";
        final String environment = "USR_ID=RFAUST";
        final String flags = "001794";
        String command = this.command;
        if (command.equals("login user"))
        {
            command = "login user where usr_id = 'RFAUST' and usr_pswd = 'RFaust123'";
        }
        final String body = String.join(DELIMITER, service, schema, traceInfo, environment, flags, command);

        // Header
        //2^^^94
        final String idk = "2";
        final String encryptionMethod = "";
        final String encryptionInfo = "";
        final String header = String.join(DELIMITER, idk, encryptionMethod, encryptionInfo, String.valueOf(body.length()));

        // Payload
        final String payload = String.join(DELIMITER, header, body);
        return String.join(DELIMITER, String.valueOf(payload.length()), payload);
    }
}
