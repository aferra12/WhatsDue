//CODE TAKEN FROM WORK DONE IN LECTURE
package persist;

import exceptions.CrudException;
import model.Course;
import model.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCProfessorCRUDPersister implements CRUDPersister<Professor> {
    private Connection conn;

    public JDBCProfessorCRUDPersister(Connection conn) {
        this.conn = conn;
    }

    /* add a new professor entry to SQLite table */
    @Override
    public void create(Professor prof) throws CrudException, SQLException {
        try {
            String profId = prof.getId();
            if (profId == null) {
                throw new SQLException("null id");
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to create the professor due to null id", e);
        }
        try {
            String name = prof.getName();
            if (name == null) {
                throw new SQLException("null name");
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to create the professor due to null name", e);
        }
        String sql = "INSERT INTO Professors(profId, professorName, calURL, digits, username, password, phoneNum) VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, prof.getId());
            pst.setString(2, prof.getName());
            pst.setString(3, prof.getCalURL());
            pst.setString(4, prof.getDigits());
            pst.setString(5, prof.getUsername());
            pst.setString(6, prof.getPassword());
            pst.setString(7, prof.getPhoneNum());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new CrudException("Unable to create the professor", e);
        }
    }

    /* read all professors with a specific name */
    @Override
    public Professor read(String name) throws CrudException {
        String sql = "SELECT * FROM Professors WHERE professorName = ?;";
        PreparedStatement pst = null;
        Professor prof = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) return null;
            prof = new Professor(rs.getString("profId"),
                    rs.getString("professorName"),
                    rs.getString("calURL")
            );
        } catch (SQLException e) {
            throw new CrudException("Unable to read the professor", e);
        }
        return prof;
    }

    /* find all professors corresponding to specific professor id */
    public List<Professor> findByID(String profId) throws CrudException {
        List<Professor> professors = new ArrayList<Professor>();
        String sql = "SELECT * FROM Professors WHERE profId = ?;";
        PreparedStatement pst = null;
        Professor professor = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, profId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                professor = new Professor(
                        rs.getString("profId"),
                        rs.getString("professorName"),
                        rs.getString("calURL"),
                        rs.getString("digits"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("phoneNum")
                );
                professors.add(professor);
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to read the prof", e);
        }
        return professors;
    }

    /* update a professor entry corresponding to specific professor id */
    @Override
    public void update(Professor prof) {
        String sql = "UPDATE Professors SET professorName = ? WHERE profId = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, prof.getName());
            pst.setString(2, prof.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new CrudException("Unable to update the professor", e);
        }
    }

    /* update a professors calendar url */
    public void updateURL(String profId, String URL) throws CrudException {
        String sql = "UPDATE Professors SET calURL = ? WHERE profId = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, URL);
            pst.setString(2, profId);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new CrudException("Unable to update professor url", e);
        }
    }

    /* update a professors calendar url */
    public void updateTwilio(String profId, String digits, String username, String password, String phoneNum) throws CrudException {
        String sql = "UPDATE Professors SET digits = ?, username = ?, password = ?, phoneNum = ? WHERE profId = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, digits);
            pst.setString(2, username);
            pst.setString(3, password);
            pst.setString(4, phoneNum);
            pst.setString(5, profId);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new CrudException("Unable to update professor digits", e);
        }
    }

    /* delete a professor entry from table */
    @Override
    public void delete(String profId) throws CrudException {
        String sql = "DELETE FROM Professors WHERE profId = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, profId);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.toString());
            throw new CrudException("Unable to delete professor", e);
        }
    }

    /* return all professors in database */
    public List<Professor> readAll(String name) throws CrudException {
        List<Professor> professors = new ArrayList<Professor>();
        if (name == null) {
            return null;
        }
        String sql = "SELECT * FROM Professors;";
        PreparedStatement pst = null;
        Professor prof = null;
        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                prof = new Professor(rs.getString("profId"),
                        rs.getString("professorName"),
                        rs.getString("calURL")
                );
                professors.add(prof);
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to read the professor", e);
        }
        return professors;
    }

}
