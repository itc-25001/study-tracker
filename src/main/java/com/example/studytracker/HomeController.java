package com.example.studytracker;

import com.example.studytracker.repository.StudyRecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final StudyRecordRepository repository;

    public HomeController(StudyRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("records", repository.findAll());
        return "index";
    }
}