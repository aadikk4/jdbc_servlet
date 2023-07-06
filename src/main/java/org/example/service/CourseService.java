package org.example.service;

import org.example.model.Course;
import org.example.model.dto.CourseDTO;
import org.example.model.dto.StudentDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    CourseDTO getCourseByName(String courseName);
    void addCourse(CourseDTO course);
    void deleteCourseById(long id);
    void deleteStudentsByCourseId(long courseId);
    }
