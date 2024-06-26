package model;

import java.util.Objects;

public class Course {
    private String id;
    private String courseName;
    private String professorName;
    private String startDate;
    private String endDate;
    private String lectureDaysOfWeek; //turn into List<String> later
    private String lectureStartTime;
    private String lectureEndTime;
    private String ohDaysOfWeek;
    private String ohStartTime;
    private String ohEndTime;

    public Course(String id, String courseName, String professorName, String startDate,
                  String endDate, String lectureDaysOfWeek, String lectureStartTime,
                  String lectureEndTime, String ohDaysOfWeek, String ohStartTime, String ohEndTime) {
        this.id = id;
        this.courseName = courseName;
        this.professorName = professorName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lectureDaysOfWeek = lectureDaysOfWeek;
        this.lectureStartTime = lectureStartTime;
        this.lectureEndTime = lectureEndTime;
        this.ohDaysOfWeek = ohDaysOfWeek;
        this.ohStartTime = ohStartTime;
        this.ohEndTime = ohEndTime;
    }

    public String getId() {
        return id;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLectureDaysOfWeek() {
        return lectureDaysOfWeek;
    }

    public String getLectureStartTime() {
        return lectureStartTime;
    }

    public String getLectureEndTime() {
        return lectureEndTime;
    }

    public String getOhDaysOfWeek() {
        return ohDaysOfWeek;
    }

    public String getOhStartTime() {
        return ohStartTime;
    }

    public String getOhEndTime() {
        return ohEndTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLectureDaysOfWeek(String lectureDaysOfWeek) {
        this.lectureDaysOfWeek = lectureDaysOfWeek;
    }

    public void setLectureStartTime(String lectureStartTime) {
        this.lectureStartTime = lectureStartTime;
    }

    public void setLectureEndTime(String lectureEndTime) {
        this.lectureEndTime = lectureEndTime;
    }

    public void setOhDaysOfWeek(String ohDaysOfWeek) {
        this.ohDaysOfWeek = ohDaysOfWeek;
    }

    public void setOhStartTime(String ohStartTime) {
        this.ohStartTime = ohStartTime;
    }

    public void setOhEndTime(String ohEndTime) {
        this.ohEndTime = ohEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return getId() == course.getId() &&
                getCourseName().equals(course.getCourseName()) &&
                Objects.equals(getProfessorName(), course.getProfessorName()) &&
                Objects.equals(getStartDate(), course.getStartDate()) &&
                Objects.equals(getEndDate(), course.getEndDate()) &&
                Objects.equals(getLectureDaysOfWeek(), course.getLectureDaysOfWeek()) &&
                Objects.equals(getLectureStartTime(), course.getLectureStartTime()) &&
                Objects.equals(getLectureEndTime(), course.getLectureEndTime()) &&
                Objects.equals(getOhDaysOfWeek(), course.getOhDaysOfWeek()) &&
                Objects.equals(getOhStartTime(), course.getOhStartTime()) &&
                Objects.equals(getOhEndTime(), course.getOhEndTime());
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", professorName='" + professorName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", lectureDaysOfWeek='" + lectureDaysOfWeek + '\'' +
                ", lectureStartTime='" + lectureStartTime + '\'' +
                ", lectureEndTime='" + lectureEndTime + '\'' +
                ", ohDaysOfWeek='" + ohDaysOfWeek + '\'' +
                ", ohStartTime='" + ohStartTime + '\'' +
                ", ohEndTime='" + ohEndTime + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCourseName(), getProfessorName(),
                getStartDate(), getEndDate(), getLectureDaysOfWeek(), getLectureStartTime(),
                getLectureEndTime(), getOhDaysOfWeek(), getOhStartTime(), getOhEndTime());
    }
}