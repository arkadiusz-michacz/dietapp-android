package com.example.dietapp1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ProduktAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.model.My_user;
import com.model.Produkt;
import com.model.Profil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static com.android.volley.VolleyLog.TAG;

public class ListaActivity extends AppCompatActivity {

    Context mContext;

    ArrayAdapter<Produkt> produktAdapter;


    static public Profil dzisiaj = new Profil();
    final ArrayList<Produkt> produktList = new ArrayList<Produkt>();

    TextView kal;
    TextView prot;
    TextView fat ;
    TextView carb;



    //funkcja odswieżająca dane po wyjsciu z historii
    @Override
    protected void onResume() {
        super.onResume();
        loadToday(kal,prot,fat,carb);
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Button addNewProduct= (Button) findViewById(R.id.button);
        Button editProfile= (Button) findViewById(R.id.buttonProfil);
        Button historia= (Button) findViewById(R.id.buttonHistoria);


        kal = (TextView) findViewById(R.id.kalorie);
        prot = (TextView) findViewById(R.id.bialko);
        fat = (TextView) findViewById(R.id.tluszcz);
        carb = (TextView) findViewById(R.id.wegle);
        ListView produktListView = this.findViewById(R.id.produktList);



        /*final ArrayAdapter<Produkt>*/ produktAdapter = new ProduktAdapter(mContext, produktList,kal,prot,fat,carb);

        produktListView.setAdapter(produktAdapter);




        //reloadToday(kal,prot,fat,carb);
        loadToday(kal,prot,fat,carb);




        loadProdukcts(produktAdapter);

        historia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getApplicationContext(), HistoriaActivity.class);
                    startActivity(i);

                }
            }

        );




        addNewProduct.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Nowy Produkt");


                Context context = v.getContext();

                ScrollView scrollView = new ScrollView(context);
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
                final EditText nazwa = new EditText(context);
                nazwa.setFilters(new InputFilter[]{new InputFilter.LengthFilter(256)});
                nazwa.setHint("Nazwa");
                layout.addView(nazwa); // Notice this is an add method

// Add another TextView here for the "Description" label
                final EditText kalorie = new EditText(context);
                kalorie.setInputType(TYPE_CLASS_NUMBER);
                kalorie.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                kalorie.setHint("Kalorie");
                layout.addView(kalorie); // Another add method

                final EditText bialko = new EditText(context);
                bialko.setInputType(TYPE_CLASS_NUMBER);
                bialko.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                bialko.setHint("Białko");
                layout.addView(bialko); // Another add method

                final EditText tluszcz = new EditText(context);
                tluszcz.setInputType(TYPE_CLASS_NUMBER);
                tluszcz.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                tluszcz.setHint("Tłuszcz");
                layout.addView(tluszcz); // Another add method

                final EditText wegle = new EditText(context);
                wegle.setInputType(TYPE_CLASS_NUMBER);
                wegle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                wegle.setHint("Węglowodany");
                layout.addView(wegle); // Another add method

                dialog.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final Produkt nowy = new Produkt();
                        if(!nazwa.getText().toString().isEmpty() && !kalorie.getText().toString().isEmpty() && !bialko.getText().toString().isEmpty() &&
                                !tluszcz.getText().toString().isEmpty() && !wegle.getText().toString().isEmpty()){

                            nowy.setNazwa(nazwa.getText().toString());
                            nowy.setOwner(MainActivity.loggedIn);
                            nowy.setKalorie(Double.parseDouble(kalorie.getText().toString()));
                            nowy.setBialko(Double.parseDouble(bialko.getText().toString()));
                            nowy.setTluszcz(Double.parseDouble(tluszcz.getText().toString()));
                            nowy.setWegle(Double.parseDouble(wegle.getText().toString()));
                            nowy.setStatus(1);


                            RequestQueue queue = Volley.newRequestQueue(v.getContext());

                            //String url = "http://192.168.0.31:8080/testWeb/webresources/test/historia/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd();
                            String url = v.getContext().getResources().getString(R.string.api_path)+"dodajprodukt/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd();

                            Gson gson = new Gson();
                            final String json = gson.toJson(nowy);
                            System.out.println(json);

                            try {
                                JSONObject reader = new JSONObject(json);

                                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                        Request.Method.POST, url, reader,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d(TAG, response.toString() + " i am queen");
                                                System.out.println("Sukcess");

                                                try {

                                                    if(response.getLong("id") != 0)
                                                    {
                                                        nowy.setId(response.getLong("id"));
                                                        System.out.println("AJdi: "+nowy.getId());
                                                        produktList.add(nowy);

                                                        Toast.makeText(getBaseContext(),
                                                                "Sukces!", Toast.LENGTH_SHORT).show();

                                                        produktAdapter.notifyDataSetChanged();
                                                    }


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    System.out.println("jsonex");
                                                    Toast.makeText(getBaseContext(),
                                                            "JSON error", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                        System.out.println("volleyerror");
                                        Toast.makeText(getBaseContext(),
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
                                System.out.println("jsonex2??");
                                Toast.makeText(getBaseContext(),
                                        "JSON error", Toast.LENGTH_LONG).show();
                            }




                        }

                    }
                })
                        .setNegativeButton("Zamknij",null)
                        .create();

                scrollView.addView(layout);
                dialog.setView(scrollView);
                dialog.show();


            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                dialog.setTitle("Profil użytkownika");


                Context context = v.getContext();

                ScrollView scrollView = new ScrollView(context);
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);


                final TextView kalorieTW = new TextView(context);
                kalorieTW.setText("Kalorie:");
                layout.addView(kalorieTW);


