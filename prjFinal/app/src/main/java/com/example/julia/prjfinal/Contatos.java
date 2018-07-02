package com.example.julia.prjfinal;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Contatos extends AppCompatActivity
{
    Banco banco;

    Button btnSelecionar;

    ArrayAdapter adapter;

    ListView lstContatos;

    ArrayList<Contato> contatos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        banco = new Banco(getBaseContext());

        Toast.makeText(getBaseContext(), "OK", Toast.LENGTH_LONG).show();

        btnSelecionar = (Button) findViewById(R.id.btnSelecionar);
        lstContatos = (ListView) findViewById(R.id.lstContatos);

        contatos = new ArrayList<>();
        contatos = banco.recuperarRegistros();

        adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1);

        for (Contato contato : contatos)
        {
            adapter.add("Nome: " + contato.getNome() + " - Telefone: " + contato.getTelefone());
        }

        lstContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnSelecionar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, 0);
            }
        });

        lstContatos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                TextView tv = (TextView) view;

                for (Contato contato : contatos)
                {

                    if(tv.getText().equals("Nome: " + contato.getNome() + " - Telefone: " + contato.getTelefone()))
                    {
                        banco.excluirContato(contato.getId());
                        onResume();

                    }
                }
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK)
        {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};

            Cursor cursor = getApplicationContext().getContentResolver().query(contactUri, projection,
                    null, null, null);

            // If the cursor returned is valid, get the phone number
            if (cursor != null && cursor.moveToFirst())
            {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberIndex);

                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Nickname.DISPLAY_NAME);
                String name = cursor.getString(nameIndex);

                String numeroFormat = "";

                for(int i = 0; i < number.length(); i++)
                {
                    if(!number.substring(i, i + 1).equals("-") && !number.substring(i, i + 1).equals(" "))
                    {
                        numeroFormat = numeroFormat + number.substring(i, i + 1);
                    }
                }

                banco.inserirRegistro(name, numeroFormat);
                onResume();
            }

            cursor.close();
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        contatos = banco.recuperarRegistros();

        adapter.clear();

        for (Contato contato : contatos)
        {
            adapter.add("Nome: " + contato.getNome() + " - Telefone: " + contato.getTelefone());
        }

        lstContatos.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
