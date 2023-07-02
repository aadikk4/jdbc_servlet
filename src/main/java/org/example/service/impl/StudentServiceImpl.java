package org.example.service.impl;

import org.example.dao.StudentDao;
import org.example.dao.impl.StudentDaoImpl;
import org.example.model.Student;
import org.example.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDaoImpl;

    public StudentServiceImpl(StudentDao studentDaoImpl) {
        this.studentDaoImpl = studentDaoImpl;
    }
    public StudentServiceImpl() {
        this.studentDaoImpl = new StudentDaoImpl();
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDaoImpl.getAllStudents();
    }

    @Override
    public void addStudent(Student student) {
        studentDaoImpl.addStudent(student);
    }

    @Override
    public void deleteStudentById(long id) {
        studentDaoImpl.deleteStudentById(id);
    }

    @Override
    public void deleteCoursesByStudentId(long studentId) {
        studentDaoImpl.deleteCoursesByStudentId(studentId);
    }
}
