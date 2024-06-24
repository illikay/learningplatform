package com.kayikci.learningplatform.data;

import com.kayikci.learningplatform.auth.AuthenticationService;
import com.kayikci.learningplatform.auth.RegisterRequest;
import com.kayikci.learningplatform.repository.ExamRepository;
import com.kayikci.learningplatform.repository.QuestionRepository;
import com.kayikci.learningplatform.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class DataLoader {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;


    @PostConstruct
    private void loadData() {


       /* questionRepository.deleteAll();
        examRepository.deleteAll();
        userRepository.deleteAll();*/


        RegisterRequest user1 = new RegisterRequest("vorname1", "nachname1", "asdf@asdf.de", "Ilkay0101!");

        authenticationService.register(user1);




        /*SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        String dateString = format.format(new Date());


        Exam exam1 = new Exam("Prüfung 123", "Info1", "Beschreiung1", dateString, dateString, 12);
        Exam exam2 = new Exam("Exam 1", "asdf", "asdf", dateString, dateString, 12);
        Exam exam3 = new Exam("Exam 2", "qwer", "qwer", dateString, dateString, 12);
        Exam exam4 = new Exam("Exam 3", "hjkl", "hjkl", dateString, dateString, 12);

        exam1.setUser(user1);
        exam2.setUser(user2);
        exam3.setUser(user2)
        exam4.setUser();

        examRepository.save(exam1);
        examRepository.save(exam2);
        examRepository.save(exam3);
        examRepository.save(exam4);


        Question question1 = new Question("questionFrage123", "questionHinweis", "questionLoesung",
                dateString, dateString, true);

        Question question2 = new Question("questionFrageasdf", "questionHinweis2", "questionLoesung2", dateString, dateString, true);

        question1.setExam(exam1);

        question2.setExam(exam1);
        questionRepository.save(question1);
        questionRepository.save(question2);
*/


    }
}
