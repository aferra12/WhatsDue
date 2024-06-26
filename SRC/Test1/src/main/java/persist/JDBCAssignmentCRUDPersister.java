/* structure based on work done in jhu oose */

package persist;

import exceptions.CrudException;
import model.Assignment;
import model.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCAssignmentCRUDPersister implements CRUDPersister<Assignment> {

    private Connection conn;

    public JDBCAssignmentCRUDPersister(Connection conn) {
        this.conn = conn;
    }

    /* create a new assignment entry in table */
    @Override
    public void create(Assignment assignment) throws CrudException, SQLException {
        try {
            String assignmentId = assignment.getAssignmentId();
            if (assignmentId == null) {
                throw new SQLException("null assignment id");
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to create the assignment due to null id", e);
        }
        String sql = "INSERT INTO Assignments(assignmentId, assignmentNumber, studentTask, dueDate, added, materialType) VALUES (?, ?, ?, ?, ?, ?);";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, assignment.getAssignmentId());
            pst.setString(2, assignment.getAssignmentNumber());
            pst.setString(3, assignment.getStudentTask());
            pst.setString(4, assignment.getDueDate());
            pst.setString(5, assignment.getAdded());
            pst.setString(6, assignment.getMaterialType());
            pst.executeUpdate();
        } catch (SQLException e) {
        throw new CrudException("Unable to create the Assignment", e);
        }
    }

    /* read assignments based on assignmentId */
    @Override
    public Assignment read(String assignmentId) throws CrudException {
        String sql = "SELECT * FROM Assignments WHERE assignmentId = ?;";
        PreparedStatement pst = null;
        Assignment assignment = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, assignmentId);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) return null;
            assignment = new Assignment(
                    rs.getString("assignmentId"),
                    rs.getString("assignmentNumber"),
                    rs.getString("studentTask"),
                    rs.getString("dueDate"),
                    rs.getString("added"),
                    rs.getString("materialType")
            );

        } catch (SQLException e) {
            throw new CrudException("Unable to read the course", e);
        }
        return assignment;
    }

    /* update an assignment at a specific id */
    @Override
    public void update(Assignment assignment) {
        String sql = "UPDATE Assignments SET assignmentNumber = ? , studentTask = ?, dueDate = ?, added = ?, materialType = ? WHERE id = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, assignment.getAssignmentNumber());
            pst.setString(2, assignment.getStudentTask());
            pst.setString(3, assignment.getDueDate());
            pst.setString(4, assignment.getAdded());
            pst.setString(5, assignment.getMaterialType());
            pst.setInt(6, assignment.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new CrudException("Unable to update the assignment", e);
        }
    }

    /* Finds all assignments based on certain assignmentId and returns list of these assignments */
    public List<Assignment> findByCD(String courseId) throws CrudException {
        List<Assignment> assignments = new ArrayList<Assignment>();
        String sql = "SELECT * FROM Assignments WHERE assignmentId = ?;";
        PreparedStatement pst = null;
        Assignment assignment = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, courseId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                assignment = new Assignment(
                        rs.getInt("id"),
                        rs.getString("assignmentId"),
                        rs.getString("assignmentNumber"),
                        rs.getString("studentTask"),
                        rs.getString("dueDate"),
                        rs.getString("added"),
                        rs.getString("materialType")

                );
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to read the course", e);
        }
        return assignments;
    }

    /* delete all assignments corresponding to a task and assignment id */
    public void deleteA(String studentTask, String courseId) throws CrudException {
        String sql = "DELETE FROM Assignments WHERE studentTask = ? AND assignmentId = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, studentTask);
            pst.setString(2, courseId);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new CrudException("Unable to delete assignment", e);
        }
    }

    /* this method was implemented to implement the CRUDPersister interface */
    @Override
    public void delete(String studentTask) throws CrudException {
        String sql = "DELETE FROM Assignments WHERE studentTask = ?;";
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            pst.setString(1, studentTask);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new CrudException("Unable to delete assignment", e);
        }
    }

    /* reads all assignments in database */
    public List<Assignment> readAll(String courseId) throws CrudException {
        List<Assignment> assignments = new ArrayList<Assignment>();
        String assignmentId = courseId;
        if (courseId == null) {
            return null;
        }
        String sql = "SELECT * FROM Assignments;";
        PreparedStatement pst = null;
        Assignment assignment = null;
        try {
            pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                assignment = new Assignment(
                        rs.getString("assignmentId"),
                        rs.getString("assignmentNumber"),
                        rs.getString("studentTask"),
                        rs.getString("dueDate"),
                        rs.getString("added"),
                        rs.getString("materialType")

                );
                assignments.add(assignment);
            }
        } catch (SQLException e) {
            throw new CrudException("Unable to read the assignment", e);
        }
        return assignments;
    }

}
