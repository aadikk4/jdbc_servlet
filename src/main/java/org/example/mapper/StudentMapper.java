package org.example.mapper;

import org.example.model.Course;
import org.example.model.Student;
import org.example.model.dto.CourseDTO;
import org.example.model.dto.StudentDTO;

import java.util.ArrayList;
import java.util.List;

public class StudentMapper {
    public StudentDTO toDto(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setDormitoryId(student.getDormitoryId());
        if (student.getCourses() != null && !student.getCourses().isEmpty()) {
            List<CourseDTO> courseDTOS = new ArrayList<>();
            for (Course course : student.getCourses()) {
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setId(course.getId());
                courseDTO.setName(course.getName());
                courseDTOS.add(courseDTO);
            }
            dto.setCourses(courseDTOS);
        }
        return dto;
    }

    public static Student fromDto(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setDormitoryId(dto.getDormitoryId());
        if (dto.getCourses() != null && !dto.getCourses().isEmpty()) {
            List<Course> courses = new ArrayList<>();
            for (CourseDTO courseName : dto.getCourses()) {
                Course course = new Course();
                course.setId(courseName.getId());
                course.setName(courseName.getName());
                courses.add(course);
            }
            student.setCourses(courses);
        }
        return student;
    }
}