// Add another TextView here for the "Description" label
                final EditText kalorie = new EditText(context);
                kalorie.setInputType(TYPE_CLASS_NUMBER);
                kalorie.setHint("Kalorie");
                kalorie.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                kalorie.setText(String.valueOf( (int) MainActivity.loggedInProfil.getKalorie()) );
                layout.addView(kalorie); // Another add method

                final TextView bialkoTW = new TextView(context);
                bialkoTW.setText("Bialko:");
                layout.addView(bialkoTW);

                final EditText bialko = new EditText(context);
                bialko.setInputType(TYPE_CLASS_NUMBER);
                bialko.setHint("Białko");
                bialko.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                bialko.setText(String.valueOf( (int) MainActivity.loggedInProfil.getBialko()));
                layout.addView(bialko); // Another add method

                final TextView tluszczTW = new TextView(context);
                tluszczTW.setText("Tluszcz:");
                layout.addView(tluszczTW);

                final EditText tluszcz = new EditText(context);
                tluszcz.setInputType(TYPE_CLASS_NUMBER);
                tluszcz.setHint("Tłuszcz");
                tluszcz.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                tluszcz.setText(String.valueOf( (int) MainActivity.loggedInProfil.getTluszcz()));
                layout.addView(tluszcz); // Another add method


                final TextView wegleTW = new TextView(context);
                wegleTW.setText("Węglowodany:");
                layout.addView(wegleTW);


                final EditText wegle = new EditText(context);
                wegle.setInputType(TYPE_CLASS_NUMBER);
                wegle.setHint("Węglowodany");
                wegle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                wegle.setText(String.valueOf( (int) MainActivity.loggedInProfil.getWegle()));
                //dialog.setMessage("Węglowodany: ");
                layout.addView(wegle); // Another add method

                dialog.setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final Profil nowy = new Profil();
                        if(!kalorie.getText().toString().isEmpty() && !bialko.getText().toString().isEmpty() &&
                                !tluszcz.getText().toString().isEmpty() && !wegle.getText().toString().isEmpty()){


                            nowy.setOwner(MainActivity.loggedIn);
                            System.out.println(nowy.getOwner().getName());
                            nowy.setKalorie(Double.parseDouble(kalorie.getText().toString()));
                            nowy.setBialko(Double.parseDouble(bialko.getText().toString()));
                            nowy.setTluszcz(Double.parseDouble(tluszcz.getText().toString()));
                            nowy.setWegle(Double.parseDouble(wegle.getText().toString()));


                            RequestQueue queue = Volley.newRequestQueue(v.getContext());

                            //String url = "http://192.168.0.31:8080/testWeb/webresources/test/historia/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd();
                            String url = v.getContext().getResources().getString(R.string.api_path)+"edytujprofil/"/*+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd()+"/"*/+nowy.getOwner().getUserId();

                            Gson gson = new Gson();
                            final String json = gson.toJson(nowy);
                            System.out.println(json);

                            try {
                                JSONObject reader = new JSONObject(json);

                                JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                                        Request.Method.PUT, url, reader,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.d(TAG, response.toString() + " i am queen");

                                                        try {
                                                            MainActivity.loggedInProfil = nowy;
                                                            reloadToday(kal,prot,fat,carb);

                                                        }catch(Exception ex)
                                                        {
                                                            Log.d(TAG, "Error: " + ex.getMessage());
                                                            Toast.makeText(getBaseContext(),
                                                                    "Loading error", Toast.LENGTH_LONG).show();
                                                        }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                        Toast.makeText(getBaseContext(),
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
                                Toast.makeText(getBaseContext(),
                                        "JSON error", Toast.LENGTH_LONG).show();
                            }




                        }
                        else
                        {
                            Toast.makeText(getBaseContext(),
                                    "Uzupełnij pola", Toast.LENGTH_LONG).show();
                        }

                    }
                })
                        .setNegativeButton("Zamknij",null)
                        .create();

                //dialog.setView(layout);
                scrollView.addView(layout);
                dialog.setView(scrollView);
                dialog.show();


            }
        });





    }

    //ładowanie produktów do adaptera
    public void loadProdukcts(final ArrayAdapter<Produkt> produktAdapter)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.api_path)+"produkty";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray reader = new JSONArray(response);
                            for (int i = 0; i < reader.length(); i++)
                            {
                                JSONObject produkt = reader.getJSONObject(i);
                                JSONObject owner = produkt.getJSONObject("owner");
                                My_user own = new My_user();
                                own.setStatus(owner.getInt("status"));
                                own.setUserId(owner.getLong("userId"));
                                own.setPasswd(owner.getString("passwd"));
                                own.setName(owner.getString("name"));

                                Produkt prd = new Produkt();
                                prd.setOwner(own);
                                prd.setNazwa(produkt.getString("nazwa"));
                                prd.setBialko(produkt.getDouble("bialko"));
                                prd.setKalorie(produkt.getDouble("kalorie"));
                                prd.setTluszcz(produkt.getDouble("tluszcz"));
                                prd.setWegle(produkt.getDouble("wegle"));
                                prd.setId(produkt.getLong("id"));

                                System.out.println(prd.toString());

                                produktList.add(prd);



                            }

                            produktAdapter.notifyDataSetChanged();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(),
                                    "JSON error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(getBaseContext(),
                        "HTTP error", Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }

    //odświeżenie dzisiejszych statystyk UI
    static public void reloadToday(final TextView kal,final TextView prot,final TextView fat,final TextView carb)
    {
        kal.setText("Kalorie: "+String.valueOf((int) dzisiaj.getKalorie()+"/"+(int) MainActivity.loggedInProfil.getKalorie()));
        prot.setText("Białko: "+String.valueOf((int) dzisiaj.getBialko()+"/"+(int) MainActivity.loggedInProfil.getBialko()));
        fat.setText("Tłuszcz: "+String.valueOf((int) dzisiaj.getTluszcz()+"/"+(int) MainActivity.loggedInProfil.getTluszcz()));
        carb.setText("Węglowodany: "+String.valueOf((int) dzisiaj.getWegle()+"/"+(int) MainActivity.loggedInProfil.getWegle()));

        if(dzisiaj.getKalorie()>MainActivity.loggedInProfil.getKalorie()) {
            kal.setBackgroundColor(kal.getContext().getResources().getColor(R.color.red));
        }else{
            kal.setBackgroundColor(kal.getContext().getResources().getColor(R.color.grey));
        }
        if(dzisiaj.getBialko()>MainActivity.loggedInProfil.getBialko()) {
            prot.setBackgroundColor(prot.getContext().getResources().getColor(R.color.red));
        }else{
            prot.setBackgroundColor(prot.getContext().getResources().getColor(R.color.grey));
        }

        if(dzisiaj.getTluszcz()>MainActivity.loggedInProfil.getTluszcz()) {
            fat.setBackgroundColor(fat.getContext().getResources().getColor(R.color.red));
        }else{
            fat.setBackgroundColor(fat.getContext().getResources().getColor(R.color.grey));
        }

        if(dzisiaj.getWegle()>MainActivity.loggedInProfil.getWegle()) {
            carb.setBackgroundColor(carb.getContext().getResources().getColor(R.color.red));
        }else{
            carb.setBackgroundColor(carb.getContext().getResources().getColor(R.color.grey));
        }



    }


    //ładowanie dzisiejszych statystyk
    public void loadToday(final TextView kal, final TextView prot, final TextView fat, final TextView carb)
    {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.api_path)+"historia/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd()+"/"+MainActivity.loggedIn.getUserId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject reader = new JSONObject(response);

                            dzisiaj.setBialko(reader.getDouble("bialko"));
                            dzisiaj.setKalorie(reader.getDouble("kalorie"));
                            dzisiaj.setTluszcz(reader.getDouble("tluszcz"));
                            dzisiaj.setWegle(reader.getDouble("wegle"));

                            reloadToday(kal,prot,fat,carb);


                            /*kal.setText("K: "+String.valueOf(dzisiaj.getKalorie()+"/"+MainActivity.loggedInProfil.getKalorie()));
                            prot.setText("B: "+String.valueOf(dzisiaj.getBialko()+"/"+MainActivity.loggedInProfil.getBialko()));
                            fat.setText("T: "+String.valueOf(dzisiaj.getTluszcz()+"/"+MainActivity.loggedInProfil.getTluszcz()));
                            carb.setText("W: "+String.valueOf(dzisiaj.getWegle()+"/"+MainActivity.loggedInProfil.getWegle()));*/


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getBaseContext(),
                                    "JSON error", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(getBaseContext(),
                        "HTTP error", Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


    }


    //część wyszukiwania, reszta w adapterze
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                produktAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
