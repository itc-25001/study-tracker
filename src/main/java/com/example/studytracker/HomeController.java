package com.example.studytracker;

import com.example.studytracker.repository.StudyRecordRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.studytracker.model.StudyRecord;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

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
    @PostMapping("/records")
    public String addRecord(
            @RequestParam String subject,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate studyDate,
            @RequestParam Integer minutes) {

        StudyRecord record = new StudyRecord();

        record.setSubject(subject);
        record.setStudyDate(studyDate);
        record.setMinutes(minutes);

        repository.save(record);

        return "redirect:/";
    }
}