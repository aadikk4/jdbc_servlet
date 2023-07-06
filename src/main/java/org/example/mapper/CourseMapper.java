package org.example.mapper;

import org.example.model.Course;
import org.example.model.Student;
import org.example.model.dto.CourseDTO;
import org.example.model.dto.StudentDTO;

import java.util.ArrayList;
import java.util.List;

public class CourseMapper {
    public static CourseDTO toDto(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        if (course.getStudents() != null && !course.getStudents().isEmpty()) {
            List<StudentDTO> studentDTOS = new ArrayList<>();
            for (Student student : course.getStudents()) {
                StudentDTO studentDTO = new StudentDTO();
                studentDTO.setId(student.getId());
                studentDTO.setName(student.getName());
                studentDTOS.add(studentDTO);
            }
            dto.setStudents(studentDTOS);
        }
        return dto;
    }

    public static Course fromDto(CourseDTO dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        if (dto.getStudents() != null && !dto.getStudents().isEmpty()) {
            List<Student> students = new ArrayList<>();
            for (StudentDTO studentName : dto.getStudents()) {
                Student student = new Student();
                student.setId(studentName.getId());
                student.setName(studentName.getName());
                students.add(student);
            }
            course.setStudents(students);
        }
        return course;
    }
}
