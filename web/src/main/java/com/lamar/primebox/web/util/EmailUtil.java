package com.lamar.primebox.web.util;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.Mac;

public class EmailUtil {

    public static String createActivationUrl(String username, String secretKey) {
        final String encodeHex = createSecretHmacSha256Encoded(username, secretKey);

        final String uri = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost:8080")
                .path("/activate")
                .queryParam("username", username)
                .queryParam("encodedSecret", encodeHex)
                .build()
                .toUriString();
        return uri;
    }

    public static String createSecretHmacSha256Encoded(String secret, String secretKey) {
        final Mac mac = HmacUtils.getInitializedMac(HmacAlgorithms.HMAC_SHA_256, secretKey.getBytes());
        final byte[] macData = mac.doFinal(secret.getBytes());
        return Hex.encodeHexString(macData);
    }

}
