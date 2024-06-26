package dao;

import model.Assignment;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryAssignmentDao implements AssignmentDao {
    private List<Assignment> assignmentList;
    public InMemoryAssignmentDao() {
        assignmentList = new ArrayList<>();
    }
    @Override
    public void add(Assignment assignment) {
        assignmentList.add(assignment);
    }
    @Override
    public List<Assignment> findAll() {
        return Collections.unmodifiableList(assignmentList);
    }
    /**
     * The following methods were developed with help from sqlitetutorial.net
     */

    /* update doc in assignments table */
    public void updateDoc(String studentTask, byte[] fileBytes, Connection conn, String courseId) {
        // update sql
        String updateSQL = "UPDATE Assignments "
                + "SET doc = ? "
                + "WHERE studentTask=? AND assignmentId = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            // set parameters
            pstmt.setBytes(1, fileBytes);
            pstmt.setString(2, studentTask);
            pstmt.setString(3, courseId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Response download(String filename, Connection conn, Response response, String courseId) throws SQLException, IOException {
        int DEFAULT_BUFFER_SIZE = 4000;

        // Read blob from db
        String selectSQL = "SELECT doc FROM Assignments WHERE studentTask=? AND assignmentId = ?";
        PreparedStatement pstmt = conn.prepareStatement(selectSQL);
        pstmt.setString(1, filename);
        pstmt.setString(2, courseId);
        ResultSet rs = pstmt.executeQuery();
        // Set up response header
        response.type("application/force-download");
        response
                .header("Content-disposition", "attachment; filename=\""
                        + "Download.pdf" + "\"");

        // Set up response body buffer
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.raw()
                .getOutputStream(), DEFAULT_BUFFER_SIZE);
        while (rs.next()) {
            InputStream input = rs.getBinaryStream("doc");
            byte[] buffer = new byte[1024];
            // Write blob bytes to response buffer
            while (input.read(buffer) > 0) {
                bufferedOutputStream.write(buffer);
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
        }
        response.raw().getOutputStream().close();
        return response;
    }

}
