package com.example.agendatelefonica.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agendatelefonica.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button agendar = (Button) findViewById(R.id.btnAgendar);
        Button lista = (Button) findViewById(R.id.btnListar);

        //chamando rebanho de telas
        agendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(MainActivity.this, AgendarPessoa.class);
                startActivity(it);
            }
        });


        lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(MainActivity.this, ListPessoa.class);
                startActivity(it);
            }
        });
    }
}