package org.example.service;

import org.example.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    void addStudent(Student student);
    void deleteStudentById(long id);
    void deleteCoursesByStudentId(long studentId);
}
