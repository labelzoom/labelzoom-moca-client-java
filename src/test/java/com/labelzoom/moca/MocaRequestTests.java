package com.labelzoom.moca;

import org.junit.jupiter.api.Test;

class MocaRequestTests
{
    @Test
    void testMocaResultXml()
    {
        final MocaRequest request = MocaRequestBuilder.http().command("login user where usr_id = 'RFAUST' and usr_paswd = 'RFaust123'");
        System.out.println(request.toString());
    }
    @Test
    void testMocaResultLegacy()
    {
        final MocaRequest request = MocaRequestBuilder.legacy().command("login user where usr_id = 'RFAUST' and usr_paswd = 'RFaust123'");
        System.out.println(request.toString());
    }
}
