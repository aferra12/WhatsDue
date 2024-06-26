/* based on hw2 code */

package persist;

import exceptions.CrudException;
import model.Professor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;

public class JDBCProfessorCRUDPersisterTest {

    private Connection conn;
    private JDBCProfessorCRUDPersister professorCrud;

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

        professorCrud = new JDBCProfessorCRUDPersister(conn);
    }

    @Test
    public void readProfessorWorks() throws SQLException {
        Professor p1 = new Professor("3", "Ali Madooei", null);
        professorCrud.create(p1);
        Professor p2 = professorCrud.read(p1.getName());
        assertEquals(p1, p2);
    }

    @Test
    public void updateProfessorWorks() throws SQLException {
        Professor p1 = new Professor("5", "Ali Madooei", null);
        professorCrud.create(p1);

        p1.setName("Matt Green");
        professorCrud.update(p1);

        Professor p2 = professorCrud.read(p1.getName());
        assertEquals(p1, p2);
    }

    @Test
    public void deleteProfessorWorks() throws SQLException {
        Professor p1 = new Professor("4", "Ali Madooei", null);
        professorCrud.create(p1);

        professorCrud.delete(p1.getId());
        Professor p2 = professorCrud.read(p1.getName());
        assertNull(p2);
    }

    @Test(expected = CrudException.class)
    public void addingProfessorWithNullNameFails() throws SQLException {
        Professor p1 = new Professor(null, null, null);
        professorCrud.create(p1);
    }

    @Test
    public void readAllProfessorWorks() throws SQLException {
        Professor p1 = new Professor("6", "Ali Madooei", null);
        Professor p2 = new Professor("7", "Matt Green", null);
        Professor p3 = new Professor("8", "Avi Rubin", null);

        professorCrud.create(p1);
        professorCrud.create(p2);
        professorCrud.create(p3);

        List<Professor> results = professorCrud.readAll("Rubin");
        assertTrue(results.contains(p1));
        assertTrue(results.contains(p2));
        assertTrue(results.contains(p3));
    }

    @Test
    public void readProfessorByIdWorks() throws SQLException {
        Professor p1 = new Professor("6", "Ali Madooei", "7", "7", "7", "7", "7");
        Professor p2 = new Professor("7", "Matt Green", null, "7", "7", "7", "7");
        Professor p3 = new Professor("8", "Avi Rubin", null, "7", "7", "7", "7");

        professorCrud.create(p1);
        professorCrud.create(p2);
        professorCrud.create(p3);

        List<Professor> results = professorCrud.findByID("6");
        assertTrue(results.contains(p1));
        assertFalse(results.contains(p2));
        assertFalse(results.contains(p3));
    }

    @Test
    public void updateURLWorks() throws SQLException {
        Professor p1 = new Professor("6", "Ali Madooei", null);
        Professor p2 = new Professor("7", "Matt Green", null);
        Professor p3 = new Professor("8", "Avi Rubin", null);

        professorCrud.create(p1);
        professorCrud.create(p2);
        professorCrud.create(p3);


        professorCrud.updateURL("6", "123");

        String sql = "SELECT * FROM Professors WHERE profId = 6;";
        PreparedStatement pst = null;

        Professor professor = null;

        try {
            pst = conn.prepareStatement(sql);
            //use % for formatting to use LIKE command
            //String search = "%" + professorName + "%";
            //pst.setString(1, 6);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                professor = new Professor(
                        rs.getString("profId"),
                        rs.getString("professorName"),
                        rs.getString("calURL")

                );
                //course.setId(rs.getString("id"));
                //professors.add(professor);
            }
            p1 = professor;

        } catch (SQLException e) {
            throw new CrudException("Unable to read the prof", e);
        }

        assertEquals("123", p1.getCalURL());

    }

    @After
    public void tearDown() throws SQLException {
        conn.close();
    }
}
