package kz.aibek.devstudy.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DevStudyController {

    @GetMapping("/")
    public String home(){
        return "DevStudy API";
    }

    @GetMapping("/healthz")
    public String health(){
        return "ok";
    }
    
}
