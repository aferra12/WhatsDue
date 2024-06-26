/* written based on work done in jhu oose */

package dao;

import model.Professor;

import java.util.List;

public interface ProfessorDao {
    void add (Professor professor);
    List<Professor> findAll();
}
