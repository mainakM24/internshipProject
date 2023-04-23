package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;


public class MyDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_details, container, false);
        Toast.makeText(getActivity(), "Fetching data", Toast.LENGTH_SHORT).show();
//////////////////////////////////////////////////////////////////////////////////////////////////

        DataService dataService = new DataService(getActivity());
        dataService.getId(new DataService.VolleyResponseListner() {
            @Override
            public void onResponse(String id, String name, String dob, String gender, String street,
                                   String city, String pin, String state, String mobile, String email) {



                DataTable dataTable = view.findViewById(R.id.data_table);
                DataTableHeader header = new DataTableHeader.Builder()
                        .item("My Information", 1)
                        .item("", 1)
                        .build();

                ArrayList<DataTableRow> rows = new ArrayList<>();

                createRow("Patient id",id, rows);
                createRow("Name", name, rows);
                createRow("Date of birth", dob, rows);
                createRow("Gender", gender, rows);
                createRow("Street name", street, rows);
                createRow("City", city, rows);
                createRow("Pin Code", pin, rows);
                createRow("State", state, rows);
                createRow("Mobile", mobile, rows);
                createRow("Email", email, rows);

                dataTable.setHeader(header);
                dataTable.setRows(rows);
                dataTable.inflate(getActivity());
            }

            @Override
            public void onError(String message) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    public void createRow(String field, String data, ArrayList<DataTableRow> rows){

        DataTableRow row = new DataTableRow.Builder()
                .value(field)
                .value(data)
                .build();
        rows.add(row);
    }


}