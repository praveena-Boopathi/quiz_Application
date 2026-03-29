package com.quizapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Forwards unhandled paths (404) to the Single Page Application index.html
        return "forward:/index.html";
    }
}
