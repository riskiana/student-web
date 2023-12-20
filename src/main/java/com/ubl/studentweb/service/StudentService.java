package com.ubl.studentweb.service;

import com.ubl.studentweb.domain.Student;
import java.util.*;

public interface StudentService {

    List<Student> getStudents();

    Optional<Student> findStudentByNim(final String nim);

    void save(Student student);

    void delete(final String nim);

    void update(final Student student);

}
