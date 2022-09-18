package joe.example.controllers;

import joe.example.entity.Example;
import joe.example.service.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Provider;

@RestController
public class ExampleController {
    @Autowired
    private Provider<ExampleService> exampleService;

    @GetMapping("/send")
    public String send() {
        return exampleService.get().sendMessage();
    }

    @GetMapping("/receive")
    public String receive() {
        return exampleService.get().receiveMessage();
    }

    @PostMapping("/callback")
    public Example callback(@RequestBody Example example) {
        exampleService.get().saveExample(example);
        return example;
    }
}