package com.example.julia.prjfinal;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Banco banco;

    ImageButton btnAviso;

    ArrayList<Contato> listaContatos;

    FusedLocationProviderClient fusedLocationProviderClient;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        banco = new Banco(getBaseContext());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnAviso = (ImageButton) findViewById(R.id.btnAviso);

        listaContatos = new ArrayList<>();

        btnAviso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listaContatos = banco.recuperarRegistros();

                if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            0);
                } else {
                    if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.SEND_SMS)
                            == PackageManager.PERMISSION_GRANTED)
                    {
                        String mensagem;
                        mensagem = banco.recuperarMensagem();
                        enviarSMS(mensagem);
                    }
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnAjustes:
                Intent preferencias = new Intent(getBaseContext(), Ajustes.class);
                startActivity(preferencias);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    String mensagem;
                    mensagem = banco.recuperarMensagem();
                    enviarSMS(mensagem);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

    public void enviarSMS(String msg) {

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
        }
        else
        {
            if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED)
            {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>()
                {
                    @Override
                    public void onSuccess(Location location)
                    {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                });

                for(Contato contato:listaContatos)
                {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(contato.getTelefone(), null, msg, null, null);

                    String smsLocalizacao = "http://maps.google.com?q=" + latitude + "," + longitude;
                    smsManager.sendTextMessage(contato.getTelefone(), null, smsLocalizacao, null, null);

                    Toast.makeText(getApplicationContext(), "SMS sent." + smsLocalizacao,
                            Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
