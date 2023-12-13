package com.ubl.studentweb;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StudentController {

    public static Map<String, Student> studentMap = new HashMap<>();

    @GetMapping("/students")
    public String getStudents(Model model) {
        model.addAttribute("students", fetchStudents());
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
        boolean exists = studentMap.values().stream()
                .anyMatch(data -> nim.equals(data.getNim()));

        if (exists) {
            throw new IllegalArgumentException("student with nim:" + nim + "is already exist");
        }
        studentMap.put(nim, student);
        model.addAttribute("students", fetchStudents());
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
        final Student student = studentMap.get(nim);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    private static List<Student> fetchStudents() {
        return studentMap.values().stream().toList();
    }

    @PostMapping(value = "/students/{nim}")
    public String updateStudent(@PathVariable("nim") String nim,
            Student student,
            BindingResult result, Model model) {
        final Student studentToBeUpdated = studentMap.get(student.getNim());
        studentToBeUpdated.setAddress(student.getAddress());
        studentToBeUpdated.setDateOfBirth(student.getDateOfBirth());
        studentToBeUpdated.setFullName(student.getFullName());
        studentMap.put(student.getNim(), studentToBeUpdated);

        model.addAttribute("students", fetchStudents());
        return "redirect:/students";
    }

    @GetMapping("/edit/{nim}")
    public String showUpdateForm(@PathVariable("nim") String nim, Model model) {
        final Student studentToBeUpdated = studentMap.get(nim);
        if (studentToBeUpdated == null) {
            throw new IllegalArgumentException("student with nim:" + nim + "is not found");
        }
        model.addAttribute("student", studentToBeUpdated);
        return "update-student";
    }

    @GetMapping(value = "/students/{nim}/delete")
    public String deleteStudent(@PathVariable("nim") String nim) {
        studentMap.remove(nim);
        return "redirect:/students";
    }
}
