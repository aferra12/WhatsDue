package server;

/**
 * Welcome to the What's Due course organizer! This will allow you to organize your course material
 * easily! This server was written in lecture 7, and we have adapted it for our project.
 * Brian Linton AJ Ferrara Joey Kern Adam Kohl Hedin Beattie
 */

/* Twilio allows for text updates */

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import dao.*;
import exceptions.CrudException;
import model.Assignment;
import model.Course;
import model.Professor;
import persist.JDBCAssignmentCRUDPersister;
import persist.JDBCCourseCRUDPersister;
import persist.JDBCProfessorCRUDPersister;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.utils.IOUtils;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.Part;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class webServer {
    public static void main(String[] args) throws SQLException {


        //from Continues Heroku reading. Assigns port for Heroku
        port(getHerokuAssignedPort());

        //connecting to database Store.db
        final String URI = "jdbc:sqlite:./Store.db";
        Connection conn = DriverManager.getConnection(URI);

        String sql;
        Statement st = conn.createStatement();

        //create tables to store professors, courses, and assignments
        //The profId for a course = the profId for corresponding professor.
        //Professor username and password correspond to their Twilio account information for text alerts.
        //You can't add a course with a non-existing profId.
        //The assignmentId for an assignment = courseId for corresponding course.
        //You can't add an assignment for a non-existing course.
        //You cannot have duplicate profId or courseId. You can have duplicate
        //assignmentId, as multiple assignments can be associated with a single course.

        sql = "CREATE TABLE IF NOT EXISTS Professors (profId VARCHAR(14) PRIMARY KEY, professorName VARCHAR(100), calURL VARCHAR(1000), digits VARCHAR(5000), username VARCHAR(100), password VARCHAR(100), phoneNum VARCHAR(25));";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Courses (courseId VARCHAR(14) PRIMARY KEY, profId VARCHAR(14), courseName VARCHAR(30) NOT NULL, professorName VARCHAR(100), startDate VARCHAR(20), endDate VARCHAR(20), lectureDaysOfWeek VARCHAR(7), lectureStartTime VARCHAR(10), lectureEndTime VARCHAR(10), ohDaysOfWeek VARCHAR(7), ohStartTime VARCHAR(10), ohEndTime VARCHAR(10), added VARCHAR(10), FOREIGN KEY(profId) REFERENCES Professors(profId) ON DELETE CASCADE);";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Assignments (id INTEGER PRIMARY KEY AUTOINCREMENT, assignmentId VARCHAR(14), assignmentNumber VARCHAR(5), studentTask VARCHAR(30), dueDate VARCHAR(14), doc BLOB, added VARCHAR(10), materialType VARCHAR(30), FOREIGN KEY(assignmentId) REFERENCES Courses(courseId) ON DELETE CASCADE);";
        st.execute(sql);

        sql = "PRAGMA foreign_keys = ON;";
        st.execute(sql);

        JDBCCourseCRUDPersister courseCRUD = new JDBCCourseCRUDPersister(conn);
        JDBCProfessorCRUDPersister professorCRUD = new JDBCProfessorCRUDPersister(conn);
        JDBCAssignmentCRUDPersister assignmentCRUD = new JDBCAssignmentCRUDPersister(conn);

        CourseDao courseDao = new InMemoryCourseDao();
        ProfessorDao professorDao = new InMemoryProfessorDao();
        AssignmentDao assignmentDao = new InMemoryAssignmentDao();

        //location of css and js files
        staticFiles.location("/public");

        getSignUpPage(professorDao);
        getLogInPage(professorDao);
        loggedIn(professorCRUD);

        getDashboardPage(professorCRUD,courseCRUD);

        /* Professor Methods */
        //wanted to do delete method but html form doesn't support this method
        deleteProf(professorCRUD, professorDao);
        //wanted to use put but html forms only support get and post
        editProf(professorCRUD, professorDao);
        addProf(professorCRUD, professorDao);
        /* Course Methods */

        editCourse(courseCRUD);
        deleteCourse(courseCRUD);
        addCourse(courseCRUD, courseDao, professorCRUD);
        addCourseByFile(courseCRUD, courseDao, professorCRUD);
        getCourseOverview(courseCRUD, professorCRUD);
        /* Assignment Methods */
        getAssignmentsTable(assignmentCRUD, courseCRUD, professorCRUD);
        //wanted to do delete method but html form doesn't support this method
        deleteAssignment(assignmentCRUD);
        downloadAssignmentDoc(assignmentDao, conn);
        //wanted to use put but html forms only support get and post
        editAssignment(assignmentCRUD, assignmentDao, conn);
        addAssignment(assignmentCRUD, assignmentDao, conn);
        numberAdd(professorCRUD);
        /* Calendar Methods */
        getCalendar(courseCRUD, assignmentCRUD, professorCRUD);
        postCalendar(courseCRUD, assignmentCRUD);

        errorHelp(courseCRUD, professorCRUD);

        /* Student view methods */
        getStudentOverview(courseCRUD, assignmentCRUD, professorCRUD);
        getStudentTable(assignmentCRUD,courseCRUD,professorCRUD);
        getStudentCalendar(courseCRUD, assignmentCRUD, professorCRUD);

        //conn.close();
    }

    /* .hbs file sets up front end of page in ModelAndView, model determines what information that page can access */
    /* request.queryParams("name") gets a specific value from an html form */
    /* response.redirect sends the user to a new page after the method is done */
    /* get and post define endpoints for users to interact with */
    /* this server developed using java spark framework */

    //set up sign up page
    private static void getSignUpPage(ProfessorDao professorDao) {
        get("/signup", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("professorList", professorDao.findAll());
            return new ModelAndView(model, "signup.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //set up login up page
    private static void getLogInPage(ProfessorDao professorDao) {
        get("/", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("professorList", professorDao.findAll());
            return new ModelAndView(model, "login.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //set up dashboard page
    private static void getDashboardPage(JDBCProfessorCRUDPersister professorCRUD, JDBCCourseCRUDPersister courseCRUD) {
        get("/dashboard/:profId", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            /* Finds corresponding professor, courses to the profId */
            model.put("professorList", professorCRUD.findByID(request.params(":profId")));
            model.put("courseList", courseCRUD.findByPID(request.params(":profId")));
            model.put("sidebarList", courseCRUD.findByPID(request.params(":profId")));
            return new ModelAndView(model, "dashboard.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //login post request
    private static void loggedIn(JDBCProfessorCRUDPersister professorCRUD) {
        post("/login", ((request, response) -> {
            String profId = request.queryParams("profId");
            List<Professor> professors = professorCRUD.findByID(profId);
            /* Make sure professor id exists */
            if(professors.size() != 0) {
                response.redirect("/dashboard/" + profId);
            } else {
                response.redirect("/error");
            }
            return null;
        }),  new HandlebarsTemplateEngine());
    }

    //set up student overview
    private static void getStudentOverview(JDBCCourseCRUDPersister courseCRUD, JDBCAssignmentCRUDPersister assignmentCRUD, JDBCProfessorCRUDPersister professorCRUD) {
        get("/studentOverview/:profId/:courseId", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("professorList", professorCRUD.findByID(request.params(":profId")));
            model.put("courseList", courseCRUD.findByPCID(request.params(":profId"), request.params(":courseId")));
            model.put("assignmentList", assignmentCRUD.findByCD(request.params(":courseId")));
            return new ModelAndView(model, "studentOverview.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //set up student table
    private static void getStudentTable(JDBCAssignmentCRUDPersister assignmentCRUD, JDBCCourseCRUDPersister courseCRUD, JDBCProfessorCRUDPersister professorCRUD) {
        get("/studentTable/:profId/:courseId", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("assignmentList", assignmentCRUD.findByCD(request.params(":courseId")));
            model.put("professorList", professorCRUD.findByID(request.params(":profId")));
            model.put("courseList", courseCRUD.cfindByCID(request.params(":courseId")));
            return new ModelAndView(model, "studentTable.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //set up student calendar
    private static void getStudentCalendar(JDBCCourseCRUDPersister courseCRUD, JDBCAssignmentCRUDPersister assignmentCRUD, JDBCProfessorCRUDPersister professorCRUD) {
        get("/studentCalendar/:profId/:courseId", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("professorList", professorCRUD.findByID(request.params(":profId")));
            model.put("courseList", courseCRUD.findByPCID(request.params(":profId"), request.params(":courseId")));
            model.put("assignmentList", assignmentCRUD.findByCD(request.params(":courseId")));
            return new ModelAndView(model, "studentCalendar.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //method to delete professor
    private static void deleteProf(JDBCProfessorCRUDPersister professorCRUD, ProfessorDao professorDao) {
        post("/deleteProf", ((request, response) -> {
            String profId = request.queryParams("profIdDelete");
            try {
                if (profId != null) {
                    professorCRUD.delete(profId);
                }
            } catch (CrudException e) {
                System.out.println(e.getMessage());
                response.redirect("/error");
            }
            /* redirect to login page */
            response.redirect("/");
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //method to edit a professor
    private static void editProf(JDBCProfessorCRUDPersister professorCRUD, ProfessorDao professorDao) {
        post("/editProf", ((request, response) -> {
            String sameId = request.queryParams("sameId");
            String newName = request.queryParams("newName");
            String newURL = request.queryParams("calURL");
            if (newURL != null) {
                professorCRUD.updateURL(sameId, newURL);
            }
            Professor p = new Professor(sameId, newName, null);
            try {
                professorCRUD.update(p);

            } catch (CrudException e) {
                System.out.println("couldnt update professor");
                response.redirect("/error");
            }
            response.redirect("/dashboard/" + sameId);
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //method to add a professor
    private static void addProf(JDBCProfessorCRUDPersister professorCRUD, ProfessorDao professorDao) {
        post("/addProf", ((req, res) -> {
            String professorName = req.queryParams("professorName");
            String profId = req.queryParams("profId");
            String calURL = req.queryParams("calURL");
            /* new professor to be added */
            Professor p = new Professor(profId, professorName, calURL, null, null, null, null);
            try {
                professorCRUD.create(p);
                professorDao.add(p);
            } catch (CrudException e) {
                System.out.println("couldnt add professor");
                res.redirect("/error");
            }
            try {
                res.redirect("/dashboard/" + profId);
            } catch (IllegalStateException e) {
                System.out.println("Response state message. Does not impact functionality.");
            }
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //method to help with errors
    private static void errorHelp(JDBCCourseCRUDPersister courseCRUD, JDBCProfessorCRUDPersister professorCRUD) {
        get("/error", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("courseList", courseCRUD.readAll("1"));
            model.put("professorList", professorCRUD.readAll("1"));
            return new ModelAndView(model, "error.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //method to edit a course
    private static void editCourse(JDBCCourseCRUDPersister courseCRUD) {
        post("/editCourse", ((request, response) -> {
            String courseId = request.queryParams("courseId");
            String profId = request.queryParams("profId");
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
            /* create new course to be edited */
            Course c = new Course(courseId, profId, courseName, professorName, startDate, endDate, lectureDaysOfWeek, lectureStartTime,
                    lectureEndTime, ohDaysOfWeek, ohStartTime, ohEndTime, "false");
            try {
                courseCRUD.update(c);
            } catch (CrudException e) {
                System.out.println("couldnt update course");
                response.redirect("/error");
            }
            response.redirect("/dashboard/" + profId);
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //method to delete a course
    private static void deleteCourse(JDBCCourseCRUDPersister courseCRUD) {
        post("/deleteCourse", ((request, response) -> {
            String profId = request.queryParams("profId");
            String courseId = request.queryParams("courseIdDelete");
            try {
                if (courseId != null) {
                    courseCRUD.delete(courseId);
                }
            } catch (CrudException e) {
                System.out.println(e.getMessage());
                response.redirect("/error");
            }
            response.redirect("/dashboard/" + profId);
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //add a course
    private static void addCourse(JDBCCourseCRUDPersister courseCRUD, CourseDao courseDao, JDBCProfessorCRUDPersister professorCRUD) {
        post("/courses", ((request, response) -> {
            String courseId = request.queryParams("courseId");
            String profId = request.queryParams("profId");
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
            /* course to be added */
            Course c = new Course(courseId, profId, courseName, professorName, startDate, endDate, lectureDaysOfWeek, lectureStartTime,
                    lectureEndTime, ohDaysOfWeek, ohStartTime, ohEndTime, "false");
            try {
                courseCRUD.create(c);
                courseDao.add(c);
            } catch (CrudException e) {
                System.out.println("couldnt add course");
                response.redirect("/error");
            }
            try {
                response.redirect("/dashboard/" + profId);
            } catch (IllegalStateException e) {
                System.out.println("Response state message. Does not impact functionality.");
            }
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //add a course via input
    @SuppressWarnings("unchecked")
    private static void addCourseByFile(JDBCCourseCRUDPersister courseCRUD, CourseDao courseDao, JDBCProfessorCRUDPersister professorCRUD) {
        post("/yaml", ((request, response) -> {
            /* gets home directory of users computer to find YAML template */
            /*
            String home = System.getProperty("user.home");
            final String fileName = home + "/Downloads/CoursesTemplate.yml";

             */

            /* indicates that there are multiple input types and need to read them as a multipart request (text and file */

            if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            }

            Part partBytes = request.raw().getPart("fileUploadYAML");
            InputStream ios = partBytes.getInputStream();

            ArrayList<String> key = new ArrayList<String>();
            ArrayList<String> value = new ArrayList<String>();
            Yaml yaml = new Yaml();
            try {
                //InputStream ios = new FileInputStream(new File(fileName));
                // Parse the YAML file and return the output as a series of Maps and Lists
                Map<String, Object> result = (Map<String, Object>) yaml.load(ios);
                for (Object name : result.keySet()) {
                    key.add(name.toString());
                    value.add(result.get(name).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /* course to be added */
            Course c = new Course(value.get(0),value.get(1),value.get(2),value.get(3),value.get(4),value.get(5),value.get(6),value.get(7),value.get(8),value.get(9),value.get(10),value.get(11),value.get(12));
            try {
                courseCRUD.create(c);
                courseDao.add(c);
            } catch (CrudException e) {
                System.out.println("couldnt add course");
                response.redirect("/error");
            }
            try {
                response.redirect("/dashboard/" + c.getProfId());
            } catch (IllegalStateException e) {
                System.out.println("Response state message. Does not impact functionality.");
            }
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //method for course overview page
    private static void getCourseOverview(JDBCCourseCRUDPersister courseCRUD, JDBCProfessorCRUDPersister professorCRUD) {
        get("/overview/:profId/:courseId", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("professorList", professorCRUD.findByID((request.params(":profId"))));
            model.put("courseList", courseCRUD.findByPCID((request.params(":profId")), request.params(":courseId")));
            model.put("sidebarList", courseCRUD.findByPID(request.params(":profId")));
            return new ModelAndView(model, "CourseOverview.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //method to set up assignments table page
    private static void getAssignmentsTable(JDBCAssignmentCRUDPersister assignmentCRUD, JDBCCourseCRUDPersister courseCRUD, JDBCProfessorCRUDPersister professorCRUD) {
        get("/table/:profId/:courseId", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            /* get corresponding assignment for professor and course */
            model.put("assignmentList", assignmentCRUD.findByCD(request.params(":courseId")));
            model.put("professorList", professorCRUD.findByID(request.params(":profId")));
            model.put("courseList", courseCRUD.cfindByCID(request.params(":courseId")));
            model.put("sidebarList", courseCRUD.findByPID(request.params(":profId")));
            return new ModelAndView(model, "TableView.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //method to delete an assignment
    private static void deleteAssignment(JDBCAssignmentCRUDPersister assignmentCRUD) {
        post("/deleteAssignment", ((request, response) -> {
            String profId = request.queryParams("profId");
            String courseId = request.queryParams("courseId");
            String studentTask = request.queryParams("taskDelete");
            try {
                if (studentTask != null) {
                    assignmentCRUD.deleteA(studentTask, courseId);
                }
            } catch (CrudException e) {
                System.out.println(e.getMessage());
                response.redirect("/error");
            }
            response.redirect("/table/" + profId + "/" + courseId);
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //method to add a document to table
    private static void downloadAssignmentDoc(AssignmentDao assignmentDao, Connection conn) {
        post("/table", ((request, response) -> {
            String profId = request.queryParams("profId");
            String courseId = request.queryParams("courseId");
            String filename = request.queryParams("button");
            assignmentDao.download(filename, conn, response, courseId);
            try {
                response.redirect("/table/" + profId + "/" + courseId);
            } catch (IllegalStateException e) {
                System.out.println("Response state message. Does not impact functionality.");
            }
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //method to edit assignment
    private static void editAssignment(JDBCAssignmentCRUDPersister assignmentCRUD, AssignmentDao assignmentDao, Connection conn) {
        post("/editTable", ((request, response) -> {

            /* indicates that there are multiple input types and need to read them as a multipart request (text and file */

            if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            }

            String id = request.queryParams("count");
            int id1 = Integer.parseInt(id);
            String profId = request.queryParams("profId");
            String assignmentId = request.queryParams("assignmentId");
            String assignmentNumber = request.queryParams("assignmentNumberE");
            String studentTask = request.queryParams("studentTaskE");
            String dueDate = request.queryParams("dueDateE");
            String materialType = request.queryParams("materialTypeE");
            /* reading in file that was uploaded */
            Part partBytes = request.raw().getPart("fileUploadE");
            byte[] fileBytes = IOUtils.toByteArray(partBytes.getInputStream());
            /* new assignment to be edited */
            Assignment a = new Assignment(id1, assignmentId, assignmentNumber, studentTask, dueDate, "false", materialType);

            /* formatting for text alerts */
            //Gson gson = new Gson();

            String digits = request.queryParams("digits");
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String phoneNum = request.queryParams("phoneNum");
            /*

            Type type = new TypeToken<ArrayList<String>>() {}.getType();

            ArrayList<String>  numArray = gson.fromJson(digits, type);

             */

            ArrayList<String> numArray = new ArrayList<>();
            int leng = (digits.length());
            int y = 0;
            for (int i = 0; i < leng; i+=12) {
                numArray.add(y, digits.substring(i, (i+12)));
                y++;
            }

            try {
                assignmentCRUD.update(a);
                /* adds document to database */
                assignmentDao.updateDoc(studentTask, fileBytes, conn, assignmentId);
                if(!(username.equals("")) && !(password.equals("")) && !(digits.equals("")) && !(phoneNum.equals(""))) {
                    sendEditAlert(username, password, studentTask, dueDate, numArray, phoneNum);
                }

            } catch (CrudException e) {
                System.out.println("couldnt update assignment");
            }
            response.redirect("/table/" + profId + "/" + assignmentId);
            return null;
        }), new HandlebarsTemplateEngine());
    }

    //method to add assignment to table
    private static void addAssignment(JDBCAssignmentCRUDPersister assignmentCRUD, AssignmentDao assignmentDao, Connection conn) {
        post("/addtotable", (request, response) -> {
            /* indicates that there are multiple input types and need to read them as a multipart request (text, file) */
            if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            }
            String profId = request.queryParams("profId");
            String assignmentId = request.queryParams("assignmentId");
            String assignmentNumber = request.queryParams("assignmentNumber");
            String studentTask = request.queryParams("studentTask");
            String dueDate = request.queryParams("dueDate");
            String materialType = request.queryParams("materialType");
            /* reading in file that was uploaded */
            Part partBytes = request.raw().getPart("fileUpload");
            byte[] fileBytes = IOUtils.toByteArray(partBytes.getInputStream());
            /* assignment to be added */
            Assignment a = new Assignment(assignmentId ,assignmentNumber, studentTask, dueDate, "false", materialType);

            /* formatting for text alerts */
            //Gson gson = new Gson();

            String digits = request.queryParams("digits");
            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String phoneNum = request.queryParams("phoneNum");


            //Type type = new TypeToken<ArrayList<String>>() {}.getType();

            //ArrayList<String>  numArray = gson.fromJson(digits, type);

            ArrayList<String> numArray = new ArrayList<>();
            int leng = (digits.length());
            int y = 0;
            for (int i = 0; i < leng; i+=12) {
                numArray.add(y, digits.substring(i, (i+12)));
                y++;
            }

            try {
                assignmentCRUD.create(a);
                assignmentDao.add(a);
                /* adds document to database */
                assignmentDao.updateDoc(studentTask, fileBytes, conn, assignmentId);

                if(!(username.equals("")) && !(password.equals("")) && !(digits.equals("")) && !(phoneNum.equals(""))) {
                    sendAddAlert(username, password, studentTask, dueDate, numArray, phoneNum);
                }

            } catch (CrudException e) {
                System.out.println("couldnt add assignment");
            }
            try {
                response.redirect("/table/" + profId + "/" + assignmentId);
            } catch (IllegalStateException e) {
                System.out.println("Response state message. Does not impact functionality.");
            }
            return null;
        }, new HandlebarsTemplateEngine());
    }

    //method to get calendar page
    private static void getCalendar(JDBCCourseCRUDPersister courseCRUD, JDBCAssignmentCRUDPersister assignmentCRUD,
                                    JDBCProfessorCRUDPersister professorCRUD) {
        get("/calendar/:profId/:courseId", ((request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("professorList", professorCRUD.findByID(request.params(":profId")));
            model.put("courseList", courseCRUD.findByPCID(request.params(":profId"), request.params(":courseId")));
            model.put("assignmentList", assignmentCRUD.findByCD(request.params(":courseId")));
            model.put("sidebarList", courseCRUD.findByPID(request.params(":profId")));
            return new ModelAndView(model, "CalendarView.hbs");
        }),  new HandlebarsTemplateEngine());
    }

    //post method for calendar
    private static void postCalendar(JDBCCourseCRUDPersister courseCRUD, JDBCAssignmentCRUDPersister assignmentCRUD) {
        post("/postCalendar", ((request, response) -> {
            String added = request.queryParams("added");
            String courseId = request.queryParams("courseId");
            String profId = request.queryParams("profId");
            List<Course> courseList = courseCRUD.cfindByCID(courseId);
            List<Assignment> assignmentList = assignmentCRUD.findByCD(courseId);
            if(added != null) {
                Course c = courseList.get(0);
                c.setAdded("true");
                //System.out.println(c.getAdded());
                courseCRUD.update(c);

                for (Assignment a : assignmentList) {
                    if (a.getAdded().equals("false")) {
                        a.setAdded("true");
                        assignmentCRUD.update(a);
                    }
                }
            }
            response.redirect("/calendar/" + profId + "/" + courseId);
            return null;
        }), new HandlebarsTemplateEngine());
    }

    /* method for professors to upload a list of student phone numbers */
    @SuppressWarnings("unchecked")
    private static void numberAdd(JDBCProfessorCRUDPersister professorCRUD) {
        post("/numberAdd", ((request, response) -> {
            if (request.raw().getAttribute("org.eclipse.jetty.multipartConfig") == null) {
                MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
                request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
            }

            String profId = request.queryParams("profIdn");
            String courseId = request.queryParams("courseIdn");

            /* read in YAML file */
            Part partBytes = request.raw().getPart("numbers");
            InputStream ios = partBytes.getInputStream();

            ArrayList<String> key = new ArrayList<String>();
            ArrayList<String> value = new ArrayList<String>();
            Yaml yaml = new Yaml();
            try {
                //InputStream ios = new FileInputStream(new File(fileName));
                // Parse the YAML file and return the output as a series of Maps and Lists
                Map<String, Object> result = (Map<String, Object>) yaml.load(ios);
                for (Object name : result.keySet()) {
                    key.add(name.toString());
                    value.add(result.get(name).toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /* use json to format input array into a string for database */
            /*
            Gson gson = new Gson();
            String inputString = gson.toJson(value);

             */
            String inputString = "";
            for (int i = 0; i < value.size(); i++) {
                inputString += value.get(i);
            }

            String username = request.queryParams("username");
            String password = request.queryParams("password");
            String phoneNum = request.queryParams("phoneNum");
            /* update professor info in database */
            professorCRUD.updateTwilio(profId, inputString, username, password, phoneNum);

            try {
                response.redirect("/table/" + profId + "/" + courseId);
            } catch (IllegalStateException e) {
                System.out.println("Response state message. Does not impact functionality.");
            }
            return null;
        }), new HandlebarsTemplateEngine());
    }

    /* Developed using twilio help documentation */

    //method to send text alert on assignment add
    private static void sendAddAlert(String ACCOUNT_SID, String AUTH_TOKEN, String studentTask, String dueDate, ArrayList<String> numbers, String phoneNum) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        for (int i = 0; i < numbers.size(); i++) {
            Message message = Message
                    .creator(new PhoneNumber(numbers.get(i)), // to
                            new PhoneNumber(phoneNum), // from
                            "New Assignment! " + studentTask + " is due " + dueDate)
                    .create();
        }

    }

    /* Developed using twilio help documentation */

    //method to send text alert on assignment edit
    private static void sendEditAlert(String ACCOUNT_SID, String AUTH_TOKEN, String studentTask, String dueDate, ArrayList<String> numbers, String phoneNum) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        for (int i = 0; i < numbers.size(); i++) {
            Message message = Message
                    .creator(new PhoneNumber(numbers.get(i)), // to
                            new PhoneNumber(phoneNum), // from
                            "Assignment edited! " + studentTask + " is due " + dueDate)
                    .create();
        }

    }

    /* from reading about deploying with heroku */
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

} //end class webServer