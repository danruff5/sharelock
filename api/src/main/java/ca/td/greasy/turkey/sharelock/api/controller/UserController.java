package ca.td.greasy.turkey.sharelock.api.controller;

import ca.td.greasy.turkey.sharelock.api.model.User;
import ca.td.greasy.turkey.sharelock.api.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        userRepository.save(user);
        return user;
    }
    
    @GetMapping("/users/{userId}")
    public Optional<User> getUserById(@PathVariable("userId") Long id) {
        return userRepository.findById(id);
    }
    
    @GetMapping("/users/search/{phoneNumber}")
    public Optional<User> getUserByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        return userRepository.findUserByPhoneNumber(phoneNumber);
    }
}
