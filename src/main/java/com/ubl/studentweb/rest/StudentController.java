package com.ubl.studentweb.rest;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.ubl.studentweb.domain.Student;
import com.ubl.studentweb.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j(topic = "student-logger")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/students")
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.getStudents());
        return "index";
    }

    @GetMapping("/signup")
    public String showSignUpForm(Student student) {
        return "add-student";
    }

    @PostMapping("/students")
    public String addStudent(@Valid Student student, BindingResult bindingResult, Model model) {
        log.debug("nim {}, fullname {}", student.getNim(), student.getFullName());
        String errorDateOfBirth = validateDateOfBirth(student);

        log.info("errordateOfBirth {}", errorDateOfBirth);
        if (errorDateOfBirth != null) {
            ObjectError error = new ObjectError("globalError", errorDateOfBirth);
            bindingResult.addError(error);
        }

        log.info("bindingResult {}", bindingResult);

        if (bindingResult.hasErrors()) {
            return "add-student";
        }

        String nim = student.getNim();

        boolean exists = studentService.findStudentByNim(nim).isPresent();

        if (exists) {
            throw new IllegalArgumentException("student with nim:" + nim + "is already exist");
        }

        studentService.save(student);

        model.addAttribute("students", studentService.getStudents());
        return "index";
    }

    private String validateDateOfBirth(Student student) {
        String errString = null;
        if (student.getDateOfBirth() != null) {
            log.info("validate date of birth");
            LocalDate dateOfBirth = convertToLocalDateViaSqlDate(student.getDateOfBirth());
            LocalDate currentTime = LocalDate.now();
            int age = Period.between(dateOfBirth, currentTime).getYears();

            if (age < 12) {
                errString = "minimum age is 12. your age: " + age;
            }
        }

        return errString;
    }

    public LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    @GetMapping(value = "/students/{nim}")
    public ResponseEntity<Student> findStudent(@PathVariable("nim") String nim) {
        Optional<Student> studentOptional = studentService.findStudentByNim(nim);
        if (studentOptional.isPresent()) {
            return new ResponseEntity<>(studentOptional.get(), HttpStatus.OK);
        } else {
            return null;
        }

    }

    @PostMapping(value = "/students/{nim}")
    public String updateStudent(@PathVariable("nim") String nim,
            Student student,
            BindingResult result, Model model) {

        final Optional<Student> optionalStudent = studentService.findStudentByNim(student.getNim());
        if (optionalStudent.isEmpty()) {
            throw new ServiceException("student with nim:" + nim + "is not exists");
        }

        studentService.update(student);

        model.addAttribute("students", studentService.getStudents());
        return "redirect:/students";
    }

    @GetMapping("/edit/{nim}")
    public String showUpdateForm(@PathVariable("nim") String nim, Model model) {
        final Optional<Student> optionalStudent = studentService.findStudentByNim(nim);
        if (optionalStudent.isEmpty()) {
            throw new ServiceException("student with nim:" + nim + "is not exists");
        }
        final Student studentToBeUpdated = optionalStudent.get();

        model.addAttribute("student", studentToBeUpdated);
        return "update-student";
    }

    @GetMapping(value = "/students/{nim}/delete")
    public String deleteStudent(@PathVariable("nim") String nim) {
        studentService.delete(nim);
        return "redirect:/students";
    }
}
