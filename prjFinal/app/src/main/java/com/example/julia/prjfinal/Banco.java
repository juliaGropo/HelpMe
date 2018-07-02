package com.example.julia.prjfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julia on 15/06/2018.
 */

public class Banco extends SQLiteOpenHelper
{
    public static final String BANCO = "BD_HELPME";
    public static final int VERSAO = 3;

    public Banco(Context context)
    {
        super(context, BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE contato (id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT, telefone TEXT);");
        db.execSQL("CREATE TABLE usuario (id INTEGER PRIMARY KEY AUTOINCREMENT, mensagem TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists contato");
        db.execSQL("drop table if exists usuario");
        onCreate(db);
    }

    public void inserirRegistro(String nome, String telefone)
    {
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("telefone", telefone);

        banco.insert("contato", null, values);

        banco.close();
    }

    public void atualizarRegistro(String id, String nome, String telefone)
    {
        SQLiteDatabase banco = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("nome", nome);
        values.put("telefone", telefone);

        banco.update("contato", values, "id=?", new String[]{id});

        banco.close();
    }

    public ArrayList<Contato> recuperarRegistros()
    {
        ArrayList<Contato> listaContatos = new ArrayList<Contato>();

        SQLiteDatabase banco = this.getReadableDatabase();

        Cursor cursor = banco.rawQuery("select * from contato;", new String[]{});

        if(cursor.moveToFirst())
        {
            do
            {
                Contato contato = new Contato();
                int id;
                id = cursor.getInt(cursor.getColumnIndex("id"));
                contato.setId(String.valueOf(id));
                contato.setNome(cursor.getString(cursor.getColumnIndex("nome")));
                contato.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));

                listaContatos.add(contato);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        banco.close();

        return listaContatos;

    }

    public void excluirTudo(String tabela)
    {
        SQLiteDatabase banco = getWritableDatabase();
        banco.delete(tabela, null, new String[]{});
        banco.close();
    }

    public void inserirMensagem(String mensagem)
    {
        SQLiteDatabase banco = this.getWritableDatabase();
        banco.delete("usuario", null, new String[]{});
        ContentValues values = new ContentValues();
        values.put("mensagem", mensagem);
        System.out.println("AQUIIIIIIIIIII " + mensagem);
        banco.insert("usuario", null, values);
        banco.close();
    }

    public String recuperarMensagem()
    {
        String mensagem = null;

        SQLiteDatabase banco = this.getReadableDatabase();

        Cursor cursor = banco.rawQuery("select * from usuario;", new String[]{});

        if(cursor.moveToFirst())
        {
            do
            {
                Usuario usuario = new Usuario();

                usuario.setMensagem(cursor.getString(cursor.getColumnIndex("mensagem")));

                mensagem = usuario.getMensagem();
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        banco.close();

        return mensagem;
    }

    public void excluirContato(String id)
    {
        SQLiteDatabase banco = this.getWritableDatabase();
        banco.delete("contato", "id=?", new String[]{id});
        banco.close();
    }
}
