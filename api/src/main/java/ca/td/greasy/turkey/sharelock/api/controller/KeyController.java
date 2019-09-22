package ca.td.greasy.turkey.sharelock.api.controller;

import ca.td.greasy.turkey.sharelock.api.JWT;
import static ca.td.greasy.turkey.sharelock.api.JWT.base64SecretBytes;
import ca.td.greasy.turkey.sharelock.api.model.CreateKeyRequest;
import ca.td.greasy.turkey.sharelock.api.model.Key;
import ca.td.greasy.turkey.sharelock.api.model.Lock;
import ca.td.greasy.turkey.sharelock.api.model.UpdateKeyRequest;
import ca.td.greasy.turkey.sharelock.api.model.User;
import ca.td.greasy.turkey.sharelock.api.repository.KeyRepository;
import ca.td.greasy.turkey.sharelock.api.repository.LockRepository;
import ca.td.greasy.turkey.sharelock.api.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class KeyController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private LockRepository lockRepository;
    
    @Autowired
    private KeyRepository keyRepository;

    
    @PostMapping("keys")
    public Key createKey(@RequestBody CreateKeyRequest request) throws Exception {
        Key key = new Key();
        key.setName(request.getName());
        
        Optional<User> user = userRepository.findById(request.getUserId());
        if (!user.isPresent()) {
            throw new Exception("User does not exist");
        }
        key.setUser(user.get());
        
        Optional<Lock> lock = lockRepository.findById(request.getLockId());
        if (!lock.isPresent()) {
            throw new Exception("Lock does not exist");
        }
        key.setOwnerName(lock.get().getOwner().getFirstName());
        key.setLock(lock.get());
        
        key.setExpiryTime(new Date(request.getExpiryTime()));
        keyRepository.save(key);
        
        String token = JWT.generateToken(
                key.getExpiryTime(), 
                request.getUserId().toString(), 
                request.getLockId().toString(),
                key.getId().toString()
        );
        key.setToken(token);
        key.setActive(true);
        keyRepository.save(key);
        
        return key;
    }
    
    @GetMapping("keys/{userId}")
    public List<Key> getKeys(@PathVariable("userId") Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new Exception("User does not exist"); 
        }
        AtomicLong tokenKey = new AtomicLong(0L), tokenLock = new AtomicLong(0L), tokenUser = new AtomicLong(0L);
        List<Key> keys = keyRepository.getKeysByUserId(userId);
        for (int i = 0; i < keys.size(); i++) {
            try {
                JWT.verifyToken(keys.get(i).getToken(), tokenKey, tokenLock, tokenUser);
                keys.get(i).setActive(true);
            } catch (ExpiredJwtException ex) {
                keys.get(i).setActive(false);
            }
            keyRepository.save(keys.get(i));
        }
        
        return keys;
    }
    
    @PutMapping("keys/{keyId}")
    public Key updateKey(@RequestBody UpdateKeyRequest request, @PathVariable Long keyId) throws Exception {
        Optional<Key> key = keyRepository.findById(keyId);
        if (!key.isPresent()) {
            throw new Exception("Key does not exist.");
        }
        
        Key k = key.get();
        if (request.getName() != null) {
            k.setName(request.getName());
        }
        k.setExpiryTime(new Date(request.getNewExpiry()));
        String token = JWT.generateToken(
                k.getExpiryTime(), 
                k.getUser().getId().toString(),
                k.getLock().getId().toString(),
                k.getId().toString()
        );
        k.setToken(token);
        k.setActive(true);
        keyRepository.save(k);
        
        return k;
    }
    
    @GetMapping("keys/{keyId}/revoke")
    public void revokeKey(@PathVariable Long keyId) throws Exception {
        Optional<Key> key = keyRepository.findById(keyId);
        if (!key.isPresent()) {
            throw new Exception("Key does not exist");
        }
        
        keyRepository.delete(key.get());
    }
}
