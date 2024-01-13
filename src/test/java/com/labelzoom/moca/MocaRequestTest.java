package com.labelzoom.moca;

import org.junit.jupiter.api.Test;

public class MocaRequestTest
{
    @Test
    public void testMocaResultXml()
    {
        final MocaRequest request = MocaRequestBuilder.http().command("login user where usr_id = 'RFAUST' and usr_paswd = 'RFaust123'");
        System.out.println(request.toString());
    }
    @Test
    public void testMocaResultLegacy()
    {
        final MocaRequest request = MocaRequestBuilder.legacy().command("login user where usr_id = 'RFAUST' and usr_paswd = 'RFaust123'");
        System.out.println(request.toString());
    }
}
