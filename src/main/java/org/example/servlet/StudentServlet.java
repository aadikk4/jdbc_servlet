package org.example.servlet;


import org.example.model.Course;
import org.example.model.Student;
import org.example.service.StudentService;
import org.example.service.impl.StudentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StudentServlet extends HttpServlet {
    private final StudentService studentService;


    public StudentServlet(StudentService studentService) {
        this.studentService = studentService;
    }

    public StudentServlet() {
        studentService = new StudentServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("students", studentService.getAllStudents());
        req.getRequestDispatcher("students.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
            return;
        }

        switch (action) {
            case "add":
                addStudent(req, resp);
                break;
            case "delete":
                deleteStudent(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
    }

    private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        Long dormitoryId = Long.valueOf(req.getParameter("dormitoryId"));
        String[] courses = req.getParameterValues("courseNames[]");

        Student student = new Student();
        student.setName(name);
        student.setDormitoryId(dormitoryId);
        student.setCourses(new ArrayList<>());


        if (courses != null && courses.length > 0) {
            for (String courseName : courses) {
                Course course = getCourseByName(courseName);
                student.getCourses().add(course);
            }
        }
        studentService.addStudent(student);

        List<Student> students = studentService.getAllStudents();
        req.setAttribute("students", students);
        req.getRequestDispatcher("students.jsp").forward(req, resp);
    }

    private Course getCourseByName(String courseName) {
        Course course = new Course();
        switch (courseName) {
            case "Mathematics":
                course.setId(1);
                break;
            case "Physics":
                course.setId(2);
                break;
            case "Chemistry":
                course.setId(3);
                break;
        }
        course.setName(courseName);
        return course;
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long studentId = Long.parseLong(req.getParameter("id"));
        studentService.deleteCoursesByStudentId(studentId);
        studentService.deleteStudentById(studentId);
        resp.sendRedirect(req.getContextPath() + "/students");
    }
}

