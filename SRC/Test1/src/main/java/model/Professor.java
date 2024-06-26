package model;

import java.util.Objects;

public class Professor {
    private String profId;
    private String professorName;
    private String calURL;
    private String digits;
    private String username;
    private String password;
    private String phoneNum;

    public Professor(String profId, String name, String calURL) {
        this.profId = profId;
        this.professorName = name;
        this.calURL = calURL;
    }

    public Professor(String profId, String name, String calURL, String digits, String username, String password, String phoneNum) {
        this.profId = profId;
        this.professorName = name;
        this.calURL = calURL;
        this.digits = digits;
        this.username = username;
        this.password = password;
        this.phoneNum = phoneNum;
    }

    public String getPhoneNum() {return phoneNum;}

    public void setPhoneNum(String phoneNum) {this.phoneNum = phoneNum;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDigits() {return digits; }

    public void setDigits(String digits) {this.digits = digits; }

    public String getCalURL() {return calURL; }

    public void setCalURL(String url) {
        this.calURL = url;
    }

    public String getId() { return profId; }

    public void setId(String id) { this.profId = id; }

    public String getName() {
        return professorName;
    }

    public void setName(String name) {
        this.professorName = name;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "professorName='" + professorName + '\'' +
                ", id='" + profId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Professor)) return false;
        Professor professor = (Professor) o;
        return Objects.equals(getName(), professor.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}


