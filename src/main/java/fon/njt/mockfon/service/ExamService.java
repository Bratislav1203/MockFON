package fon.njt.mockfon.service;

import fon.njt.mockfon.dto.ExamDto;
import fon.njt.mockfon.model.Exam;
import fon.njt.mockfon.model.ExamSubject;
import fon.njt.mockfon.model.User;
import fon.njt.mockfon.repository.ExamRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;


@Service
@AllArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    // Method to retrieve all exams
    @Transactional(readOnly = true)
    public List<Exam> findAllExams() {
        return examRepository.findAll();
    }

    // Method to find an exam by ID
    @Transactional(readOnly = true)
    public Exam findExamById(Long id) {
        return examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
    }

    @Transactional
    public Exam createExam(ExamDto examDto) {
        Exam exam = new Exam();
        exam.setExamSubject(ExamSubject.fromDisplayName(String.valueOf(examDto.getSubject())));
        try {
            LocalDate date = LocalDate.parse(examDto.getDate());
            LocalTime time = LocalTime.parse(examDto.getTime());
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            exam.setDateAndTime(dateTime); // Postavljanje LocalDateTime-a direktno
        } catch (DateTimeParseException e) {
            System.err.println("Neispravan format datuma ili vremena: " + e.getMessage());
        }
        exam.setRegistrationStart(LocalDate.parse(examDto.getRegistrationStart()));
        exam.setRegistrationEnd(LocalDate.parse(examDto.getRegistrationEnd()));
        try {
            exam.setPrice(Integer.parseInt(String.valueOf(examDto.getPrice())));
        } catch (NumberFormatException e) {
            System.err.println("Neispravan format cene: " + e.getMessage());
            exam.setPrice(0);
        }
        return examRepository.save(exam);
    }





    // Method to update an existing exam
    @Transactional
    public Exam updateExam(Long id, ExamDto examDto) {
//        Exam exam = examRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Exam not found"));
//        // Update fields
//        exam.setExamSubject(examDto.getExamSubject());
//        exam.setDateAndTime(examDto.getDateAndTime());
//        exam.setRegistrationStart(examDto.getRegistrationStart());
//        exam.setRegistrationEnd(examDto.getRegistrationEnd());
//        return examRepository.save(exam);
        return null;
    }

    // Method to delete an exam
    @Transactional
    public void deleteExam(Long id) {
        if (!examRepository.existsById(id)) {
            throw new RuntimeException("Exam not found");
        }
        examRepository.deleteById(id);
    }
}

