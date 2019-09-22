package ca.td.greasy.turkey.sharelock.api;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;   
import java.util.Base64;
import java.util.Date;

public class JWT {

    private static final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final byte[] secretBytes = secret.getEncoded();
    private static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    public static String generateToken(Date expiry, String user, String lockID, String keyId) {
        String token = Jwts.builder()
                .setExpiration(expiry)
                .claim("user", user)
                .claim("lockID", lockID)
                .claim("keyId", keyId)
                .signWith(secret)
                .compact();

        return token;
    }

    public static void verifyToken(String token, Long keyId, Long lockId, Long userId) {
        Claims claims = Jwts.parser()
                .setSigningKey(base64SecretBytes)
                .parseClaimsJws(token).getBody();
        
        keyId = claims.get("keyId", Long.class);
        lockId = claims.get("lockId", Long.class);
        userId = claims.get("userId", Long.class);
        
        System.out.println("----------------------------");
        System.out.println("User: " + userId);
        System.out.println("lockID: " + lockId);
        System.out.println("keyID: " + userId);
        System.out.println("Expiration: " + claims.getExpiration());
        
        
    }

    /*public static void main(String[] args) {
        String token = generateToken(new Date(System.currentTimeMillis() + (1000 * 30)), "users/user1", "lockid123test");
        String token2 = generateToken(new Date(System.currentTimeMillis() + (1000 * 30)), "users/user2", "lockid222test");
        System.out.println(token);
        verifyToken(token2);
    }*/
}