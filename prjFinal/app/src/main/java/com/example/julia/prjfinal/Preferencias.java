package com.example.julia.prjfinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Preferencias extends AppCompatActivity
{
    EditText txtMensagem;
    Button btnConcluir;
    Banco banco;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);

        banco = new Banco(getBaseContext());

        txtMensagem = (EditText) findViewById(R.id.txtMensagem);
        btnConcluir = (Button) findViewById(R.id.btnConcluir);

        txtMensagem.setText(banco.recuperarMensagem());

        btnConcluir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Usuario usuario = new Usuario();
                usuario.setMensagem(txtMensagem.getText().toString());
                banco.inserirMensagem(usuario.getMensagem());
                Toast.makeText(getBaseContext(), "Alterações realizadas com sucesso", Toast.LENGTH_LONG).show();
            }
        });
    }
}
