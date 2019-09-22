package ca.td.greasy.turkey.sharelock.api;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.security.Key;   
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

public class JWT {

    public static final Key secret = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static final byte[] secretBytes = secret.getEncoded();
    public static final String base64SecretBytes = Base64.getEncoder().encodeToString(secretBytes);

    public static String generateToken(Date expiry, String user, String lockID, String keyId) {
        String token = Jwts.builder()
                .setExpiration(expiry)
                .claim("userId", user)
                .claim("lockId", lockID)
                .claim("keyId", keyId)
                .signWith(secret)
                .compact();

        return token;
    }

    public static void verifyToken(String token, AtomicLong keyId, AtomicLong lockId, AtomicLong userId) {
        Claims claims = Jwts.parser()
                .setSigningKey(base64SecretBytes)
                .parseClaimsJws(token).getBody();
        
        keyId.set(Long.parseLong(claims.get("keyId", String.class)));
        lockId.set(Long.parseLong(claims.get("lockId", String.class)));
        userId.set(Long.parseLong(claims.get("userId", String.class)));
        
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