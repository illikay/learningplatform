package com.kayikci.learningplatform.repository;

import com.kayikci.learningplatform.domain.Exam;
import com.kayikci.learningplatform.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ExamRepository extends JpaRepository<Exam, Long> {
    Iterable<Exam> findByUserId(Long userId);

    Optional<Exam> findByIdAndUserId(Long id, Long userId);
}
