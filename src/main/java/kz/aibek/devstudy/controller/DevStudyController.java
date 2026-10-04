package kz.aibek.devstudy.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    @GetMapping("/topics")
    public List<String> topics(){
        return List.of("Git","Linux","Docker");
    }
    
}
