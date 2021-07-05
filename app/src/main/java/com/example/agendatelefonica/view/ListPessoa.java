package com.example.agendatelefonica.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ListMenuItemView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;

import com.example.agendatelefonica.DAO.PessoaDAO;
import com.example.agendatelefonica.R;
import com.example.agendatelefonica.adapters.PessoaAdapter;
import com.example.agendatelefonica.controller.ConexaoSQLite;
import com.example.agendatelefonica.model.Pessoa;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ListPessoa extends AppCompatActivity {
    private Button btnAnterior, btnPosterior;
    private ListView listView;
    private PessoaDAO dao;
    private List<Pessoa>pessoas;
    private List<Pessoa> pessoaFiltrados = new ArrayList<>();
    private int paginaAtual = 1;
    private final static int QUANTIDADE_REGISTROS_POR_PAGINA = 15;
    private String descricaoParaPesquisa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pessoa);

        btnAnterior = findViewById(R.id.btnAnterior);
        btnPosterior = findViewById(R.id.btnPosteriores);

        listView = findViewById(R.id.lista_pessoas);
        dao = new PessoaDAO(this);
        carregarProdutos();
        //ArrayAdapter<Produto> adaptador = new ArrayAdapter<Produto>(this, android.R.layout.simple_list_item_1, produtoFiltrados);
        PessoaAdapter adaptador = new PessoaAdapter(this, pessoaFiltrados);
        listView.setAdapter(adaptador);

        //quando for precionado vai abrir o contexto
        registerForContextMenu(listView);
    }

    //MENU DE ITEM
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_principal, menu);

        SearchView sv = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                procuraPessoas(s);
                return false;
            }
        });

        return true;
    }

    //menu de contexto atualizar e excluir
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.menu_contexto, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Pessoa pessoaSelecionado = (Pessoa) listView.getItemAtPosition(info.position);
        Log.e("Pessoa selecionada", pessoaSelecionado.getNome());

        switch (item.getItemId()) {
            case R.id.opcaoExcluir:
                excluir(pessoaSelecionado);
                return true;
            case R.id.opcaoAtualizar:
                atualizar(pessoaSelecionado);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    //BUSCA DE PRODUTO NA BARRRA
    public void procuraPessoas (String nome){
        this.descricaoParaPesquisa = nome;
        paginaAtual = 1;

        onResume();
    }

    //consulta  da lista atualizada
    @Override
    public void onResume(){
        Log.d("Pagina Atual: ", String.valueOf(paginaAtual));
        super.onResume();
        carregarProdutos();
        verificarPaginacao();
        listView.invalidateViews();
    }

    private void verificarPaginacao(){
        //desabilitar pagina anterior qunando estiver na pagina 1
        if (paginaAtual==1){
            btnAnterior.setEnabled(false);
        }else{
            btnAnterior.setEnabled(true);
        }
        //desabilitar proxima pagina qunando nao ha itens pra ela
        boolean possuiMaisPaginas;
        if (descricaoParaPesquisa==null || descricaoParaPesquisa.isEmpty()){
            possuiMaisPaginas = !dao.obterTodos(paginaAtual+1, QUANTIDADE_REGISTROS_POR_PAGINA).isEmpty();
        }else{
            possuiMaisPaginas = !dao.obterTodosComDescricao(paginaAtual+1, QUANTIDADE_REGISTROS_POR_PAGINA, descricaoParaPesquisa).isEmpty();
        }
        btnPosterior.setEnabled(possuiMaisPaginas);

    }

    private void carregarProdutos(){
        if (descricaoParaPesquisa==null || descricaoParaPesquisa.isEmpty()){
            pessoas = dao.obterTodos(paginaAtual, QUANTIDADE_REGISTROS_POR_PAGINA);
        }else{
            pessoas = dao.obterTodosComDescricao(paginaAtual, QUANTIDADE_REGISTROS_POR_PAGINA, descricaoParaPesquisa);
        }
        pessoaFiltrados.clear();
        pessoaFiltrados.addAll(pessoas);
    }


    public void proximaPagina(View view){
        paginaAtual++;
        onResume();
    }

    public void paginaAnterior(View view){
        paginaAtual--;
        onResume();
    }


    //chamar a tela de cadastro de produtos no menu principal xml
    public void cadastrar(MenuItem item){
        Intent it = new Intent(this, AgendarPessoa.class);
        startActivity(it);
        finish();
    }

    //chamar o menu de atualiza produtos
    public void atualizar(final Pessoa pessoa){
        Intent it = new Intent(this, AgendarPessoa.class);
        it.putExtra("pessoa", pessoa);
        startActivity(it);
    }

    public void excluir(final Pessoa pessoa){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("Realmente deseja Excluir ?")
                .setNegativeButton("Não", null)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pessoaFiltrados.remove(pessoa);
                        pessoas.remove(pessoa);
                        dao.delete(pessoa);
                        listView.invalidateViews();
                    }
                }).create();
        dialog.show();
    }

}




