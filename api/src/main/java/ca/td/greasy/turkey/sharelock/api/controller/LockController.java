package ca.td.greasy.turkey.sharelock.api.controller;

import ca.td.greasy.turkey.sharelock.api.model.Lock;
import ca.td.greasy.turkey.sharelock.api.model.LockActionRequest;
import ca.td.greasy.turkey.sharelock.api.model.Status;
import ca.td.greasy.turkey.sharelock.api.model.User;
import ca.td.greasy.turkey.sharelock.api.repository.LockRepository;
import ca.td.greasy.turkey.sharelock.api.repository.UserRepository;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LockController {
 
    @Autowired
    private LockRepository lockRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("users/{userId}/locks")
    public Lock addLock(@RequestBody Lock lock, @PathVariable("userId") Long userId) throws Exception {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new Exception("User does not exist.");
        }
        
        lock.setUser(user.get());
        lockRepository.save(lock);
        return lock;
    }
    
    @GetMapping("users/{userId}/locks")
    public List<Lock> getLocks(@PathVariable("userId") Long userId) {
        return lockRepository.findByUserId(userId);
    }
    
    
    @PostMapping("locks/{lockId}")
    public Lock actionLock(@RequestBody LockActionRequest request, @PathVariable("lockId") Long lockId) throws Exception {
        Optional<Lock> lock = lockRepository.findById(lockId);
        if (!lock.isPresent()) {
            throw new Exception("User does not exist.");
        }
        
        Lock l = lock.get();
        if (Status.DISABLED.equals(l.getStatus())) {
            throw new Exception("Lock is disabled, cannot action it");
        } else {
            l.setStatus(request.getStatus());
            l.setLastAcessed(Calendar.getInstance());
            lockRepository.save(l);
        }
        
        return l;
    }
}