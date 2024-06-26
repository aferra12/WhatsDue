package model;

import java.util.Objects;

public class Course {
    private String courseId;
    private String profId;
    private String courseName;
    private String professorName;
    private String startDate;
    private String endDate;
    private String lectureDaysOfWeek;
    private String lectureStartTime;
    private String lectureEndTime;
    private String ohDaysOfWeek;
    private String ohStartTime;
    private String ohEndTime;
    private String added;


    public Course(String courseId, String profId, String courseName, String professorName, String startDate,
                  String endDate, String lectureDaysOfWeek, String lectureStartTime,
                  String lectureEndTime, String ohDaysOfWeek, String ohStartTime, String ohEndTime, String added) {
        this.courseId = courseId;
        this.profId = profId;
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
        this.added = added;
    }

    public String getAdded() {return added; }

    public void setAdded(String added) { this.added = added; }


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProfId() {
        return profId;
    }

    public void setProfId(String profId) {
        this.profId = profId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLectureDaysOfWeek() {
        return lectureDaysOfWeek;
    }

    public void setLectureDaysOfWeek(String lectureDaysOfWeek) {
        this.lectureDaysOfWeek = lectureDaysOfWeek;
    }

    public String getLectureStartTime() {
        return lectureStartTime;
    }

    public void setLectureStartTime(String lectureStartTime) {
        this.lectureStartTime = lectureStartTime;
    }

    public String getLectureEndTime() {
        return lectureEndTime;
    }

    public void setLectureEndTime(String lectureEndTime) {
        this.lectureEndTime = lectureEndTime;
    }

    public String getOhDaysOfWeek() {
        return ohDaysOfWeek;
    }

    public void setOhDaysOfWeek(String ohDaysOfWeek) {
        this.ohDaysOfWeek = ohDaysOfWeek;
    }

    public String getOhStartTime() {
        return ohStartTime;
    }

    public void setOhStartTime(String ohStartTime) {
        this.ohStartTime = ohStartTime;
    }

    public String getOhEndTime() {
        return ohEndTime;
    }

    public void setOhEndTime(String ohEndTime) {
        this.ohEndTime = ohEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return getCourseId().equals(course.getCourseId()) && getProfId().equals(course.getProfId()) &&
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
                "courseId='" + courseId + "' profId='" + profId + '\'' +
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
        return Objects.hash(getCourseId(),getProfId(), getCourseName(), getProfessorName(),
                getStartDate(), getEndDate(), getLectureDaysOfWeek(), getLectureStartTime(),
                getLectureEndTime(), getOhDaysOfWeek(), getOhStartTime(), getOhEndTime());
    }
}