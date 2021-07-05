package com.example.agendatelefonica.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agendatelefonica.DAO.PessoaDAO;
import com.example.agendatelefonica.R;
import com.example.agendatelefonica.controller.ConexaoSQLite;
import com.example.agendatelefonica.model.Pessoa;

public class AgendarPessoa extends AppCompatActivity {
    private Button btnGravar;
    private EditText nome, telefone, endereco;
    private Pessoa pessoa = null;
    private PessoaDAO dao;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendar_pessoa);

        nome = (EditText) findViewById(R.id.editNome);
        telefone = (EditText) findViewById(R.id.editTelefone);
        endereco = (EditText) findViewById(R.id.editEndereco);
        dao = new PessoaDAO(this);

        btnGravar = findViewById(R.id.btnGravar);


        Intent it = getIntent();
        if (it.hasExtra("pessoa")) {
            pessoa = (Pessoa) it.getSerializableExtra("pessoa");
            nome.setText(pessoa.getNome());
            telefone.setText(pessoa.getTelefone());
            endereco.setText(pessoa.getEndereco());
        }else{
            pessoa = new Pessoa();
        }
        preferences = getSharedPreferences("pessoa", getApplication().MODE_PRIVATE);
    }
    //crud
    public void gravar (View view){
        //Log.d("id",String.valueOf(codigo));
        pessoa.setNome(nome.getText().toString());
        pessoa.setTelefone(telefone.getText().toString());
        pessoa.setEndereco(endereco.getText().toString());
//        if (dao.obterPorCodigo(produto.getCodigo())!=null) {
//            Toast.makeText(this, "Codigo de produto ja Existente!", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (pessoa.getId() == null) {
            long id = dao.insert(pessoa);
            Toast.makeText(this, "Produto cadastrado com sucesso!" + id, Toast.LENGTH_SHORT).show();
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
            finish();
        }else {
            dao.update(pessoa);
            Toast.makeText(this, "Produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();
            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);
            finish();
        }
    }

    public void finish(Pessoa pessoa){
        Intent it = new Intent(this, MainActivity.class);
        it.putExtra("pessoa", pessoa);
        startActivity(it);
        finish();
    }
}