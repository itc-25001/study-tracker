package com.example.studytracker.repository;

import com.example.studytracker.model.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRecordRepository
        extends JpaRepository<StudyRecord, Long> {
}
