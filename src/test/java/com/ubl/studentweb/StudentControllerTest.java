package com.ubl.studentweb;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.ubl.studentweb.rest.StudentController;
import com.ubl.studentweb.service.StudentService;

@RunWith(MockitoJUnitRunner.class)
public class StudentControllerTest {
   
    @Mock
    StudentService studentService;
     StudentController studentController = new StudentController(studentService);

    @Test
    public void age_before_18_should_failed() {
        String errorString = studentController.validateDateOfBirth(
            LocalDate.of(2015, 10, 5));
        System.out.println("error"+ errorString);
        assertEquals("minimum age is 16", errorString);

    }

}
