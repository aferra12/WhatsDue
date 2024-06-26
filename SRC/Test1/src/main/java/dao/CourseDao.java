/**
 * This was written in lecture 7 in class.
 */

package dao;

import model.Course;

import java.util.List;

public interface CourseDao {
    void add (Course course);
    List<Course> findAll();
}