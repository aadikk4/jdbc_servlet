package org.example.servlet;

import org.example.model.dto.CourseDTO;
import org.example.service.CourseService;
import org.example.service.impl.CourseServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CourseServlet extends HttpServlet {
    private final CourseService courseService;


    public CourseServlet(CourseService courseService) {
        this.courseService = courseService;
    }

    public CourseServlet() {
        courseService = new CourseServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("courses", courseService.getAllCourses());
        req.getRequestDispatcher("courses.jsp").forward(req, resp);
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
                addSCourse(req, resp);
                break;
            case "delete":
                deleteCourse(req, resp);
                break;
            default:
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action parameter");
        }
}

    private void addSCourse(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");

        CourseDTO course = new CourseDTO();
        course.setName(name);
        courseService.addCourse(course);

        List<CourseDTO> courses = courseService.getAllCourses();
        req.setAttribute("courses", courses);
        req.getRequestDispatcher("courses.jsp").forward(req, resp);
    }
    private void deleteCourse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        long courseId = Long.parseLong(req.getParameter("id"));

        courseService.deleteStudentsByCourseId(courseId);
        courseService.deleteCourseById(courseId);

        resp.sendRedirect(req.getContextPath() + "/courses");
    }

}