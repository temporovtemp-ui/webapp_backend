package com.webserver.utils;

import org.json.JSONObject;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class JwtManager {
    private static final String SECRET_KEY = "m8r04bphpIN4F4Rs7Y9yj0hTSMbO4GSlHdybfDMcx0J";
    private static final String ALGORITHM = "HmacSHA256";

    public static String generateToken(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user must not be null");
        }

        JSONObject header = new JSONObject();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        JSONObject payload = new JSONObject();
        long iat = Instant.now().getEpochSecond();
        long exp = iat + 60 * 60 * 24;
        payload.put("username", user.getUsername());
        payload.put("iat", iat);
        payload.put("exp", exp);

        String encodedHeader = base64UrlEncode(header.toString());
        String encodedPayload = base64UrlEncode(payload.toString());

        String signingInput = encodedHeader + "." + encodedPayload;
        String signature = createSignature(signingInput);

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    private static String createSignature(String data) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            mac.init(secretKeySpec);
            byte[] signatureBytes = mac.doFinal(data.getBytes());
            return base64UrlEncodeBytes(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to create JWT signature", e);
        }
    }

    private static String base64UrlEncode(String data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data.getBytes());
    }

    private static String base64UrlEncodeBytes(byte[] data) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(data);
    }

    public static boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return false;
            }

            String header = parts[0];
            String payload = parts[1];
            String providedSignature = parts[2];

            String signingInput = header + "." + payload;
            String expectedSignature = createSignature(signingInput);

            return providedSignature.equals(expectedSignature);
        } catch (Exception e) {
            return false;
        }
    }

    public static JSONObject getPayload(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT format");
            }

            String payloadBase64 = parts[1];
            byte[] payloadBytes = Base64.getUrlDecoder().decode(payloadBase64);
            String payloadJson = new String(payloadBytes);

            return new JSONObject(payloadJson);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT payload", e);
        }
    }
}