package com.ubl.studentweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;



@Repository
public interface StudentEntityRepository extends JpaRepository<StudentEntity, Long> {

    Optional<StudentEntity> findByNim(final String nim);

     Optional<StudentEntity> findByFullName(final String fullName);

}
