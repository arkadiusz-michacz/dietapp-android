package com.example.dietapp1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.encrypt.md5;
import com.google.gson.Gson;
import com.model.My_user;
import com.model.Produkt;
import com.model.Profil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
import static com.android.volley.VolleyLog.TAG;


public class MainActivity extends AppCompatActivity {

    String loginStr;
    static public My_user loggedIn = new My_user();
    static public Profil loggedInProfil = new Profil();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText login = (EditText) findViewById(R.id.login);
        login.setHint("login");

        final EditText passwd = (EditText) findViewById(R.id.passwd);
        passwd.setHint("hasło");

        //ładowanie zapisanego konta
        if(!searchPref("log").equals("error"))
        {
            login.setText(searchPref("log"));
            passwd.setText(searchPref("pass"));
        }
        Button loginBtn = (Button) findViewById(R.id.loginBtn);
        Button newAcc = (Button) findViewById(R.id.newAcc);

        /*if(loggedIn.getName() != null && loggedIn.getPasswd() != null)//(!loggedIn.getName().isEmpty() && !loggedIn.getPasswd().isEmpty())
        {
            login.setText(loggedIn.getName());
            passwd.setText(loggedIn.getPasswd());
        }*/

        final RequestQueue queue = Volley.newRequestQueue(this);



        //funkcje logowania
        View.OnClickListener loginListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                loginStr = md5.getMd5(login.getText().toString())+md5.getMd5(passwd.getText().toString());

                String url = getString(R.string.api_path);
                url+=("login/"+loginStr);

                System.out.println(url);

                StringRequest userRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.

                                try {
                                JSONObject reader = new JSONObject(response);

                                    if(!reader.getString("name").equals("Nie ma takiego usera"))
                                    {
                                        loggedIn.setName(reader.getString("name"));
                                        loggedIn.setPasswd(reader.getString("passwd"));
                                        loggedIn.setUserId(reader.getLong("userId"));
                                        loggedIn.setStatus(reader.getInt("status"));

                                        if(loggedIn.getUserId() != 0) {

                                            String urlProfil = getString(R.string.api_path) + "profil/" + loginStr + "/" + loggedIn.getUserId();
                                            System.out.println(urlProfil);
                                            StringRequest profilRequest = new StringRequest(Request.Method.GET, urlProfil,
                                                    new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            // Display the first 500 characters of the response string.

                                                            try {
                                                                JSONObject reader = new JSONObject(response);


                                                                loggedInProfil.setOwner(loggedIn);
                                                                loggedInProfil.setBialko(reader.getDouble("bialko"));
                                                                loggedInProfil.setKalorie(reader.getDouble("kalorie"));
                                                                loggedInProfil.setTluszcz(reader.getDouble("tluszcz"));
                                                                loggedInProfil.setWegle(reader.getDouble("wegle"));

                                                                savePref(login.getText().toString(),passwd.getText().toString());



                                                                System.out.println(loggedIn.toString());
                                                                System.out.println(loggedInProfil.toString());
                                                                Intent i = new Intent(getApplicationContext(), ListaActivity.class);
                                                                startActivity(i);




                                                            } catch (JSONException e) {
                                                                login.setText("");
                                                                passwd.setText("");
                                                                login.requestFocus();
                                                                e.printStackTrace();
                                                                Toast.makeText(getBaseContext(),
                                                                        "JSON error", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    login.setText("");
                                                    passwd.setText("");
                                                    login.requestFocus();
                                                    error.printStackTrace();
                                                    Toast.makeText(getBaseContext(),
                                                            "HTTP error", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                            queue.add(profilRequest);

                                        }
                                        else
                                        {
                                            login.setText("");
                                            passwd.setText("");
                                            login.requestFocus();
                                            Toast.makeText(getBaseContext(),
                                                    "Login error", Toast.LENGTH_LONG).show();
                                        }





                                    }else
                                    {
                                        loggedIn.setName(reader.getString("name"));
                                        login.setText("");
                                        passwd.setText("");
                                        login.requestFocus();
                                        Toast.makeText(getBaseContext(),
                                                "Nie ma takiego użytkownika", Toast.LENGTH_LONG).show();

                                    }
                                } catch (JSONException e) {
                                    login.setText("");
                                    passwd.setText("");
                                    login.requestFocus();
                                    e.printStackTrace();
                                    Toast.makeText(getBaseContext(),
                                            "JSON error", Toast.LENGTH_LONG).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        login.setText("");
                        passwd.setText("");
                        login.requestFocus();
                        error.printStackTrace();
                        Toast.makeText(getBaseContext(),
                                "HTTP error", Toast.LENGTH_LONG).show();
                    }
                });
                queue.add(userRequest);




            }
        };
        loginBtn.setOnClickListener(loginListener);



