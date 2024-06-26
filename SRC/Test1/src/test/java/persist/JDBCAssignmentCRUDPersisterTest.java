/* based on hw2 code */

package persist;

import exceptions.CrudException;
import model.Professor;
import model.Course;
import model.Assignment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.*;

public class JDBCAssignmentCRUDPersisterTest {

    private Connection conn;
    private JDBCAssignmentCRUDPersister assignmentCRUD;

    @Before
    public void setUp() throws SQLException {
        final String URI = "jdbc:sqlite:./Test.db";
        conn = DriverManager.getConnection(URI);

        String sql;
        Statement st = conn.createStatement();

        /* DROP makes sure the tables are clear at the start of the test */

        sql = "DROP TABLE IF EXISTS Professors;";
        st.execute(sql);

        sql = "DROP TABLE IF EXISTS Courses;";
        st.execute(sql);

        sql = "DROP TABLE IF EXISTS Assignments;";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Professors (profId VARCHAR(14) PRIMARY KEY, professorName VARCHAR(100), calURL VARCHAR(1000), digits VARCHAR(5000), username VARCHAR(100), password VARCHAR(100), phoneNum VARCHAR(100));";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Courses (courseId VARCHAR(14) PRIMARY KEY, profId VARCHAR(14), courseName VARCHAR(30) NOT NULL, professorName VARCHAR(100), startDate VARCHAR(20), endDate VARCHAR(20), lectureDaysOfWeek VARCHAR(7), lectureStartTime VARCHAR(10), lectureEndTime VARCHAR(10), ohDaysOfWeek VARCHAR(7), ohStartTime VARCHAR(10), ohEndTime VARCHAR(10), added VARCHAR(50), FOREIGN KEY(profId) REFERENCES Professors(profId) ON DELETE CASCADE);";
        st.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS Assignments (id INTEGER PRIMARY KEY AUTOINCREMENT, assignmentId VARCHAR(14), assignmentNumber VARCHAR(5), studentTask VARCHAR(30), dueDate VARCHAR(14), doc BLOB, added VARCHAR(50), materialType VARCHAR(100), FOREIGN KEY(assignmentId) REFERENCES Courses(courseId) ON DELETE CASCADE);";
        st.execute(sql);

        sql = "PRAGMA foreign_keys = ON;";
        st.execute(sql);

        JDBCCourseCRUDPersister courseCRUD = new JDBCCourseCRUDPersister(conn);
        JDBCProfessorCRUDPersister professorCRUD = new JDBCProfessorCRUDPersister(conn);
        assignmentCRUD = new JDBCAssignmentCRUDPersister(conn);

        /* add professor and courses so assignments can be added */
        Professor professor = new Professor("1", "Brian", null, "7", "7", "7", "7");
        professorCRUD.create(professor);
        Course c1 = new Course("1", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        courseCRUD.create(c1);
        Course c2 = new Course("2", "1", "OOSE", "Madooei", "January 28th", "May 5th", "TTH", "12:00PM", "1:15PM", "MWF", "2:00 PM", "3:00 PM", "false");
        courseCRUD.create(c2);
    }

    @Test
    public void readAssignmentsWorks() throws SQLException {
        Assignment a1 = new Assignment("1", "1", "HW1", "2020-04-10", "false","Homework");
        assignmentCRUD.create(a1);
        Assignment a2 = assignmentCRUD.read(a1.getAssignmentId());
        assertEquals(a1, a2);
    }

    @Test
    public void updateAssignmentWorks() throws SQLException {
        Assignment a1 = new Assignment("1", "1", "HW1", "2020-04-10", "false", "Homework");
        assignmentCRUD.create(a1);
        a1.setAssignmentNumber("2");
        a1.setStudentTask("HW2");
        a1.setDueDate("2020-04-11");
        assignmentCRUD.update(a1);
        assertEquals("2", a1.getAssignmentNumber());
        assertEquals("HW2", a1.getStudentTask());
        assertEquals("2020-04-11", a1.getDueDate());
    }

    @Test
    public void deleteAssignmentWorks() throws SQLException {
        Assignment a1 = new Assignment("1", "1", "HW1", "2020-04-10", "false", "Homework");
        assignmentCRUD.create(a1);
        assignmentCRUD.delete(a1.getStudentTask());
        Assignment a2 = assignmentCRUD.read(a1.getAssignmentId());
        /* make sure a1 was successfully deleted */
        assertNull(a2);
    }

    @Test
    public void readByCourseIdWorks() throws SQLException {
        Assignment a1 = new Assignment("1", "1", "HW1", "2020-04-10", "false", "Homework");
        Assignment a2 = new Assignment("1", "2", "HW2", "2020-04-11", "false", "Homework");
        Assignment a3 = new Assignment("2", "3", "HW3", "2020-04-12", "false", "Homework");
        assignmentCRUD.create(a1);
        assignmentCRUD.create(a2);
        assignmentCRUD.create(a3);

        List<Assignment> results = assignmentCRUD.findByCD("1");
        /* a3 should not be there because it has assignmentId 2 */
        assertTrue(results.contains(a1));
        assertTrue(results.contains(a2));
        assertFalse(results.contains(a3));


    }

    @Test
    public void readAllAssignmentsWorks() throws SQLException {
        Assignment a1 = new Assignment("1", "1", "HW1", "2020-04-10", "false", "Homework");
        Assignment a2 = new Assignment("1", "2", "HW2", "2020-04-11", "false", "Homework");
        Assignment a3 = new Assignment("1", "3", "HW3", "2020-04-12", "false", "Homework");


        assignmentCRUD.create(a1);
        assignmentCRUD.create(a2);
        assignmentCRUD.create(a3);

        List<Assignment> results = assignmentCRUD.readAll("1");
        assertTrue(results.contains(a1));
        assertTrue(results.contains(a2));
        assertTrue(results.contains(a3));
    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}

