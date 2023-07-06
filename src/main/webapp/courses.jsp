<%@ page import="org.example.model.dto.CourseDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.model.dto.StudentDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Course List</title>
</head>
<body>
<h1>Course List</h1>
<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Students</th>
        <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <% List<CourseDTO> courses = (List<CourseDTO>) request.getAttribute("courses");
        if (courses != null && !courses.isEmpty()) {
            for (CourseDTO course : courses) { %>
    <tr>
        <td><%= course.getId() %></td>
        <td><%= course.getName() %></td>
        <td>
            <% List<StudentDTO> students = course.getStudents();
                if (students != null && !students.isEmpty()) {
                    for (StudentDTO student : students) { %>
            <%= student.getName() %><br>
            <% }
            } else { %>
            No students enrolled
            <% } %>
        </td>
        <td>
            <form action="courses" method="post" onsubmit="return confirm('Are you sure you want to delete this course?')">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" value="<%= course.getId() %>">
                <button type="submit">Delete</button>
            </form>
        </td>
    </tr>
    <% }
    } else { %>
    <tr>
        <td colspan="4">No courses found</td>
    </tr>
    <% } %>
    </tbody>
</table>
<h2>Add Course</h2>
<form action="courses" method="post">
    <input type="hidden" name="action" value="add">
    <label for="name">Name:</label>
    <input type="text" id="name" name="name" required>
    <br>
    <button type="submit">Add Course</button>
</form>
</body>
</html>

