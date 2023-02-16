package com.kayikci.learningplatform;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Iterable<Question> findByExamId(Long examId);

    Optional<Question> findByIdAndExamId(Long id, Long examId);


}
