package com.example.dietapp1;

import android.os.Build;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dietapp1.ui.main.SectionsPagerAdapter;
import com.model.Historia;
import com.model.My_user;
import com.model.Produkt;
import com.trendfragment.fragmentCkal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class TrendActivity extends AppCompatActivity {

    public static ArrayList<Integer> arrayCkal;
    public static ArrayList<Integer> arrayProt;
    public static ArrayList<Integer> arrayFat;
    public static ArrayList<Integer> arrayCarb;
    public static ArrayList<String> arrayDaty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trend);

        arrayCkal = new ArrayList<>();
        arrayCarb = new ArrayList<>();
        arrayDaty = new ArrayList<>();
        arrayFat = new ArrayList<>();
        arrayProt = new ArrayList<>();






        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        //viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        trendsWeek(sectionsPagerAdapter,viewPager);
        System.out.println("po wykonaniu trendsweek()");
        if(arrayCkal.isEmpty())
            System.out.println("true");
        else
            System.out.println("false");



    }

    public void trendsWeek(SectionsPagerAdapter sectionsPagerAdapter, ViewPager viewPager)
    {


        ArrayList<Historia> trendHist = new ArrayList<>();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getString(R.string.api_path)+"trendy/"+MainActivity.loggedIn.getName()+MainActivity.loggedIn.getPasswd()+"/"+MainActivity.loggedIn.getUserId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        try {
                            JSONArray reader = new JSONArray(response);
                            for (int i = 0; i < reader.length(); i++)
                            {
                                JSONObject historia = reader.getJSONObject(i);
                                String data = historia.getString("kiedy");
                                //System.out.println(data);
                                String[] slitted = data.split("[T-]" );

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
                                //prd.setNazwa(Integer.parseInt(slitted[0])+"/"+Integer.parseInt(slitted[1])+"/"+Integer.parseInt(slitted[2]));
                                if(slitted[1].length()==1)
                                {slitted[1] = "0"+slitted[1];}
                                //System.out.println(slitted[1]);
                                prd.setNazwa(slitted[0]+"-"+slitted[1]+"-"+slitted[2]);
                                //System.out.println(prd.getNazwa());
                                hist.setProdukt(prd);
                                hist.setKiedy(new Date(Integer.parseInt(slitted[0]),Integer.parseInt(slitted[1]),Integer.parseInt(slitted[2])));

                                trendHist.add(hist);
                                //System.out.println(hist.getKiedy().getDay());




                            }

                            if(!trendHist.isEmpty())
                            { computeTrendhist(trendHist);    }


                            System.out.println("Wewnatrz trendsweek() po computeTrendhist()");
                            if(arrayCkal.isEmpty())
                                System.out.println("true");
                            else
                                System.out.println("false");

                            //very important!!!
                            viewPager.setAdapter(sectionsPagerAdapter);



                            /*for(String s: arrayDaty)
                                System.out.println(s);*/


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


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void computeTrendhist(ArrayList<Historia> x)
    {
        arrayCkal.clear();
        arrayCarb.clear();
        arrayDaty.clear();
        arrayFat.clear();
        arrayProt.clear();



        LocalDate today = LocalDate.now();
        //System.out.println(today.toString());




        for(int i = 6;!today.minusDays(i).equals(today.plusDays(1)); i --)
        {
            //System.out.println(today.minusDays(i).toString());
            arrayCkal.add(0);
            arrayProt.add(0);
            arrayFat.add(0);
            arrayCarb.add(0);
            arrayDaty.add(today.minusDays(i).toString());
        }

        //System.out.println(arrayDaty.size());

       /* for(String s: arrayDaty)
            System.out.println(s);*/

        for(int i = 0; i<arrayDaty.size(); i++)
        {
            for(Historia h:x)
            {
                //System.out.println(h.getProdukt().getNazwa()+"==>"+arrayDaty.get(i));
                //System.out.println(h.getProdukt().getNazwa().equals(arrayDaty.get(i)));
                if(h.getProdukt().getNazwa().equals(arrayDaty.get(i)))
                {
                    //System.out.println("Tu jestem");
                    arrayCkal.set(i,arrayCkal.get(i)+((int) h.getProdukt().getKalorie()));
                    arrayProt.set(i,arrayProt.get(i)+((int) h.getProdukt().getBialko()));
                    arrayFat.set(i,arrayFat.get(i)+((int) h.getProdukt().getTluszcz()));
                    arrayCarb.set(i,arrayCarb.get(i)+((int) h.getProdukt().getWegle()));

                }

            }

            //System.out.println(arrayDaty.get(i)+"==> "+arrayCkal.get(i)+"==> "+arrayProt.get(i)+"==> "+arrayFat.get(i)+"==> "+arrayCarb.get(i));

            /*Bundle bundle = new Bundle();
            bundle.putStringArrayList("arrayDaty", arrayDaty);
            bundle.putIntegerArrayList("arrayCkal", arrayCkal);
            bundle.putIntegerArrayList("arrayProt", arrayProt);
            bundle.putIntegerArrayList("arrayFat", arrayFat);
            bundle.putIntegerArrayList("arrayCarb", arrayCarb);

            fragmentCkal fragmentCkal = new fragmentCkal();
            fragmentCkal.setArguments(bundle);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.view_pager,fragmentCkal);
            fragmentTransaction.commit();*/

        }





    }


}