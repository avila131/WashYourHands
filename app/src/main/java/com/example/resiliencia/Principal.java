package com.example.resiliencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.logging.LogRecord;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.w3c.dom.Text;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.LogRecord;
import android.os.Bundle;

public class Principal extends AppCompatActivity{

    EditText texto_localidad, numeroLavadoManos;
    Button agregarValores;
    DatabaseReference reff;
    Persona persona;


    private ProgressBar p;
    private int prgStatus = 0;
    private TextView t;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);



        p = (ProgressBar) findViewById(R.id.p);
        t = (TextView) findViewById(R.id.t);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.dashboard:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.about:
                        startActivity(new Intent(getApplicationContext()
                                , about_activity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });


        // Inset data to database
        texto_localidad = (EditText) findViewById(R.id.localidadEditText);
        numeroLavadoManos = (EditText) findViewById(R.id.numberoLavadoEditText);

        agregarValores = (Button) findViewById(R.id.btnInsert);
        persona = new Persona();
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        agregarValores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numeroLavado = Integer.parseInt(numeroLavadoManos.getText().toString().trim());
                persona.setLocalidad(texto_localidad.getText().toString().trim());
                persona.setNumeroLavados(numeroLavado);

                reff.push().setValue(persona);
                prgStatus = 0;
                Toast.makeText(Principal.this, "correctly processed", Toast.LENGTH_LONG).show();

                int val = numeroLavado;
                new Thread(new Runnable() {

                    public void run() {
                        int numeroLavados = Integer.parseInt(numeroLavadoManos.getText().toString().trim());

                        while (prgStatus < 100) {
                            if (numeroLavados == 0)
                            {
                                prgStatus = 0;
                                break;
                            }

                            prgStatus += 1;

                            handler.post(new Runnable() {
                                public void run() {
                                    p.setProgress(prgStatus);
                                    t.setText(prgStatus+"/"+p.getMax());
                                }
                            });
                            try {

                                Thread.sleep(40);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            //int totalLavados = Integer.parseInt(numeroLavadoManos.getText().toString().trim());


                            if (prgStatus == numeroLavados*20)
                                break;


                        }
                    }
                }).start();


            }
        });






    }



}