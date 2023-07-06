<%@ page import="org.example.model.Student" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.model.Course" %>
<%@ page import="org.example.model.dto.StudentDTO" %>
<%@ page import="org.example.model.dto.CourseDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student List</title>
</head>
<body>
<h1>Student List</h1>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Dormitory ID</th>
        <th>Courses</th>
    </tr>
    </thead>
    <tbody>
    <% List<StudentDTO> students = (List<StudentDTO>) request.getAttribute("students");
        if (students != null && !students.isEmpty()) {
            for (StudentDTO student : students) { %>
    <tr>
        <td><%= student.getId() %></td>
        <td><%= student.getName() %></td>
        <td><%= student.getDormitoryId() %></td>
        <td>
            <% List<CourseDTO> courses = student.getCourses();
                if (courses != null && !courses.isEmpty()) {
                    for (CourseDTO course : courses) { %>
            <%= course.getName() %><br>
            <% }
            } else { %>
            No courses enrolled
            <% } %>
        </td>
        <td>
            <form action="students" method="post" onsubmit="return confirm('Are you sure you want to delete this student?')">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="<%= student.getId() %>">
                <button type="submit">Delete</button>
            </form>
        </td>

    </tr>
    <% }
    } else { %>
    <tr>
        <td colspan="4">No students found</td>
    </tr>
    <% } %>
    </tbody>
</table>
<br>
<h2>Add Student</h2>
<form action="students" method="post">
    <input type="hidden" name="action" value="add">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required>
    <br>
    <label for="dormitoryId">Dormitory ID:</label>
    <input type="number" id="dormitoryId" name="dormitoryId" required>
    <br>
    <h3>Courses:</h3>
    <div id="courses">
        <div class="course-input">
            <input type="text" name="courseNames[]" required>
        </div>
    </div>
    <button type="button" onclick="addCourseInput()">Add Course</button>
    <br><br>
    <input type="submit" value="Add Student">
</form>

<script>
    function addCourseInput() {
        const coursesDiv = document.getElementById('courses');
        const courseInputDiv = document.createElement('div');
        courseInputDiv.classList.add('course-input');
        const courseInput = document.createElement('input');
        courseInput.setAttribute('type', 'text');
        courseInput.setAttribute('name', 'courseNames[]');
        courseInput.setAttribute('required', 'true');
        courseInputDiv.appendChild(courseInput);
        coursesDiv.appendChild(courseInputDiv);
    }
</script>
</body>
</html>
