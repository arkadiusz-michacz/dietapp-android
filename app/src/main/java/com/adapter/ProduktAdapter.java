package com.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.android.volley.toolbox.Volley;
import com.example.dietapp1.ListaActivity;
import com.example.dietapp1.MainActivity;
import com.example.dietapp1.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.model.Historia;
import com.model.Produkt;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static com.android.volley.VolleyLog.TAG;

public class ProduktAdapter extends ArrayAdapter<Produkt> implements Filterable {

    private Context mContext;

    private ArrayList<Produkt> produktList;
    private ArrayList<Produkt> wszystkieProdukty;


    private TextView kal;
    private TextView prot;
    private TextView fat;
    private TextView carb;

    //private Toast toastMssg;

    public ProduktAdapter(Context context, ArrayList<Produkt> produktList, TextView kal,TextView prot, TextView fat, TextView carb) {
        super(context, R.layout.pridukt_row, produktList);
        this.produktList = produktList;
        this.mContext = context;

        //this.toastMssg = null;

        this.wszystkieProdukty = new ArrayList<>(this.produktList);


        this.kal = kal;
        this.prot = prot;
        this.fat = fat;
        this.carb = carb;


    }




    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        final View row = layoutInflater.inflate(R.layout.pridukt_row, parent, false);

        final Produkt task = getItem(position);



        final TextView taskName = (TextView) row.findViewById(R.id.task_name);
        final TextView taskCkal = (TextView) row.findViewById(R.id.task_ckal);
        final TextView taskProt = (TextView) row.findViewById(R.id.task_prot);
        final TextView taskFat = (TextView) row.findViewById(R.id.task_fat);
        final TextView taskCarb = (TextView) row.findViewById(R.id.task_carb);
        Button taskDone = (Button) row.findViewById(R.id.task_status);
        Button editTask = (Button) row.findViewById(R.id.editBtn);
        Button deleteTask = (Button) row.findViewById(R.id.deleteBtn);







        editTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if(task.getOwner().getUserId() == MainActivity.loggedIn.getUserId())
                {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("Edycja");


                    Context context = v.getContext();
                    ScrollView scrollView = new ScrollView(context);
                    LinearLayout layout = new LinearLayout(context);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    // Add a TextView here for the "Title" label, as noted in the comments
                    final EditText nazwa = new EditText(context);
                    nazwa.setFilters(new InputFilter[]{new InputFilter.LengthFilter(256)});
                    nazwa.setHint("Nazwa");
                    nazwa.setText(task.getNazwa());
                    layout.addView(nazwa); // Notice this is an add method

                    final TextView kalorieTW = new TextView(context);
                    kalorieTW.setText("Kalorie:");
                    layout.addView(kalorieTW);

                    // Add another TextView here for the "Description" label
                    final EditText kalorie = new EditText(context);
                    kalorie.setInputType(TYPE_CLASS_NUMBER);
                    kalorie.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    kalorie.setHint("Kalorie");
                    kalorie.setText(String.valueOf( (int) task.getKalorie()));
                    layout.addView(kalorie); // Another add method

                    final TextView bialkoTW = new TextView(context);
                    bialkoTW.setText("Bialko:");
                    layout.addView(bialkoTW);

                    final EditText bialko = new EditText(context);
                    bialko.setInputType(TYPE_CLASS_NUMBER);
                    bialko.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    bialko.setHint("Białko");
                    bialko.setText(String.valueOf( (int) task.getBialko()));
                    layout.addView(bialko); // Another add method

                    final TextView tluszczTW = new TextView(context);
                    tluszczTW.setText("Tłuszcz:");
                    layout.addView(tluszczTW);

                    final EditText tluszcz = new EditText(context);
                    tluszcz.setInputType(TYPE_CLASS_NUMBER);
                    tluszcz.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    tluszcz.setHint("Tłuszcz");
                    tluszcz.setText(String.valueOf( (int) task.getTluszcz()));
                    layout.addView(tluszcz); // Another add method

                    final TextView wegleTW = new TextView(context);
                    wegleTW.setText("Węglowodany:");
                    layout.addView(wegleTW);

                    final EditText wegle = new EditText(context);
                    wegle.setInputType(TYPE_CLASS_NUMBER);
                    wegle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                    wegle.setHint("Węglowodany");
                    wegle.setText(String.valueOf( (int) task.getWegle()));
                    layout.addView(wegle); // Another add method

                    dialog.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            boolean nazwaCheck = false;
                            for(Produkt produkt:produktList)
                            {
                                if(nazwa.getText().toString().equals(produkt.getNazwa()) && !nazwa.getText().toString().equals(task.getNazwa())){
                                    nazwaCheck = true;
                                    break;
                                }
                            }

                            //final Produkt nowy = new Produkt();
                            if (!nazwaCheck && !nazwa.getText().toString().isEmpty() && !kalorie.getText().toString().isEmpty() && !bialko.getText().toString().isEmpty() &&
                                    !tluszcz.getText().toString().isEmpty() && !wegle.getText().toString().isEmpty()) {

                                task.setNazwa(nazwa.getText().toString());
                                task.setOwner(MainActivity.loggedIn);
                                task.setKalorie(Double.parseDouble(kalorie.getText().toString()));
                                task.setBialko(Double.parseDouble(bialko.getText().toString()));
                                task.setTluszcz(Double.parseDouble(tluszcz.getText().toString()));
                                task.setWegle(Double.parseDouble(wegle.getText().toString()));


                                RequestQueue queue = Volley.newRequestQueue(v.getContext());

                                //String url = "http://192.168.0.31:8080/testWeb/webresources/test/historia/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd();
                                String url = v.getContext().getResources().getString(R.string.api_path) + "editprod/" + MainActivity.loggedIn.getName() + MainActivity.loggedIn.getPasswd();

                                Gson gson = new Gson();
                                final String json = gson.toJson(task);
                                System.out.println(json);

                                try {
                                    JSONObject reader = new JSONObject(json);

                                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                            Request.Method.PUT, url, reader,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                    Log.d(TAG, response.toString() + " i am queen");
                                                    if(mContext instanceof ListaActivity)
                                                    {
                                                        ((ListaActivity)mContext).loadToday(kal, prot, fat, carb);
                                                    }


                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                                            Toast.makeText(getContext(),
                                                    "HTTP error", Toast.LENGTH_LONG).show();
                                        }
                                    }) {

                                        /**
                                         * Passing some request headers
                                         */
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            HashMap<String, String> headers = new HashMap<String, String>();
                                            headers.put("Content-Type", "application/json; charset=utf-8");
                                            return headers;
                                        }

                                    };

                                    queue.add(jsonObjReq);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(),
                                            "JSON error", Toast.LENGTH_LONG).show();
                                }


                            }

                        }
                    })
                            .setNegativeButton("Zamknij", null)
                            .create();

                    scrollView.addView(layout);
                    dialog.setView(scrollView);
                    dialog.show();


                }
                else{
                    Toast.makeText(getContext(),
                            "Nie jesteś właścicielem obiektu", Toast.LENGTH_LONG).show();
                }
            }
        });


        deleteTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                if(task.getOwner().getUserId() == MainActivity.loggedIn.getUserId())
                {

                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("Jesteś pewien?");






                    dialog.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            task.setStatus(0);

                            RequestQueue queue = Volley.newRequestQueue(v.getContext());

                                //String url = "http://192.168.0.31:8080/testWeb/webresources/test/historia/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd();
                                String url = v.getContext().getResources().getString(R.string.api_path) + "deleteprod/" + MainActivity.loggedIn.getName() + MainActivity.loggedIn.getPasswd();

                                Gson gson = new Gson();
                                final String json = gson.toJson(task);
                                System.out.println(json);

                                try {
                                    JSONObject reader = new JSONObject(json);

                                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                            Request.Method.PUT, url, reader,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Log.d(TAG, response.toString() + " i am queen");
                                                    wszystkieProdukty.addAll(produktList);
                                                    produktList.remove(position);
                                                    wszystkieProdukty.remove(position);
                                                    notifyDataSetChanged();


                                                }
                                            }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                                            Toast.makeText(getContext(),
                                                    "HTTP error", Toast.LENGTH_LONG).show();
                                        }
                                    }) {

                                        /**
                                         * Passing some request headers
                                         */
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            HashMap<String, String> headers = new HashMap<String, String>();
                                            headers.put("Content-Type", "application/json; charset=utf-8");
                                            return headers;
                                        }

                                    };

                                    queue.add(jsonObjReq);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getContext(),
                                            "JSON error", Toast.LENGTH_LONG).show();
                                }




                        }
                    })
                            .setNegativeButton("Anuluj", null)
                            .create();


                    dialog.show();


                }
                else{
                    Toast.makeText(getContext(),
                            "Nie jesteś właścicielem obiektu", Toast.LENGTH_LONG).show();
                }
            }
        });






        taskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(getContext());

                //String url = "http://192.168.0.31:8080/testWeb/webresources/test/historia/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd();
                String url = v.getContext().getResources().getString(R.string.api_path) + "historia/" + MainActivity.loggedIn.getName() + MainActivity.loggedIn.getPasswd();


                Historia unit = new Historia();
                unit.setProdukt(produktList.get(position));
                unit.setUser(MainActivity.loggedIn);

                if(true){

                    Gson gson = new Gson();
                    final String json = gson.toJson(unit);
                    System.out.println(json);

                    try {
                        JSONObject reader = new JSONObject(json);

                        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                Request.Method.POST, url, reader,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d(TAG, response.toString() + " i am queen");

                                        //Toast toastMssg = new Toast(getContext());
                                        /*if(toastMssg != null)
                                        {   System.out.println("Cancel Toast");
                                            toastMssg.cancel();}*/

                                            Snackbar snackbar = Snackbar
                                                    .make(v, "Om Nom Nom", Snackbar.LENGTH_SHORT);
                                            if(!snackbar.isShown())
                                                snackbar.show();



                                        /*toastMssg.makeText(getContext(),
                                                "Om Nom Nom", Toast.LENGTH_LONG).show();*/



                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d(TAG, "Error: " + error.getMessage());
                                Toast.makeText(getContext(),
                                        "Request error", Toast.LENGTH_LONG).show();


                            }
                        }) {

                            /**
                             * Passing some request headers
                             */
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                return headers;
                            }

                        };

                        queue.add(jsonObjReq);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(),
                                "JSON error", Toast.LENGTH_LONG).show();
                    }


                    ListaActivity.dzisiaj.setKalorie(ListaActivity.dzisiaj.getKalorie() + produktList.get(position).getKalorie());
                    ListaActivity.dzisiaj.setBialko(ListaActivity.dzisiaj.getBialko() + produktList.get(position).getBialko());
                    ListaActivity.dzisiaj.setTluszcz(ListaActivity.dzisiaj.getTluszcz() + produktList.get(position).getTluszcz());
                    ListaActivity.dzisiaj.setWegle(ListaActivity.dzisiaj.getWegle() + produktList.get(position).getWegle());

                    ListaActivity.reloadToday(kal, prot, fat, carb);
                }

            }
        });



        taskName.setText(String.format("%s", task.getNazwa()));
        taskCkal.setText(String.format("%s", (int) task.getKalorie()));
        taskProt.setText(String.format("%s", (int) task.getBialko()));
        taskFat.setText(String.format("%s", (int) task.getTluszcz()));
        taskCarb.setText(String.format("%s", (int) task.getWegle()));
        return row;

    }



    //reszta wyszukiwania
    @Override
    public Filter getFilter(){
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList <Produkt> filteredList = new ArrayList<>();
            if(produktList.size()>wszystkieProdukty.size()){
                wszystkieProdukty.addAll(produktList);
            }


            if(constraint.toString().isEmpty()){
                filteredList.addAll(wszystkieProdukty);
            }else{
                for(Produkt produkt : wszystkieProdukty)
                {
                    if(produkt.getNazwa().toLowerCase().contains(constraint.toString().toLowerCase()))
                    {
                        filteredList.add(produkt);
                    }
                }

            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            produktList.clear();
            produktList.addAll((Collection<? extends Produkt>) results.values);
            notifyDataSetChanged();


        }
    };

}
