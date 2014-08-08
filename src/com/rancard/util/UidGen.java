package com.rancard.util;

import java.util.UUID;

/**
 * Created by Mustee on 2/9/14.
 */
public class UidGen {
    public static String generateSecureUID () {
        UUID id = UUID.randomUUID();
        return id.toString ();
    }

    public static String generateAirtelTransactionId(){
        return Long.toString(UUID.randomUUID().getMostSignificantBits());
    }
}
