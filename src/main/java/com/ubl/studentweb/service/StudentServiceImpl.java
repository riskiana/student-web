package com.ubl.studentweb.service;

import java.util.List;
import java.util.Optional;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ubl.studentweb.domain.Student;
import com.ubl.studentweb.repository.StudentEntity;
import com.ubl.studentweb.repository.StudentEntityRepository;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService, UserDetailsService {
    private final StudentEntityRepository studentEntityRepository;
    private final PasswordEncoder passwordEncoder;

    // method standard untuk login
    @Override
    public UserDetails loadUserByUsername(String nim) throws UsernameNotFoundException {
        Optional<StudentEntity> optionalEntity = studentEntityRepository.findByNim(nim);
        if (optionalEntity.isPresent()) {
            StudentEntity entity = optionalEntity.get();
            List<GrantedAuthority> authorities = buildAuthority(entity);
            return new org.springframework.security.core.userdetails.User(entity.getNim(),
                    entity.getPassword(),
                    authorities);
        } else {
            throw new UsernameNotFoundException("Invalid nim or password.");
        }
    }

    private List<GrantedAuthority> buildAuthority(StudentEntity studentEntity) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(studentEntity.getRole()));
        return grantedAuthorities;
    }

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
        entity.setPassword(passwordEncoder.encode(student.getPassword()));
        entity.setRole("USER");
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
        // select * from student where nim='21141341'
        Optional<StudentEntity> optionalStudentEntity = studentEntityRepository.findByNim(nim);
        if (optionalStudentEntity.isPresent()) {
            return Optional.of(this.map(optionalStudentEntity.get()));
        } else {
            return Optional.empty();

        }
    }

    @Override
    public void save(Student student) {
        // "insert into student ('nim', 'fullName', 'address', 'dateOfBirth')
        // values ('23232', 'dfsds', 'jakarta', '2012-01-06')"
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
