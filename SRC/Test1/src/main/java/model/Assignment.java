package model;

import java.util.Objects;

public class Assignment {

    private int id;
    private String assignmentId;
    private String assignmentNumber;
    private String studentTask;
    private String dueDate;
    private String added;
    private String materialType;

    public Assignment(String assignmentId, String assignmentNumber, String studentTask, String dueDate, String added, String materialType) {

        this.assignmentId = assignmentId;
        this.assignmentNumber = assignmentNumber;
        this.studentTask = studentTask;
        this.dueDate = dueDate;
        this.added = added;
        this.materialType = materialType;
    }

    /* constructor for adding assignment with specific id as primary key in SQLite table */
    public Assignment(int id, String assignmentId, String assignmentNumber, String studentTask, String dueDate, String added, String materialType) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.assignmentNumber = assignmentNumber;
        this.studentTask = studentTask;
        this.dueDate = dueDate;
        this.added = added;
        this.materialType = materialType;
    }
    
    public int getId() {return id;}

    public void setId(int id) { this.id = id;}

    public String getMaterialType() { return materialType; }

    public void setMaterialType(String materialType) { this.materialType = materialType; }

    public String getAdded() { return added; }

    public void setAdded(String added) { this.added = added;}

    public String getAssignmentId() {return assignmentId; }

    public String getAssignmentNumber() {
        return assignmentNumber;
    }

    public String getStudentTask() {
        return studentTask;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setAssignmentId(String assignmentId) { this.assignmentId = assignmentId; }

    public void setAssignmentNumber(String assignmentNumber) {
        this.assignmentNumber = assignmentNumber;
    }

    public void setStudentTask(String studentTask) {
        this.studentTask = studentTask;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Assignment)) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(getAssignmentId(), that.getAssignmentId()) &&
                Objects.equals(getAssignmentNumber(), that.getAssignmentNumber()) &&
                Objects.equals(getStudentTask(), that.getStudentTask()) &&
                Objects.equals(getDueDate(), that.getDueDate());

    }

    @Override
    public int hashCode() {
        return Objects.hash(getAssignmentId(), getAssignmentNumber(), getStudentTask(), getDueDate());
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "assignmentId='" + assignmentId + '\'' +
                ", assignmentNumber='" + assignmentNumber + '\'' +
                ", studentTask='" + studentTask + '\'' +
                ", dueDate='" + dueDate + '\'' +
                '}';
    }
}
