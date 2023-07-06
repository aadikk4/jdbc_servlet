package org.example.service.impl;

import org.example.dao.StudentDao;
import org.example.dao.impl.StudentDaoImpl;
import org.example.mapper.StudentMapper;
import org.example.model.Student;
import org.example.model.dto.StudentDTO;
import org.example.service.StudentService;

import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDaoImpl;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentDao studentDaoImpl, StudentMapper studentMapper) {
        this.studentDaoImpl = studentDaoImpl;
        this.studentMapper = new StudentMapper();
    }
    public StudentServiceImpl() {
        this.studentMapper = new StudentMapper();
        this.studentDaoImpl = new StudentDaoImpl();
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentDaoImpl.getAllStudents();
        return students.stream().map(studentMapper::toDto).toList();
    }

    @Override
    public void addStudent(StudentDTO studentDTO) {
        Student student = StudentMapper.fromDto(studentDTO);
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