        //nowe konto
        newAcc.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {


                System.out.println("start dialog");



                final AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext())
                        .setPositiveButton("Dodaj", null)
                        .setNegativeButton("Zamknij",null)
                        .setTitle("Nowe Konto")
                        ;




                Context context = v.getContext();
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

// Add a TextView here for the "Title" label, as noted in the comments
                final EditText login = new EditText(context);
                login.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                login.setHint("Login");
                layout.addView(login); // Notice this is an add method

// Add another TextView here for the "Description" label
                final EditText haslo = new EditText(context);
                haslo.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                haslo.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                haslo.setHint("Hasło");
                layout.addView(haslo); // Another add method

                final EditText haslo2 = new EditText(context);
                haslo2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                haslo2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                haslo2.setHint("Ponów hasło");
                layout.addView(haslo2); // Another add method

                System.out.println("alert window");



                dialog.setView(layout);
                final AlertDialog alert = dialog.create();
                alert.show();

                Button positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View v) {


                        if(haslo.getText().toString().equals(haslo2.getText().toString()) && (login.getText().length() > 5) && (haslo.getText().length() > 5)) {

                            RequestQueue queue = Volley.newRequestQueue(v.getContext());


                            String url = v.getContext().getResources().getString(R.string.api_path) + "newacc/" + md5.getMd5(login.getText().toString()) + "/" + md5.getMd5(haslo.getText().toString());
                            System.out.print(url);


                            StringRequest newAccountRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the first 500 characters of the response string.

                                            System.out.println("Tutaj jestesmy");

                                            if (response.equals("Utworzono konto")) {

                                                alert.dismiss();
                                                Toast.makeText(getBaseContext(),
                                                        response, Toast.LENGTH_LONG).show();

                                            } else {
                                                login.setText("");
                                                haslo.setText("");
                                                haslo2.setText("");
                                                login.requestFocus();

                                                Toast.makeText(getBaseContext(),
                                                        response, Toast.LENGTH_LONG).show();
                                            }


                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    login.setText("");
                                    haslo.setText("");
                                    haslo2.setText("");
                                    login.requestFocus();
                                    error.printStackTrace();
                                    Toast.makeText(getBaseContext(),
                                            "HTTP error", Toast.LENGTH_LONG).show();
                                }
                            });
                            queue.add(newAccountRequest);
                        }
                        else if (5 >= login.getText().length())
                        {
                            login.setText("");

                            login.requestFocus();

                            Toast.makeText(getBaseContext(),
                                    "Za krótki login", Toast.LENGTH_LONG).show();
                        }
                        else if(!haslo.getText().toString().equals(haslo2.getText().toString()))
                        {
                            haslo.setText("");
                            haslo2.setText("");
                            haslo.requestFocus();

                            Toast.makeText(getBaseContext(),
                                    "Niezgodne hasła", Toast.LENGTH_LONG).show();
                        }
                        else if(5 >= haslo.getText().length())
                        {
                            haslo.setText("");
                            haslo2.setText("");
                            haslo.requestFocus();

                            Toast.makeText(getBaseContext(),
                                    "Za krótkie hasło", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            login.setText("");
                            haslo.setText("");
                            haslo2.setText("");
                            login.requestFocus();
                            Toast.makeText(getBaseContext(),
                                    "Error", Toast.LENGTH_LONG).show();
                        }
                        }
                });

            }
        });

    }

    //zapis i wyszukiwanie zapisanego konta
    public void savePref(String log, String pass)
    {
        SharedPreferences myPrefs = getSharedPreferences("logs",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("log", log);
        editor.putString("pass", pass);
        editor.apply();
    }

    public String searchPref(String key)
    {
        SharedPreferences myPrefs = getSharedPreferences("logs",Context.MODE_PRIVATE);
        String result = myPrefs.getString(key,"error");
        return result;
    }


}
