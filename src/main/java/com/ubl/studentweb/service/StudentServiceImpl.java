package com.ubl.studentweb.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
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

    private Student map(StudentEntity entity) {
        final Student student = new Student();
        student.setNim(entity.getNim());
        student.setFullName(entity.getFullName());
        student.setAddress(entity.getAddress());
        student.setDateOfBirth(entity.getDateOfBirth());
        return student;

    }

    private StudentEntity map(Student student) {
        final StudentEntity entity = new StudentEntity();
        entity.setNim(student.getNim());
        entity.setFullName(student.getFullName());
        entity.setAddress(student.getAddress());
        entity.setDateOfBirth(student.getDateOfBirth());
        return entity;

    }

    @Override
    public List<Student> getStudents() {
        final List<StudentEntity> entities = studentEntityRepository.findAll();
        return entities.stream()
                .map(this::map)
                .collect(toList());
    }

    @Override
    public Optional<Student> findStudentByNim(String nim) {
        Optional<StudentEntity> optionalStudentEntity = studentEntityRepository.findByNim(nim);
        if (optionalStudentEntity.isPresent()) {
            return Optional.of(this.map(optionalStudentEntity.get()));
        } else {
            return Optional.empty();

        }
    }

    @Override
    public void save(Student student) {
        studentEntityRepository.save(this.map(student));
    }

    @Override
    public void delete(String nim) {
        Optional<StudentEntity> optionalEntity = studentEntityRepository.findByNim(nim);
        if (optionalEntity.isPresent()) {
            studentEntityRepository.delete(optionalEntity.get());
        } else {
            throw new ServiceException("student with nim {0} not found" + nim);
        }

    }

    @Override
    public void update(Student student) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

   
}
