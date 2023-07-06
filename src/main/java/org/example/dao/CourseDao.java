package org.example.dao;

import org.example.model.Course;
import org.example.model.dto.StudentDTO;

import java.sql.SQLException;
import java.util.List;

public interface CourseDao {
    List<Course> getAllCourses();

    Course getCourseByName(String courseName);

    void addCourse(Course course);

    void deleteCourseById(long id);

    void deleteStudentsByCourseId(long courseId);
}
