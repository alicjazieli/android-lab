package com.example.android_lab;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> target;
    private SimpleCursorAdapter adapter;
    MySQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new MySQLite(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.target = new ArrayList<>();
        String[] values = new String[]{
                "Pies", "Kot", "Koń", "Gołąb", "Kruk", "Dzik", "Karp", "Osioł", "Chomik", "Mysz",
                "Jeż", "Karaluch"
        };

        this.target.addAll(Arrays.asList(values));
        this.adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                db.lista(),
                new String[] {"_id", "gatunek", "opis"},
                new int[] {android.R.id.text1,
                        android.R.id.text2},

                SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE
        );

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(this.adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
                TextView name = view.findViewById(android.R.id.text1);
                Animal zwierz = db.pobierz(Integer.parseInt(name.getText().toString()));
                Intent intencja = new Intent(getApplicationContext(), DodajWpis.class);
                intencja.putExtra("element", zwierz);
                startActivityForResult(intencja, 2);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView name = view.findViewById(android.R.id.text1);
                db.usun(name.getText().toString());
                adapter.changeCursor(db.lista());
                adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Animal nowy = (Animal) extras.getSerializable("nowy");
            this.db.dodaj(nowy);
            adapter.changeCursor(db.lista());
            adapter.notifyDataSetChanged();
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Animal newEntry = (Animal) extras.getSerializable("nowy");
            this.db.aktualizuj(newEntry);
            adapter.changeCursor(db.lista());
            adapter.notifyDataSetChanged();
        }
    }

    public void onNewEntry(MenuItem mi) {
        Intent intention = new Intent(this, DodajWpis.class);
        startActivityForResult(intention, 1);
    }

}