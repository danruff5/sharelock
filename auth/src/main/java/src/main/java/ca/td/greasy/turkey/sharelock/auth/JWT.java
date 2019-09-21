package src.main.java.ca.td.greasy.turkey.sharelock.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

public class JWT {

    private static final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    private static String generateToken() {
        Date exp = new Date(System.currentTimeMillis() + (1000 * 30));

        String subject = "users/TzMUocMF4p";
        String name = "Robert Token Man";
        String scope = "self groups/admins";

        String token = Jwts.builder()
                .setSubject(subject)
                .setExpiration(exp)
                .claim("name", name)
                .claim("scope", scope)
                .signWith(secret)
                .compact();

        return token;
    }

    private static void verifyToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(base64SecretBytes)
                .parseClaimsJws(token).getBody();
        System.out.println("----------------------------");
        System.out.println("Subject: " + claims.getSubject());
        System.out.println("Name: " + claims.get("name"));
        System.out.println("Scope: " + claims.get("scope"));
        System.out.println("Expiration: " + claims.getExpiration());
    }

    public static void main(String[] args) {
        System.out.println(generateToken());
        String token = generateToken();
        verifyToken(token);
    }
}
