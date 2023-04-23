package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class DataService {

    Context context;
    //String idd;

    public DataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListner{

        void onResponse(String id, String name, String dob, String gender, String street,String city, String pin, String state, String mobile, String email);
        void onError(String message);

    }

    public interface ResponseListner{

        void onResponse(HashMap<Integer, String>[] Users);
        void onError(String message);

    }

    public interface Listner{

        void onResponse(int[] total);

    }

    public interface VolleyListner{

        void onResponse(float[] count);

    }


    public void getId(VolleyResponseListner volleyResponseListner){
        String username ;
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("username", "error");

        String url = "https://dummyjson.com/users/search?q=" + username;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String id, firstname,lastname, dob, gender, street, city, pin, state, mobile, email;
                try {
                    JSONArray details = response.getJSONArray("users");
                    JSONObject user = details.getJSONObject(0);
                    id = user.getString("id");
                    firstname = user.getString("firstName");
                    lastname = user.getString("lastName");
                    dob = user.getString("birthDate");
                    gender = user.getString("gender");
                    street = user.getJSONObject("address").getString("address");
                    city = user.getJSONObject("address").getString("city");
                    pin = user.getJSONObject("address").getString("postalCode");
                    state = user.getJSONObject("address").getString("state");
                    mobile = user.getString("phone");
                    email = user.getString("email");


                    //callback response
                    volleyResponseListner.onResponse(id,firstname+" "+lastname, dob, gender, street, city, pin, state, mobile, email);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getHeath(ResponseListner ResponseListner){

        String url = "https://dummyjson.com/users";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HashMap<Integer, String>[] usr = new HashMap[5];
                try {
                    JSONArray users = response.getJSONArray("users");
                    for (int i = 0; i < 5 ; i++){
                        JSONObject person = users.getJSONObject(i);
                        usr[i] = new HashMap<Integer, String>();
                        usr[i].put(2, person.getString("height"));
                        usr[i].put(3, person.getString("weight"));
                        usr[i].put(4, person.getString("bloodGroup"));
                        String fn = person.getString("firstName");
                        String ln = person.getString("lastName");
                        usr[i].put(1, fn + " " + ln);
                        usr[i].put(5, person.getString("eyeColor"));

                    }
                    ResponseListner.onResponse(usr);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getBlood(Listner listner){
        String url = "https://dummyjson.com/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int[] numbers = {0,0,0,0,0,0,0,0};
                    JSONArray users = response.getJSONArray("users");


                    for(int i =0; i < users.length(); i++){
                        JSONObject detail = users.getJSONObject(i);
                        switch (detail.getString("bloodGroup")){
                            case "A+" :
                                numbers[0]++;
                                break;
                            case "B+" :
                                numbers[1]++;
                                break;
                            case "AB+" :
                                numbers[2]++;
                                break;
                            case "O+" :
                                numbers[3]++;
                                break;
                            case "A−" :
                                numbers[4]++;
                                break;
                            case "B−" :
                                numbers[5]++;
                                break;
                            case "AB−" :
                                numbers[6]++;
                                break;
                            case "O−" :
                                numbers[7]++;
                                break;
                        }
                    }
                    listner.onResponse(numbers);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getAge(VolleyListner volleyListner){
        String url = "https://dummyjson.com/users";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray users = response.getJSONArray("users");
                    float[] count = {0, 0, 0, 0, 0};

                    for(int i =0; i < users.length(); i++){
                        JSONObject detail = users.getJSONObject(i);
                        int age = detail.getInt("age");

                       if( age >= 20 && age <=30 ) count[0]++;
                       if( age >= 31 && age <=40 ) count[1]++;
                       if( age >= 41 && age <=50 ) count[2]++;
                       if( age >= 51 && age <=60 ) count[3]++;
                       if( age >= 61 && age <=70 ) count[4]++;

                    }
                    volleyListner.onResponse(count);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(context).addToRequestQueue(request);
    }

}

