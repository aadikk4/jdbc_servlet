package org.example.dao.impl;

import org.example.dao.CourseDao;
import org.example.model.Course;
import org.example.model.Student;
import org.example.model.dto.CourseDTO;
import org.example.model.dto.StudentDTO;
import org.example.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.example.util.DbUtil.getConnection;

public class CourseDaoImpl implements CourseDao {

    private static final String SELECT_ALL_COURSES = "SELECT id, name FROM course";
    private static final String SELECT_ALL_STUDENTS_BY_COURSES = "SELECT s.id AS student_id, s.name AS student_name\n" +
            "FROM course c\n" +
            "JOIN student_course sc ON c.id = sc.course_id\n" +
            "JOIN student s ON s.id = sc.student_id\n" +
            "WHERE c.id = ?";
    private static final String SELECT_COURSE_BY_NAME = "SELECT id, name FROM course WHERE name = ?";
    private static final String INSERT_COURSE = "INSERT INTO course (name) VALUES (?)";
    private static final String DELETE_COURSE = "DELETE FROM course WHERE id = ?";
    private static final String DELETE_STUDENT_FROM_COURSE = "DELETE FROM student_course WHERE course_id = ?";

    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_COURSES)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                long courseId = rs.getLong("id");
                String courseName = rs.getString("name");
                Course course = new Course();
                course.setId(courseId);
                course.setName(courseName);

                List<Student> students = getAllStudentsByCourseId(courseId);
                course.setStudents(students);

                courses.add(course);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return courses;
    }

    @Override
    public void addCourse(Course course) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COURSE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, course.getName());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating student failed, no rows affected.");
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                course.setId(generatedId);
            } else {
                throw new SQLException("Creating student failed, no ID obtained.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Course getCourseByName(String courseName) {

        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_COURSE_BY_NAME)) {
            statement.setString(1, courseName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                long courseId = rs.getLong("id");
                String name = rs.getString("name");

                Course course = new Course();
                course.setId(courseId);
                course.setName(name);

                return course;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void deleteCourseById(long id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COURSE)) {
            preparedStatement.setLong(1, id);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting student failed, no rows affected.");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteStudentsByCourseId(long courseId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_STUDENT_FROM_COURSE)) {
            statement.setLong(1, courseId);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public List<Student> getAllStudentsByCourseId(long courseId) {
        List<Student> students = new ArrayList<>();

        try (Connection connection = DbUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_STUDENTS_BY_COURSES)) {

            statement.setLong(1, courseId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                long studentId = resultSet.getLong("student_id");
                String studentName = resultSet.getString("student_name");

                Student student = new Student();
                student.setId(studentId);
                student.setName(studentName);
                students.add(student);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return students;
    }

}
