package my.example.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model m) {
        m.addAttribute("someAttribute", "Hello world!");
        return "index";
    }
}
