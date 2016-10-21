package com.pes12.pickanevent.business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Legault on 21/10/2016.
 */

public class EncodeUtil {


    private EncodeUtil(){}

    public static String encodePasswordSHA1(String _password)
    {
        String encoded  = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] result = md.digest(_password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }

            encoded = sb.toString();



        } catch (Exception e) {
            e.printStackTrace();
        }

        return encoded;
    }
}
