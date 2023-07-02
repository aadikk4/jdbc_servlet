import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.example.util.DbUtil.getConnection;

@Testcontainers
public class StudentIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13").withDatabaseName("test").withUsername("test").withPassword("test");

    private static Connection connection;

    @BeforeEach
    public void setup() throws SQLException, ClassNotFoundException {
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        connection = DriverManager.getConnection(jdbcUrl, username, password);

        // Создание таблицы и добавление тестовых данных
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE dormitory (id SERIAL PRIMARY KEY, name VARCHAR(100))");
            statement.execute("CREATE TABLE student (id SERIAL PRIMARY KEY, name VARCHAR(100), dormitory_id INT REFERENCES dormitory(id))");
            statement.execute("CREATE TABLE course (id SERIAL PRIMARY KEY, name VARCHAR(100))");
            statement.execute("CREATE TABLE student_course (student_id INT REFERENCES student(id), course_id INT REFERENCES course(id), PRIMARY KEY (student_id, course_id))");

            statement.executeUpdate("INSERT INTO dormitory (name) VALUES ('Dormitory A')");
            statement.executeUpdate("INSERT INTO dormitory (name) VALUES ('Dormitory B')");


            statement.executeUpdate("INSERT INTO course (name) VALUES ('Mathematics')");
            statement.executeUpdate("INSERT INTO course (name) VALUES ('Physics')");
            statement.executeUpdate("INSERT INTO course (name) VALUES ('Chemistry')");

            statement.executeUpdate("INSERT INTO student (name, dormitory_id) VALUES ('John Doe', 1)");
            statement.executeUpdate("INSERT INTO student (name, dormitory_id) VALUES ('Jane Smith', 1)");
            statement.executeUpdate("INSERT INTO student (name, dormitory_id) VALUES ('Michael Johnson', 2)");

            statement.executeUpdate("INSERT INTO student_course(student_id, course_id) VALUES(1, 1)");
            statement.executeUpdate("INSERT INTO student_course(student_id, course_id)VALUES(1, 2)");
            statement.executeUpdate("INSERT INTO student_course(student_id, course_id) VALUES(2, 1)");
            statement.executeUpdate("INSERT INTO student_course(student_id, course_id) VALUES(3, 3)");
        }
    }



    @Test
    public void testGetAllStudents() {
        int expectedStudentCount = 3;
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM student")) {

            // Считываем результаты запроса и проверяем, что возвращено ожидаемое количество студентов
            int count = 0;
            while (resultSet.next()) {
                count++;
                // Можно также проверить другие атрибуты студента, если необходимо
            }

            // Проверяем, что количество студентов соответствует ожидаемому значению
            Assertions.assertEquals(expectedStudentCount, count);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAddStudent() {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO student (name, dormitory_id) VALUES (?, ?)")) {

            // Задаем параметры для добавления нового студента
            preparedStatement.setString(1, "Naa Kent");
            preparedStatement.setLong(2, 1L);

            // Выполняем запрос на добавление студента
            int rowsAffected = preparedStatement.executeUpdate();

            // Проверяем, что студент успешно добавлен (количество добавленных строк больше нуля)
            Assertions.assertTrue(rowsAffected > 0);
            // Можно также проверить другие атрибуты добавленного студента, если необходимо
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteStudentById() {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM student WHERE id = ?")) {
            preparedStatement.execute("DELETE FROM student_course WHERE student_id = ?");
            long studentIdToDelete = 1L;

            // Задаем параметр для удаления студента по идентификатору
            preparedStatement.setLong(1, studentIdToDelete);

            // Выполняем запрос на удаление студента
            int rowsAffected = preparedStatement.executeUpdate();

            // Проверяем, что студент успешно удален (количество удаленных строк больше нуля)
            Assertions.assertTrue(rowsAffected > 0);

            // Проверяем, что студент отсутствует в базе данных после удаления
            try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery("SELECT * FROM student WHERE id = " + studentIdToDelete)) {
                Assertions.assertFalse(resultSet.next());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @AfterEach
    public  void cleanup() throws SQLException {
        // Удаление таблиц
        try (Statement statement = connection.createStatement()) {

            statement.execute("DROP TABLE student_course");
            statement.execute("DROP TABLE student");
            statement.execute("DROP TABLE dormitory");
            statement.execute("DROP TABLE course");
        }
        // Закрытие соединения
        connection.close();
    }
}
