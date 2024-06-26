//CODE TAKEN FROM WORK DONE IN LECTURE
package persist;

import exceptions.CrudException;
import model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCCourseCRUDPersister implements CRUDPersister<Course> {
    private Connection conn;

    public JDBCCourseCRUDPersister(Connection conn) {
        this.conn = conn;
    }

    /* create a new course entry in SQLite table */
    @Override
    public void create(Course course) throws CrudException, SQLException {
        try {
            String name = course.getCourseName();
            if (name == null) {
                throw new SQLException("null name");
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to create the course due to null name", e);
        }
        String sql = "INSERT INTO Courses(courseId, profId, courseName, professorName, startDate, endDate, lectureDaysOFWeek, lectureStartTime, lectureEndTime, ohDaysOfWeek, ohStartTime, ohEndTime, added) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, course.getCourseId());
            pst.setString(2, course.getProfId());
            pst.setString(3, course.getCourseName());
            pst.setString(4, course.getProfessorName());
            pst.setString(5, course.getStartDate());
            pst.setString(6, course.getEndDate());
            pst.setString(7, course.getLectureDaysOfWeek());
            pst.setString(8, course.getLectureStartTime());
            pst.setString(9, course.getLectureEndTime());
            pst.setString(10, course.getOhDaysOfWeek());
            pst.setString(11, course.getOhStartTime());
            pst.setString(12, course.getOhEndTime());
            pst.setString(13, course.getAdded());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new CrudException("Unable to create the course", e);
        }
    }

    /* read courses corresponding to a course name */
    @Override
    public Course read(String courseName) throws CrudException {
        String sql = "SELECT * FROM Courses WHERE courseName = ?;";
        PreparedStatement pst = null;
        Course course = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, courseName);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) return null;
            course = new Course(
                    rs.getString("courseId"),
                    rs.getString("profId"),
                    rs.getString("courseName"),
                    rs.getString("professorName"),
                    rs.getString("startDate"),
                    rs.getString("endDate"),
                    rs.getString("lectureDaysOfWeek"),
                    rs.getString("lectureStartTime"),
                    rs.getString("lectureEndTime"),
                    rs.getString("ohDaysOfWeek"),
                    rs.getString("ohStartTime"),
                    rs.getString("ohEndTime"),
                    rs.getString("added")
            );
        } catch (SQLException e) {
            throw new CrudException("Unable to read the course", e);
        }
        return course;
    }

    /* update a course at a specific course id */
    @Override
    public void update(Course course) {
        String sql = "UPDATE Courses SET profId = ?, courseName = ?, professorName = ?, startDate = ?, endDate = ?, lectureDaysOFWeek = ?, lectureStartTime = ?, lectureEndTime = ?, ohDaysOfWeek = ?, ohStartTime = ?, ohEndTime = ?, added = ? WHERE courseId = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, course.getProfId());
            pst.setString(2, course.getCourseName());
            pst.setString(3, course.getProfessorName());
            pst.setString(4, course.getStartDate());
            pst.setString(5, course.getEndDate());
            pst.setString(6, course.getLectureDaysOfWeek());
            pst.setString(7, course.getLectureStartTime());
            pst.setString(8, course.getLectureEndTime());
            pst.setString(9, course.getOhDaysOfWeek());
            pst.setString(10, course.getOhStartTime());
            pst.setString(11, course.getOhEndTime());
            pst.setString(12, course.getAdded());
            pst.setString(13, course.getCourseId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new CrudException("Unable to update the course", e);
        }
    }

    /* delete course based on course id */
    @Override
    public void delete(String courseId) throws CrudException {
        String sql = "DELETE FROM Courses WHERE courseId = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, courseId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new CrudException("Unable to delete course", e);
        }
    }

   /* read all courses in database */
    public List<Course> readAll(String professorName) throws CrudException {
        List<Course> courses = new ArrayList<Course>();
        if (professorName == null) {
            return null;
        }
        String sql = "SELECT * FROM Courses;";
        PreparedStatement pst = null;
        Course course = null;
        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                course = new Course(
                        rs.getString("courseId"),
                        rs.getString("profId"),
                        rs.getString("courseName"),
                        rs.getString("professorName"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("lectureDaysOfWeek"),
                        rs.getString("lectureStartTime"),
                        rs.getString("lectureEndTime"),
                        rs.getString("ohDaysOfWeek"),
                        rs.getString("ohStartTime"),
                        rs.getString("ohEndTime"),
                        rs.getString("added")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to read the course", e);
        }
        return courses;
    }

    /* find all courses corresponding to a course and professor id */
    public List<Course> findByPCID(String profId, String courseId) throws CrudException {
        List<Course> courses = new ArrayList<Course>();
        String sql = "SELECT * FROM Courses WHERE profId = ? AND courseId = ?;";
        PreparedStatement pst = null;
        Course course = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, profId);
            pst.setString(2, courseId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                course = new Course(
                        rs.getString("courseId"),
                        rs.getString("profId"),
                        rs.getString("courseName"),
                        rs.getString("professorName"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("lectureDaysOfWeek"),
                        rs.getString("lectureStartTime"),
                        rs.getString("lectureEndTime"),
                        rs.getString("ohDaysOfWeek"),
                        rs.getString("ohStartTime"),
                        rs.getString("ohEndTime"),
                        rs.getString("added")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to read the course", e);
        }
        return courses;
    }

    /* returns all courses corresponding to a specific professor id */
    public List<Course> findByPID(String profId) throws CrudException {
        List<Course> courses = new ArrayList<Course>();
        String sql = "SELECT * FROM Courses WHERE profId = ?;";
        PreparedStatement pst = null;
        Course course = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, profId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                course = new Course(
                        rs.getString("courseId"),
                        rs.getString("profId"),
                        rs.getString("courseName"),
                        rs.getString("professorName"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("lectureDaysOfWeek"),
                        rs.getString("lectureStartTime"),
                        rs.getString("lectureEndTime"),
                        rs.getString("ohDaysOfWeek"),
                        rs.getString("ohStartTime"),
                        rs.getString("ohEndTime"),
                        rs.getString("added")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to read the course", e);
        }
        return courses;
    }

    /* find all courses corresponding to a course id */
    public List<Course> cfindByCID(String courseId) throws CrudException {
        List<Course> courses = new ArrayList<Course>();
        String sql = "SELECT * FROM Courses WHERE courseId = ?;";
        PreparedStatement pst = null;
        Course course = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, courseId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                course = new Course(
                        rs.getString("courseId"),
                        rs.getString("profId"),
                        rs.getString("courseName"),
                        rs.getString("professorName"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getString("lectureDaysOfWeek"),
                        rs.getString("lectureStartTime"),
                        rs.getString("lectureEndTime"),
                        rs.getString("ohDaysOfWeek"),
                        rs.getString("ohStartTime"),
                        rs.getString("ohEndTime"),
                        rs.getString("added")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to read the course", e);
        }
        return courses;
    }

}
