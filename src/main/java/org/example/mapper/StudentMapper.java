package org.example.mapper;

import org.example.model.Student;
import org.example.model.dto.StudentDTO;

public class StudentMapper {
    public static StudentDTO toDto(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
//        dto.setCourses(student.getCourses());
        dto.setDormitoryId(student.getDormitoryId());
        return dto;
    }

    public static Student fromDto(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
//        student.setCourses(dto.getCourses());
        student.setDormitoryId(dto.getDormitoryId());
        return student;
    }
}
