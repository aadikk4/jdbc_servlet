import org.checkerframework.checker.units.qual.C;
import org.example.dao.StudentDao;
import org.example.dao.impl.StudentDaoImpl;
import org.example.model.Course;
import org.example.model.Dormitory;
import org.example.model.Student;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentDaoImplTest {
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    private StudentDao studentDao;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);

        studentDao = new StudentDaoImpl();

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(connection.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getLong(anyString())).thenReturn(1L);
        when(resultSet.getString(anyString())).thenReturn("John Doe");
        Student student = new Student();
        student.setName("John Doe");
        student.setDormitoryId(1L);
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(1L, "Mathematics"));
        student.setCourses(courses);

        // Act
        studentDao.addStudent(student);
    }

    @Test
    void getAllStudents_ReturnsListOfStudents() throws SQLException {
        // Arrange
        when(resultSet.getLong("course_id")).thenReturn(1L);
        when(resultSet.getString("course_name")).thenReturn("Mathematics");
        when(resultSet.getLong("dormitory_id")).thenReturn(1L);
        when(resultSet.getString("dormitory_name")).thenReturn("Dorm A");

        // Act
        List<Student> students = studentDao.getAllStudents();


        // Assert
        assertNotNull(students);
        assertFalse(students.isEmpty());
        assertEquals(1, students.size());

        Student student = students.get(0);
        assertEquals("John Doe", student.getName());

        List <Course>courses = student.getCourses();
        assertNotNull(courses);
        assertFalse(courses.isEmpty());
        assertEquals(1, courses.size());

        Course course = courses.get(0);
        assertEquals(1L, course.getId());
        assertEquals("Mathematics", course.getName());
    }

    @Test
    void addStudent_SuccessfullyAddsStudent() throws SQLException {
        // Arrange
        Student student = new Student();
        student.setName("Army Hummer");
        student.setDormitoryId(2L);
        List<Course> courses = new ArrayList<>();
        courses.add(new Course(3L, "Chemistry"));
        student.setCourses(courses);

        // Act
        studentDao.addStudent(student);

        // Assert
        assertNotNull(student.getId());

    }

    @Test
    void deleteStudentById_SuccessfullyDeletesStudent() throws SQLException {
        // Arrange
        long studentId = 1L;

        // Act
        studentDao.deleteStudentById(studentId);

    }

    @Test
    void deleteCoursesByStudentId_SuccessfullyDeletesCourses() throws SQLException {
        // Arrange
        long studentId = 1L;

        // Act
        studentDao.deleteCoursesByStudentId(studentId);
    }
    @AfterEach
    public void clean() throws SQLException {
        try (Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE student_course");
            statement.execute("DROP TABLE student");
            statement.execute("DROP TABLE dormitory");
            statement.execute("DROP TABLE course");
        }
        connection.close();
    }
}

