package com.kayikci.learningplatform;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findByExamId(Long examId, Pageable pageable);
    Optional<Question> findByIdAndExamId(Long id, Long examId);


}
