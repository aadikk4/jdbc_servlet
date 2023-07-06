package org.example.servlet;


import org.example.model.Course;
import org.example.model.dto.CourseDTO;
import org.example.model.dto.StudentDTO;
import org.example.service.CourseService;
import org.example.service.StudentService;
import org.example.service.impl.CourseServiceImpl;
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
    private final CourseService courseService;


    public StudentServlet(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    public StudentServlet() {

        studentService = new StudentServiceImpl();
        courseService = new CourseServiceImpl();
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

        StudentDTO student = new StudentDTO();
        student.setName(name);
        student.setDormitoryId(dormitoryId);
        student.setCourses(new ArrayList<>());


        if (courses != null && courses.length > 0) {
            for (String courseName : courses) {
                CourseDTO course = getCourseByName(courseName);
                student.getCourses().add(course);
            }
        }
        studentService.addStudent(student);

        List<StudentDTO> students = studentService.getAllStudents();
        req.setAttribute("students", students);
        req.getRequestDispatcher("students.jsp").forward(req, resp);
    }

    private CourseDTO getCourseByName(String courseName) {
        CourseDTO course = courseService.getCourseByName(courseName);

//        switch (courseName) {
//            case "Mathematics" -> course.setId(1);
//            case "Physics" -> course.setId(2);
//            case "Chemistry" -> course.setId(3);
//        }
//        course.setName(courseName);
        if (course != null) {
            course.setId(course.getId());
            course.setName(course.getName());
        }
        return course;
    }

    private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long studentId = Long.parseLong(req.getParameter("id"));
        studentService.deleteCoursesByStudentId(studentId);
        studentService.deleteStudentById(studentId);
        resp.sendRedirect(req.getContextPath() + "/students");
    }
}

