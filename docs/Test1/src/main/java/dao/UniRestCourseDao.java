/**
 * This was written in lecture 7 in class.
 */

package dao;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.Course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UniRestCourseDao implements CourseDao {

    public final static String BASE_URL = "http://127.0.0.1:7000/";
    private static Gson gson = new Gson();


    @Override
    public void add(Course course) { ;
        final String URL = BASE_URL + "courses";
        try {
            Unirest.post(URL).body(gson.toJson(course)).asJson();
        } catch (UnirestException e) {
            // TODO error
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> findAll() {
        final String URL = BASE_URL + "courses";
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get(URL).asJson();
            Course[] courses = gson.fromJson(jsonResponse.getBody().toString(), Course[].class);
            return new ArrayList<>(Arrays.asList(courses));
        } catch (UnirestException e) {
            // TODO Error
            e.printStackTrace();
        }

        return null;
    }
}
