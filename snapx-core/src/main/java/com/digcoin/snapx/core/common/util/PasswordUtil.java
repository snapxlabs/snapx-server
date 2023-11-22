package com.digcoin.snapx.core.common.util;

import org.apache.commons.codec.binary.Base64;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {

    public static String getEncryption(byte[] input) {
        String base64String = Base64.encodeBase64String(input);
        return BCrypt.hashpw(base64String, BCrypt.gensalt());
    }

    public static boolean match(String encryption, byte[] input) {
        String base64String = Base64.encodeBase64String(input);
        return BCrypt.checkpw(base64String, encryption);
    }

}
