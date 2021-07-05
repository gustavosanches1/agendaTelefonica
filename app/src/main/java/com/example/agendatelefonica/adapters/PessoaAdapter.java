package com.example.agendatelefonica.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agendatelefonica.R;
import com.example.agendatelefonica.model.Pessoa;

import java.util.List;


public class PessoaAdapter extends BaseAdapter {
    private List<Pessoa> pessoas;
    private Activity activity;

    public PessoaAdapter(Activity activity, List<Pessoa> pessoas) {
        this.activity = activity;
        this.pessoas = pessoas;
    }
    @Override
    public int getCount() {
        return pessoas.size();
    }

    @Override
    public Object getItem(int i) {
        return pessoas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return pessoas.get(i).getId();
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = activity.getLayoutInflater().inflate(R.layout.item, viewGroup, false);
        TextView codigo = v.findViewById(R.id.txtId);
        TextView nome = v.findViewById(R.id.txtNomeAtt);
        TextView telefone = v.findViewById(R.id.txtTel);
        TextView endereco = v.findViewById(R.id.txtEndereco);

        Pessoa p = pessoas.get(i);
        codigo.setText(p.getId()+"");
        nome.setText(p.getNome());
        telefone.setText(p.getTelefone());
        endereco.setText(p.getEndereco());
        return v;
    }
}
