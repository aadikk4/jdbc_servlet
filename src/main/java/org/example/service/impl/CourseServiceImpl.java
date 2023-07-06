package org.example.service.impl;


import org.example.dao.CourseDao;
import org.example.dao.impl.CourseDaoImpl;
import org.example.mapper.CourseMapper;
import org.example.model.Course;
import org.example.model.dto.CourseDTO;
import org.example.service.CourseService;

import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDaoImpl;
    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseDao courseDaoImpl, CourseMapper courseMapper) {
        this.courseDaoImpl = courseDaoImpl;
        this.courseMapper = courseMapper;
    }
    public CourseServiceImpl() {
        this.courseMapper = new CourseMapper();
        this.courseDaoImpl = new CourseDaoImpl();
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseDaoImpl.getAllCourses();
        return courses.stream().map(CourseMapper::toDto).toList();
    }

    @Override
    public CourseDTO getCourseByName(String courseName) {
        Course course = courseDaoImpl.getCourseByName(courseName);
        return CourseMapper.toDto(course);
    }

    @Override
    public void addCourse(CourseDTO courseDTO) {
        Course course = CourseMapper.fromDto(courseDTO);
        courseDaoImpl.addCourse(course);
    }

    @Override
    public void deleteCourseById(long id) {
        courseDaoImpl.deleteCourseById(id);
    }

    @Override
    public void deleteStudentsByCourseId(long courseId) {
        courseDaoImpl.deleteStudentsByCourseId(courseId);
    }
}
