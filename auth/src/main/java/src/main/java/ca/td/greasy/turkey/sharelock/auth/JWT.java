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

    private static String generateToken(Date expiry, String user, String lockID) {
        String token = Jwts.builder()
                .setExpiration(expiry)
                .claim("user", user)
                .claim("lockID", lockID)
                .signWith(secret)
                .compact();

        return token;
    }

    private static void verifyToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(base64SecretBytes)
                .parseClaimsJws(token).getBody();
        System.out.println("----------------------------");
        System.out.println("User: " + claims.get("user"));
        System.out.println("lockID: " + claims.get("lockID"));
        System.out.println("Expiration: " + claims.getExpiration());
    }

    public static void main(String[] args) {
        String token = generateToken(new Date(System.currentTimeMillis() + (1000 * 30)), "users/user1", "lockid123test");
        String token2 = generateToken(new Date(System.currentTimeMillis() + (1000 * 30)), "users/user2", "lockid222test");
        System.out.println(token);
        verifyToken(token2);
    }
}
