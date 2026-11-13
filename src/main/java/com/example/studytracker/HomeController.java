package com.example.studytracker;

import com.example.studytracker.model.StudyRecord;
import com.example.studytracker.repository.StudyRecordRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
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

        validateRecord(subject, studyDate, minutes);

        StudyRecord record = new StudyRecord();

        record.setSubject(subject);
        record.setStudyDate(studyDate);
        record.setMinutes(minutes);

        repository.save(record);

        return "redirect:/";
    }
    @PostMapping("/records/{id}/delete")
    public String deleteRecord(@PathVariable Long id) {

        if (repository.existsById(id)) {
            repository.deleteById(id);
        }

        return "redirect:/";
    }
    @GetMapping("/records/{id}/edit")
    public String editRecord(@PathVariable Long id, Model model) {
        StudyRecord record = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "学習記録が見つかりません"));

        model.addAttribute("record", record);
        return "edit";
    }

    @PostMapping("/records/{id}/edit")
    public String updateRecord(
            @PathVariable Long id,
            @RequestParam String subject,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate studyDate,
            @RequestParam Integer minutes) {

        validateRecord(subject, studyDate, minutes);

        StudyRecord record = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "学習記録が見つかりません"));

        record.setSubject(subject);
        record.setStudyDate(studyDate);
        record.setMinutes(minutes);

        repository.save(record);

        return "redirect:/";
    }
    private void validateRecord(
            String subject,
            LocalDate studyDate,
            Integer minutes) {

        if (subject == null || subject.isBlank()
                || subject.length() > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "学習科目が不正です");
        }

        if (studyDate == null || studyDate.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "学習日が不正です");
        }

        if (minutes == null || minutes < 1 || minutes > 1440) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "学習時間が不正です");
        }
    }
}