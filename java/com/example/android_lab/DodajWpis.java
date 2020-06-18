package com.example.android_lab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import static java.lang.Float.*;

public class DodajWpis extends AppCompatActivity {

    private int modyfi_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_wpis);

        ArrayAdapter gatunki = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, new String[] {"Pies", "Kot", "Rybki"});
        Spinner gatunek = findViewById(R.id.gatunek);
        gatunek.setAdapter(gatunki);

        Bundle extras = getIntent().getExtras();
        try {
            if(extras.getSerializable("element") != null) {
                Animal zwierz = (Animal) extras.getSerializable("element");
                EditText kolor = (EditText) findViewById(R.id.kolor);
                EditText wielkosc = (EditText) findViewById(R.id.wielkosc);
                EditText opis = (EditText) findViewById(R.id.opis);
                kolor.setText(zwierz.getKolor());
                wielkosc.setText(Float.toString(zwierz.getWielkosc()));
                opis.setText(zwierz.getOpis());

                this.modyfi_id=zwierz.get_id();
            }
        } catch(Exception ex) {
            this.modyfi_id=0;}
    }

    public void wyslij (View view)
    {
        EditText kolor = findViewById(R.id.kolor);
        EditText wielkosc = findViewById (R.id.wielkosc);
        EditText opis = findViewById(R.id.opis);
        Spinner gatunek = findViewById(R.id.gatunek);

        Animal zwierze = new Animal(
                gatunek.getSelectedItem().toString(),
                kolor.getText().toString(),
                Float.valueOf(wielkosc.getText().toString()),
                opis.getText().toString()
        );
        zwierze.set_id(this.modyfi_id);

        Intent intencja = new Intent();
        intencja.putExtra("nowy", zwierze);
        setResult(RESULT_OK, intencja);
        finish();
    }
}