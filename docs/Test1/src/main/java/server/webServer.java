/*
 * This was written in lecture 7 in class. We have adapted it for our website.
 */

package server;

import static spark.Spark.*;
import dao.CourseDao;
import dao.InMemoryCourseDao;
import dao.UniRestCourseDao;
import model.Course;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;



public class webServer {
    public static void main(String[] args) {


      CourseDao courseDao = new InMemoryCourseDao();
       // CourseDao courseDao = new UniRestCourseDao();

        get("/", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("courseList", courseDao.findAll());
            return new ModelAndView(model, "courses.hbs");
        }),  new HandlebarsTemplateEngine());

        post("/", ((request, response) -> {
            String id = request.queryParams("id");
            String courseName = request.queryParams("courseName");
            String professorName = request.queryParams("professorName");
            String startDate = request.queryParams("startDate");
            String endDate = request.queryParams("endDate");
            String lectureDaysOfWeek = request.queryParams("lectureDaysOfWeek");
            String lectureStartTime = request.queryParams("lectureStartTime");
            String lectureEndTime = request.queryParams("lectureEndTime");
            String ohDaysOfWeek = request.queryParams("ohDaysOfWeek");
            String ohStartTime = request.queryParams("ohStartTime");
            String ohEndTime = request.queryParams("ohEndTime");


            courseDao.add(new Course(id, courseName, professorName, startDate, endDate, lectureDaysOfWeek, lectureStartTime,
                    lectureEndTime, ohDaysOfWeek, ohStartTime, ohEndTime));

            response.redirect("/");
            return null;
        }), new HandlebarsTemplateEngine());

        get("/calendar", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("courseList", courseDao.findAll());
            return new ModelAndView(model, "calendar.hbs");
        }),  new HandlebarsTemplateEngine());

    }
}

