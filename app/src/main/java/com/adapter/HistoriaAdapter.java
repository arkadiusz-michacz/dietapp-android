package com.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dietapp1.HistoriaActivity;
import com.example.dietapp1.ListaActivity;
import com.example.dietapp1.MainActivity;
import com.example.dietapp1.R;
import com.google.gson.Gson;
import com.model.Historia;
import com.model.My_user;
import com.model.Produkt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class HistoriaAdapter extends ArrayAdapter<Historia> {

    private ArrayList<Historia> historiaList;

    private TextView kal;
    private TextView prot;
    private TextView fat;
    private TextView carb;


    public HistoriaAdapter(@NonNull Context context, ArrayList<Historia> historiaList,  TextView kal,TextView prot, TextView fat, TextView carb) {
        super(context, R.layout.historia_row, historiaList);
        this.historiaList = historiaList;

        this.kal = kal;
        this.prot = prot;
        this.fat = fat;
        this.carb = carb;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View row = layoutInflater.inflate(R.layout.historia_row, parent, false);

        final Historia task = getItem(position);

        final TextView taskName = (TextView) row.findViewById(R.id.task_name);
        final TextView taskCkal = (TextView) row.findViewById(R.id.task_ckal);
        final TextView taskProt = (TextView) row.findViewById(R.id.task_prot);
        final TextView taskFat = (TextView) row.findViewById(R.id.task_fat);
        final TextView taskCarb = (TextView) row.findViewById(R.id.task_carb);

        Button deleteTask = (Button) row.findViewById(R.id.delBtn);



        deleteTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if(true)
                {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("Jesteś pewien?");






                    dialog.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {



                            RequestQueue queue = Volley.newRequestQueue(v.getContext());

                            //String url = "http://192.168.0.31:8080/testWeb/webresources/test/historia/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd();
                            String url = v.getContext().getResources().getString(R.string.api_path) + "deletehistory/" + MainActivity.loggedIn.getName() + MainActivity.loggedIn.getPasswd()+"/"+task.getId();

                            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the first 500 characters of the response string.
                                            try {

                                                if(response.equals("YAY"))
                                                {

                                                    historiaList.remove(position);
                                                    HistoriaActivity.reloadDaySum(historiaList, kal, prot, fat, carb);
                                                    notifyDataSetChanged();
                                                }
                                                else
                                                {
                                                    Toast.makeText(getContext(),
                                                            "Nie udało się usunąć wpisu", Toast.LENGTH_LONG).show();
                                                }





                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Toast.makeText(getContext(),
                                                        "Error", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    error.printStackTrace();
                                    Toast.makeText(getContext(),
                                            "HYYP error", Toast.LENGTH_LONG).show();
                                }
                            });

// Add the request to the RequestQueue.
                            queue.add(stringRequest);




                        }
                    })
                            .setNegativeButton("Anuluj", null)
                            .create();


                    dialog.show();


                }
            }
        });






        taskName.setText(String.format("%s", task.getProdukt().getNazwa()));
        taskCkal.setText(String.format("%s", (int) task.getProdukt().getKalorie()));
        taskProt.setText(String.format("%s", (int) task.getProdukt().getBialko()));
        taskFat.setText(String.format("%s", (int) task.getProdukt().getTluszcz()));
        taskCarb.setText(String.format("%s", (int) task.getProdukt().getWegle()));
        return row;

    }



}
