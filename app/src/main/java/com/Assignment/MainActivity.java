package com.Assignment;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchData(this);
    }


    public void fetchData(Context context) {
        String url = "https://fetch-hiring.s3.amazonaws.com/hiring.json";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Type listType = new TypeToken<ArrayList<Item>>() {}.getType();
                        List<Item> items = new Gson().fromJson(response.toString(), listType);
                        displayItems(items);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }


    public void displayItems(List<Item> items) {
        List<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getName() != null && !item.getName().trim().isEmpty()) {
                filteredItems.add(item);
            }
        }

        Map<Integer, List<Item>> groupedItems = new TreeMap<>();
        for (Item item : filteredItems) {
            if (!groupedItems.containsKey(item.getListId())) {
                groupedItems.put(item.getListId(), new ArrayList<>());
            }
            groupedItems.get(item.getListId()).add(item);
        }

        for (List<Item> group : groupedItems.values()) {
            group.sort((item1, item2) -> item1.getName().compareToIgnoreCase(item2.getName()));
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ItemAdapter adapter = new ItemAdapter(groupedItems);
        recyclerView.setAdapter(adapter);
    }


}