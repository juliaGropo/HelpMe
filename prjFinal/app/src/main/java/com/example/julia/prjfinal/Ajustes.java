package com.example.julia.prjfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

public class Ajustes extends AppCompatActivity
{
    TextView txtContatos;
    TextView txtPreferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        txtContatos = (TextView) findViewById(R.id.lblContatos);
        txtPreferencias = (TextView) findViewById(R.id.lblUsuario);

        txtContatos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent contatos = new Intent(getBaseContext(), Contatos.class);
                startActivity(contatos);
            }
        });

        txtPreferencias.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent preferencias = new Intent(getBaseContext(), Preferencias.class);
                startActivity(preferencias);
            }
        });

    }
}
