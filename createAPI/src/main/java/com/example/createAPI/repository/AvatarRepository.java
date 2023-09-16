package com.example.createAPI.repository;

import com.example.createAPI.model.Avatar;
import com.example.createAPI.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    Optional<Avatar> findByStudent(Student student);
}
