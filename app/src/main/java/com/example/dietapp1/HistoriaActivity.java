package com.example.dietapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.HistoriaAdapter;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.model.Historia;
import com.model.My_user;
import com.model.Produkt;
import com.model.Profil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class HistoriaActivity extends AppCompatActivity {


    Context mContext;

    ArrayAdapter<Historia> historiaAdapter;

    final ArrayList<Historia> historiaList = new ArrayList<>();

    TextView kal;
    TextView prot;
    TextView fat ;
    TextView carb;

    Profil dzisiaj;

    DatePickerDialog picker;
    int day;
    int month;
    int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historia);

        Button datahistoria = (Button) findViewById(R.id.historiadata);
        Button trend = (Button) findViewById(R.id.toTrends);
        final TextView datalabel = (TextView) findViewById(R.id.datalabel);
        ListView historiaListView = this.findViewById(R.id.listahistoria);

        kal = (TextView) findViewById(R.id.kalorie);
        prot = (TextView) findViewById(R.id.bialko);
        fat = (TextView) findViewById(R.id.tluszcz);
        carb = (TextView) findViewById(R.id.wegle);


        historiaAdapter = new HistoriaAdapter(mContext, historiaList, kal, prot,fat,carb);
        historiaListView.setAdapter(historiaAdapter);



        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH)+1;
        year = cldr.get(Calendar.YEAR);

        dzisiaj = new Profil();

        datalabel.setText(day + "/" + month + "/" + year);

        loadHistoria(historiaAdapter,day,month,year);

        trend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TrendActivity.class);
                startActivity(i);
            }
        });



        datahistoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Test");
                picker = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yearOfYear, int monthOfYear, int dayOfMonth) {
                                datalabel.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + yearOfYear);
                                day = dayOfMonth;
                                month = monthOfYear+1;
                                year = yearOfYear;
                                loadHistoria(historiaAdapter,day,month,year);


                            }
                        }, year, month-1, day);

                picker.show();

            }
        });


    }


    static public void reloadDaySum(ArrayList<Historia> lista, final TextView kal,final TextView prot,final TextView fat,final TextView carb)
    {

        double ckal = 0;
        double protein = 0;
        double fatThisDay = 0;
        double carbs = 0;

        for(Historia element: lista)
        {


            ckal += element.getProdukt().getKalorie();
            protein += element.getProdukt().getBialko();
            fatThisDay += element.getProdukt().getTluszcz();
            carbs += element.getProdukt().getWegle();
            System.out.println("testtestloop "+ckal+protein+fatThisDay+carbs);


        }



        kal.setText("Kalorie: "+String.valueOf((int) ckal+"/"+(int) MainActivity.loggedInProfil.getKalorie()));
        prot.setText("Białko: "+String.valueOf((int) protein+"/"+(int) MainActivity.loggedInProfil.getBialko()));
        fat.setText("Tłuszcz: "+String.valueOf((int) fatThisDay+"/"+(int) MainActivity.loggedInProfil.getTluszcz()));
        carb.setText("Węglowodany: "+String.valueOf((int) carbs+"/"+(int) MainActivity.loggedInProfil.getWegle()));

        if(ckal>MainActivity.loggedInProfil.getKalorie()) {
            kal.setBackgroundColor(kal.getContext().getResources().getColor(R.color.red));
        }else{
            kal.setBackgroundColor(kal.getContext().getResources().getColor(R.color.grey));
        }
        if(protein>MainActivity.loggedInProfil.getBialko()) {
            prot.setBackgroundColor(prot.getContext().getResources().getColor(R.color.red));
        }else{
            prot.setBackgroundColor(prot.getContext().getResources().getColor(R.color.grey));
        }

        if(fatThisDay>MainActivity.loggedInProfil.getTluszcz()) {
            fat.setBackgroundColor(fat.getContext().getResources().getColor(R.color.red));
        }else{
            fat.setBackgroundColor(fat.getContext().getResources().getColor(R.color.grey));
        }

        if(carbs>MainActivity.loggedInProfil.getWegle()) {
            carb.setBackgroundColor(carb.getContext().getResources().getColor(R.color.red));
        }else{
            carb.setBackgroundColor(carb.getContext().getResources().getColor(R.color.grey));
        }

    }


    public void loadHistoria(final ArrayAdapter<Historia> historiaAdapter, int dzien, int mce, int rok)
    {

        historiaList.clear();
        historiaAdapter.notifyDataSetChanged();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.api_path)+"historialist/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd()+"/"+MainActivity.loggedIn.getUserId()+"/"+dzien+"/"+mce+"/"+rok;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray reader = new JSONArray(response);
                            for (int i = 0; i < reader.length(); i++)
                            {
                                JSONObject historia = reader.getJSONObject(i);
                                JSONObject user = historia.getJSONObject("user");
                                My_user own = new My_user();
                                own.setStatus(user.getInt("status"));
                                own.setUserId(user.getLong("userId"));
                                own.setPasswd(user.getString("passwd"));
                                own.setName(user.getString("name"));

                                JSONObject produkt = historia.getJSONObject("produkt");
                                Produkt prd = new Produkt();
                                //prd.setOwner(own);
                                prd.setNazwa(produkt.getString("nazwa"));
                                prd.setBialko(produkt.getDouble("bialko"));
                                prd.setKalorie(produkt.getDouble("kalorie"));
                                prd.setTluszcz(produkt.getDouble("tluszcz"));
                                prd.setWegle(produkt.getDouble("wegle"));
                                prd.setId(produkt.getLong("id"));


                                Historia hist = new Historia();
                                hist.setUser(own);
                                hist.setProdukt(prd);
                                hist.setId(historia.getLong("id"));



                                System.out.println(hist.toString());

                                historiaList.add(hist);



                            }

                            historiaAdapter.notifyDataSetChanged();

                            reloadDaySum(historiaList,kal, prot, fat, carb);


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
}
