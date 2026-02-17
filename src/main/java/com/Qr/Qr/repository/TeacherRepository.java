package com.Qr.Qr.repository;

import com.Qr.Qr.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Long> {
    Optional<Teacher> findByUserId(Long userId);
    Optional<Teacher> findById(Long teacherId);
}
