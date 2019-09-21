package ca.td.greasy.turkey.sharelock.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LockController {
    
    @GetMapping("/ping")
    public String ping() {
        return "Hello!!";
    }
}