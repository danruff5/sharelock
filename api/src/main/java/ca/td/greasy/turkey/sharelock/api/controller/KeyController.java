package ca.td.greasy.turkey.sharelock.api.controller;

import ca.td.greasy.turkey.sharelock.api.JWT;
import ca.td.greasy.turkey.sharelock.api.model.CreateKeyRequest;
import ca.td.greasy.turkey.sharelock.api.model.Key;
import ca.td.greasy.turkey.sharelock.api.model.Lock;
import ca.td.greasy.turkey.sharelock.api.model.User;
import ca.td.greasy.turkey.sharelock.api.repository.KeyRepository;
import ca.td.greasy.turkey.sharelock.api.repository.LockRepository;
import ca.td.greasy.turkey.sharelock.api.repository.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
        key.setLock(lock.get());
        
        key.setExpiryTime(new Date(request.getExpiryTime()));
        String token = JWT.generateToken(
                key.getExpiryTime(), 
                request.getUserId().toString(), 
                request.getLockId().toString()
        );
        key.setToken(token);
        
        keyRepository.save(key);
        return key;
    }
    
    @GetMapping("keys/{userId}")
    public List<Key> getKeys(@PathVariable("userId") Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new Exception("User does not exist"); 
        }
        
        return keyRepository.getKeysByUserId(userId);
    }
}
