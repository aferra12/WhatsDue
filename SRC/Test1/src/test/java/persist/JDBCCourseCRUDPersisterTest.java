/* Built off of code from hw 2 */

package persist;

import exceptions.CrudException;
import model.Course;
import model.Professor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class JDBCCourseCRUDPersisterTest {

    private Connection conn;
    private JDBCCourseCRUDPersister courseCrud;
    private JDBCProfessorCRUDPersister professorCrud;
    private Professor professor;

    @Before
    public void setUp() throws SQLException {
        final String URI = "jdbc:sqlite:./Test.db";
        conn = DriverManager.getConnection(URI);

        String sql;
        Statement st = conn.createStatement();

        sql = "DROP TABLE IF EXISTS Professors;";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Professors (profId VARCHAR(14) PRIMARY KEY, professorName VARCHAR(100), calURL VARCHAR(1000), digits VARCHAR(5000), username VARCHAR(100), password VARCHAR(100), phoneNum VARCHAR(100));";
        st.execute(sql);

        sql = "DROP TABLE IF EXISTS Courses;";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Courses (courseId VARCHAR(14) PRIMARY KEY, profId VARCHAR(14), courseName VARCHAR(30) NOT NULL, professorName VARCHAR(100), startDate VARCHAR(20), endDate VARCHAR(20), lectureDaysOfWeek VARCHAR(7), lectureStartTime VARCHAR(10), lectureEndTime VARCHAR(10), ohDaysOfWeek VARCHAR(7), ohStartTime VARCHAR(10), ohEndTime VARCHAR(10), added VARCHAR(100), CONSTRAINT f_key FOREIGN KEY (profId) REFERENCES Professors(profId));";
        st.execute(sql);

        sql = "PRAGMA foreign_keys = ON;";
        st.execute(sql);

        courseCrud = new JDBCCourseCRUDPersister(conn);
        professorCrud = new JDBCProfessorCRUDPersister(conn);

        professor = new Professor("1", "Madooei", null, "7", "7", "7", "7");
        professorCrud.create(professor);

    }

    @Test
    public void readCourseWorks() throws SQLException {
        Course c1 = new Course("EN.660.421", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        courseCrud.create(c1);
        Course c2 = courseCrud.read(c1.getCourseName());
        assertEquals(c1, c2);
    }

    @Test
    public void updateCourseWorks() throws SQLException {
        Course c1 = new Course("EN.660.421", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        courseCrud.create(c1);
        c1.setEndDate("May 6th");
        courseCrud.update(c1);
        Course c2 = courseCrud.read(c1.getCourseName());
        assertEquals(c1, c2);
    }

    @Test
    public void deleteCourseWorks() throws SQLException {
        Course c1 = new Course("EN.660.421", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        courseCrud.create(c1);
        courseCrud.delete(c1.getCourseId());
        Course c2 = courseCrud.read(c1.getCourseName());
        /* make sure c1 was deleted */
        assertNull(c2);
    }

    /* you can not add a course without a course name */
    @Test(expected = CrudException.class)
    public void addingCourseWithNullNameFails() throws SQLException {
        Course c1 = new Course("EN.660.421", "1", null, "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        courseCrud.create(c1);
    }

    @Test
    public void readCoursesWorks() throws SQLException {
        Professor p1 = new Professor("2", "Matt Green", null);
        professorCrud.create(p1);

        Professor p2 = new Professor("3", "Steve", null);
        professorCrud.create(p2);

        Course c1 = new Course("EN.660.421", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        Course c2 = new Course("EN.660.400", "2", "Data Structures", "Matt Green", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        Course c3 = new Course("EN.660.500", "3", "Java", "Steve", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");

        courseCrud.create(c1);
        courseCrud.create(c2);
        courseCrud.create(c3);

        List<Course> results = courseCrud.readAll("Green");
        assertTrue(results.contains(c1));
        assertTrue(results.contains(c2));
        assertTrue(results.contains(c3));
    }

    @Test
    public void readCoursesByProfIdWorks() throws SQLException {
        Professor p1 = new Professor("2", "Matt Green", null);
        professorCrud.create(p1);

        Professor p2 = new Professor("3", "Steve", null);
        professorCrud.create(p2);

        Course c1 = new Course("EN.660.421", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        Course c2 = new Course("EN.660.400", "2", "Data Structures", "Matt Green", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        Course c3 = new Course("EN.660.500", "3", "Java", "Steve", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");

        courseCrud.create(c1);
        courseCrud.create(c2);
        courseCrud.create(c3);

        /* courses c1 and c3 should not be there */
        List<Course> results = courseCrud.findByPID("2");
        assertFalse(results.contains(c1));
        assertTrue(results.contains(c2));
        assertFalse(results.contains(c3));
    }


    @Test
    public void readCoursesByCourseIdWorks() throws SQLException {
        Professor p1 = new Professor("2", "Matt Green", null);
        professorCrud.create(p1);

        Professor p2 = new Professor("3", "Steve", null);
        professorCrud.create(p2);

        Course c1 = new Course("EN.660.421", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        Course c2 = new Course("EN.660.400", "2", "Data Structures", "Matt Green", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        Course c3 = new Course("EN.660.500", "3", "Java", "Steve", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");

        courseCrud.create(c1);
        courseCrud.create(c2);
        courseCrud.create(c3);
        /* courses c2 and c3 should not be there */
        List<Course> results = courseCrud.cfindByCID("EN.660.421");
        assertTrue(results.contains(c1));
        assertFalse(results.contains(c2));
        assertFalse(results.contains(c3));
    }

    @Test
    public void readCoursesByProfandCourseIdWorks() throws SQLException {
        Professor p1 = new Professor("2", "Matt Green", null);
        professorCrud.create(p1);

        Professor p2 = new Professor("3", "Steve", null);
        professorCrud.create(p2);

        Course c1 = new Course("EN.660.421", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        Course c2 = new Course("EN.660.400", "2", "Data Structures", "Matt Green", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        Course c3 = new Course("EN.660.500", "3", "Java", "Steve", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");

        courseCrud.create(c1);
        courseCrud.create(c2);
        courseCrud.create(c3);
        /* courses c1 and c3 should not be there */
        List<Course> results = courseCrud.findByPCID("2", "EN.660.400");
        assertFalse(results.contains(c1));
        assertTrue(results.contains(c2));
        assertFalse(results.contains(c3));
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}

