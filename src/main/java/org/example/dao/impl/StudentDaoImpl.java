package org.example.dao.impl;

import org.example.dao.StudentDao;
import org.example.model.Course;
import org.example.model.Dormitory;
import org.example.model.Student;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.util.DbUtil.getConnection;

public class StudentDaoImpl implements StudentDao {
    private static final String SELECT_ALL_STUDENTS = "SELECT s.id AS student_id, s.name AS student_name, c.id AS course_id,\n" +
            "c.name AS course_name, d.id AS dormitory_id, d.name AS dormitory_name\n" +
            "FROM student s\n" +
            "LEFT JOIN student_course sc ON s.id = sc.student_id\n" +
            "LEFT JOIN course c ON c.id = sc.course_id\n" +
            "LEFT JOIN dormitory d ON s.dormitory_id = d.id;";

    private static final String INSERT_STUDENT = "INSERT INTO student (name, dormitory_id) VALUES (?, ?)";
    private static final String insertStudentCourseQuery = "INSERT INTO student_course (student_id, course_id) VALUES (?, ?)";
    private static final String DELETE_STUDENT = "DELETE FROM student WHERE id = ?";
    private static final String DELETE_COURSE_FROM_STUDENT = "DELETE FROM student_course WHERE student_id = ?";


    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        Map<Long, Student> studentMap = new HashMap<>();

        try (Connection connection = getConnection();
             PreparedStatement resultSet = connection.prepareStatement(SELECT_ALL_STUDENTS)) {

            ResultSet rs = resultSet.executeQuery();

            while (rs.next()) {
                long studentId = rs.getLong("student_id");
                String studentName = rs.getString("student_name");
                long courseId = rs.getLong("course_id");
                String courseName = rs.getString("course_name");
                long dormitoryId = rs.getLong("dormitory_id");
                String dormitoryName = rs.getString("dormitory_name");

                Student student = studentMap.computeIfAbsent(studentId, id -> {
                    Student s = new Student();
                    s.setId(studentId);
                    s.setName(studentName);
                    s.setCourses(new ArrayList<>());
                    s.setDormitoryId(dormitoryId);
                    return s;
                });

                Course course = new Course();
                course.setId(courseId);
                course.setName(courseName);

                student.getCourses().add(course);

                Dormitory dormitory = new Dormitory();
                dormitory.setId(dormitoryId);
                dormitory.setName(dormitoryName);

                student.setDormitoryId(dormitoryId);
                if (!students.contains(student)) {
                    students.add(student);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return students;
    }


    @Override
    public void addStudent(Student student) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STUDENT, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, student.getName());
            preparedStatement.setLong(2, student.getDormitoryId());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating student failed, no rows affected.");
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                long generatedId = generatedKeys.getLong(1);
                student.setId(generatedId);
            } else {
                throw new SQLException("Creating student failed, no ID obtained.");
            }

            List<Course> courses = student.getCourses();
            if (courses != null && !courses.isEmpty()) {
                try (PreparedStatement courseStatement = connection.prepareStatement(insertStudentCourseQuery)) {
                    for (Course course : courses) {
                        courseStatement.setLong(1, student.getId());
                        courseStatement.setLong(2, course.getId());
                        courseStatement.executeUpdate();
                    }
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteStudentById(long id) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_STUDENT)) {

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
    public void deleteCoursesByStudentId(long studentId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_COURSE_FROM_STUDENT)) {
            statement.setLong(1, studentId);
            statement.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}