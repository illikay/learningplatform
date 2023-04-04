package com.kayikci.learningplatform.repository;

import com.kayikci.learningplatform.domain.Exam;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExamRepository extends JpaRepository<Exam, Long> {
}
