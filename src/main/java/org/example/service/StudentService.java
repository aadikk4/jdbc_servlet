package org.example.service;

import org.example.model.Course;
import org.example.model.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getAllStudents();

    void addStudent(StudentDTO student);


    void deleteStudentById(long id);

    void deleteCoursesByStudentId(long studentId);
}
