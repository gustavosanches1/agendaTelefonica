package com.example.agendatelefonica.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.agendatelefonica.controller.ConexaoSQLite;
import com.example.agendatelefonica.model.Pessoa;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PessoaDAO {
    private ConexaoSQLite conexao;
    private SQLiteDatabase agenda;
    private Pessoa pessoa = null;

    public PessoaDAO (Context context){
        conexao = new ConexaoSQLite(context);
        agenda = conexao.getWritableDatabase();

    }

    public long insert(Pessoa pessoa){
        ContentValues values = toContentValues(pessoa);
        return agenda.insert("pessoa", null, values);
    }

    public List<Pessoa>obterTodos(int pagina, int quantidade){
        Cursor cursor = agenda.query("pessoa", null,
                null, null, null,null,"id DESC", toLimit(pagina, quantidade));
        return toPessoas(cursor);
    }


    public List<Pessoa>obterTodosComDescricao(int pagina, int quantidade, String nome){
        Cursor cursor = agenda.query("pessoa", null,
                "nome like ?", new String[]{"%"+nome+"%"}, null,null,"nome DESC", toLimit(pagina, quantidade));
        return toPessoas(cursor);
    }

    public void delete (Pessoa p){
        //Log.d("produto este",p.getId()+"");//id sempre 0, não existe id zero...
        agenda.delete("pessoa", "id = ?", new String[]{String.valueOf(p.getId())});
    }

    public int update (Pessoa pessoa){
        ContentValues values = toContentValues(pessoa);
        return agenda.update("pessoa", values, "id = ?", new String[]{String.valueOf(pessoa.getId())});
    }

    public Pessoa obterPorCodigo(String codigo){
        Cursor cursor = agenda.query("pessoa", null, "id = ?", new String[]{codigo},
                null,null,null);
        while (cursor.moveToNext()){
            return toPessoa(cursor);
        }
        return null;
    }

    public Pessoa obterPorCodigoEDescricao(String codigo, String nome){
        Cursor cursor = agenda.query("pessoa", null, "id = ? and nome = ?", new String[]{codigo, nome},
                null,null,null);
        while (cursor.moveToNext()){
            return toPessoa(cursor);
        }
        return null;
    }

    private String toLimit(int pagina, int quantidadeItensPorPagina){
        int offset = pagina == 1 ? 0 : (pagina-1) * quantidadeItensPorPagina;
        Log.d("LIMIT", String.format("%s, %s", offset, quantidadeItensPorPagina));
        return String.format("%s, %s", offset, quantidadeItensPorPagina);
    }

    private ContentValues toContentValues(Pessoa pessoa){
        ContentValues values = new ContentValues();
        values.put("nome", pessoa.getNome());
        values.put("telefone", pessoa.getTelefone());
        values.put("endereco", pessoa.getEndereco());
        return values;
    }

    private Pessoa toPessoa(Cursor cursor){
        Pessoa p = new Pessoa();
        p.setId(cursor.getInt(0));
        p.setNome(cursor.getString( 1));
        p.setTelefone(cursor.getString( 2));
        p.setEndereco(cursor.getString( 3));// quantidade
        return p;
    }

    private List<Pessoa> toPessoas(Cursor cursor){
        List<Pessoa> pessoas = new ArrayList<>();
        while (cursor.moveToNext()){
            pessoas.add(toPessoa(cursor));
        }
        return pessoas;
    }
}
