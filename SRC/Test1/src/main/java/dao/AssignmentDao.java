/* created using formats from jhu oose lectures */

package dao;

import model.Assignment;
import model.Course;
import spark.Response;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface AssignmentDao {
    void add (Assignment assignment);
    List<Assignment> findAll();
    void updateDoc(String studentTask, byte[] fileBytes, Connection conn, String courseId);
    /*method to download file from database */
    Response download(String filename, Connection conn, Response response, String courseId) throws IOException, SQLException;
}
