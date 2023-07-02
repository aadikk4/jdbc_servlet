package org.example.dao;

import org.example.model.Student;

import java.util.List;

public interface StudentDao {
    List<Student> getAllStudents();
    void addStudent(Student student);
    void deleteStudentById(long id);
    void deleteCoursesByStudentId(long studentId);
}