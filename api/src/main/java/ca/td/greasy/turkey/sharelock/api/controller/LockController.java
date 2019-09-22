package ca.td.greasy.turkey.sharelock.api.controller;

import ca.td.greasy.turkey.sharelock.api.JWT;
import ca.td.greasy.turkey.sharelock.api.model.CreateKeyRequest;
import ca.td.greasy.turkey.sharelock.api.model.Key;
import ca.td.greasy.turkey.sharelock.api.model.Lock;
import ca.td.greasy.turkey.sharelock.api.model.LockActionRequest;
import ca.td.greasy.turkey.sharelock.api.model.Status;
import ca.td.greasy.turkey.sharelock.api.model.User;
import ca.td.greasy.turkey.sharelock.api.repository.KeyRepository;
import ca.td.greasy.turkey.sharelock.api.repository.LockRepository;
import ca.td.greasy.turkey.sharelock.api.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LockController {
 
    @Autowired
    private LockRepository lockRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private KeyRepository keyRepository;
    
    @Autowired
    private KeyController keyController;
    
    @PostMapping("users/{userId}/locks")
    public Lock addLock(@RequestBody Lock lock, @PathVariable("userId") Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new Exception("User does not exist.");
        }
        
        lock.setOwner(user.get());
        lockRepository.save(lock);
        
        CreateKeyRequest request = new CreateKeyRequest();
        request.setName(lock.getName() + " Owner Key");
        request.setUserId(userId);
        request.setLockId(lock.getId());
        Calendar futureCal = Calendar.getInstance();
        futureCal.set(Calendar.YEAR, 2024);
        request.setExpiryTime(futureCal.getTimeInMillis());
        
        keyController.createKey(request);
        
        return lock;
    }
    
    @GetMapping("users/{userId}/locks")
    public List<Lock> getLocks(@PathVariable("userId") Long userId) {
        return lockRepository.findByOwnerId(userId);
    }
    
    @GetMapping("locks/{lockId}")
    public Optional<Lock> getLock(@PathVariable("lockId") Long lockId) {
        return lockRepository.findById(lockId);
    }
    
    
    @PostMapping("locks/{lockId}")
    public Lock actionLock(@RequestBody LockActionRequest request, @PathVariable("lockId") Long lockId) throws Exception {
        AtomicLong tokenKey = new AtomicLong(0L), tokenLock = new AtomicLong(0L), tokenUser = new AtomicLong(0L);
        JWT.verifyToken(request.getToken(), tokenKey, tokenLock, tokenUser);
        if (tokenKey.get() == lockId) {
            throw new Exception("Invalid token");
        }
        
        Optional<Lock> lock = lockRepository.findById(lockId);
        if (!lock.isPresent()) {
            throw new Exception("User does not exist.");
        }
        
        Lock l = lock.get();
        if (Status.DISABLED.equals(l.getStatus()) && !Status.LOCKED.equals(request.getStatus())) {
            throw new Exception("Lock is disabled, cannot action it");
        } else {
            Optional<Key> key = keyRepository.findById(tokenKey.get());
            if (!key.isPresent()) {
                throw new Exception("Key does not exist");
            }
            
            l.setStatus(request.getStatus());
            l.setLastAcessed(new Date());
            lockRepository.save(l);
            
            key.get().setLastUsedTime(new Date());
            keyRepository.save(key.get());
        }
        
        return l;
    }
    
    @GetMapping("locks/{lockId}/keys")
    public List<Key> getKeys(@PathVariable("lockId") Long lockId) throws Exception {
        Optional<Lock> lock = lockRepository.findById(lockId);
        if (!lock.isPresent()) {
            throw new Exception("Lock does not exist.");
        }
        
        List<Key> keys = keyRepository.getKeysByLockId(lockId);
        /*Long tokenKey = 0L, tokenLock = 0L, tokenUser = 0L;
        for (int i = 0; i < keys.size(); i++) {
            try {
                JWT.verifyToken(keys.get(i).getToken(), tokenKey, tokenLock, tokenUser);
                keys.get(i).setActive(true);
            } catch (ExpiredJwtException ex) {
                keys.get(i).setActive(false);
            }
        }*/
        
        return keys;
    }
}