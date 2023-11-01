package com.ubl.studentweb.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ubl.studentweb.domain.Student;

@Service
public class StudentService{

    public static Map<String, Student> studentMap = new HashMap<>();

    public List<Student> getStudents(){
        return studentMap.values().stream().toList();
    }
}