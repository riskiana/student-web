package com.ubl.studentweb.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ubl.studentweb.domain.Student;
import com.ubl.studentweb.repository.StudentEntity;
import com.ubl.studentweb.repository.StudentEntityRepository;
import static java.util.stream.Collectors.toList;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    private final StudentEntityRepository studentEntityRepository;

    @Override
    public List<Student> getStudents() {
        final List<StudentEntity> entities = studentEntityRepository.findAll();
        return entities.stream()
                .map(this::map)
                .collect(toList());

    }

    private Student map(StudentEntity entity){
        return new Student()
        .setNim(entity.getNim())
        .setFullName(entity.getFullName())
        .setAddress(entity.getAddress())
        .setDateOfBirth(entity.getDateOfBirth());
      
    }

}
