package dao;

import model.Professor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryProfessorDao implements ProfessorDao{
    private List<Professor> professorList;
    public InMemoryProfessorDao() {
        professorList = new ArrayList<>();
    }
    @Override
    public void add(Professor prof) {
        professorList.add(prof);
    }
    @Override
    public List<Professor> findAll() {
        return Collections.unmodifiableList(professorList);
    }
}
