package com.ubl.studentweb;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.ubl.studentweb.domain.Student;

@Controller
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
    public String addStudent(Student student, Model model) {
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
