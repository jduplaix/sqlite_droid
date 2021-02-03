package com.example.sqlite_droid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlite_droid.db.PhoneBookDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PhoneBookDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instanciation base SQLite
        dbHelper = new PhoneBookDbHelper(this);

        findViewById(R.id.save).setOnClickListener(v -> {
            EditText name = findViewById(R.id.name);
            EditText number = findViewById(R.id.number);
            long id = saveNumber(name.getText().toString(), number.getText().toString());
            Toast.makeText(MainActivity.this, "Numéro enregistré avec l'id " + id, Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.search).setOnClickListener(v -> {
            EditText nameSearch = findViewById(R.id.nameSearch);
            List<String> numbers = getNumbers(nameSearch.getText().toString());
            Toast.makeText(MainActivity.this, "Numéro : " + TextUtils.join(", ", numbers), Toast.LENGTH_SHORT).show();
        });
    }

    //Fermeture base SQLite à la fermeture de l'appli
    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }

    //Enregistrement couple nom/num
    private long saveNumber(String name, String number){
        //Connexion à la db
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Mapping des valeurs à insérer
        ContentValues values = new ContentValues();
        values.put(PhoneBookDbHelper.COLUMN_NAME_NAME, name);
        values.put(PhoneBookDbHelper.COLUMN_NAME_NUMBER, number);

        //insertion de la ligne
        return db.insert(PhoneBookDbHelper.TABLE_NAME,null, values);
    }

    //Methode de recherche dans la base
    private List<String> getNumbers(String name){
        //Connexion à la db
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //projection = colonnes retournées par la req SELECT
        String[] projection = {
                PhoneBookDbHelper.COLUMN_NAME_NUMBER
        };

        //Filtre "WHERE name = ''"
        String selection = PhoneBookDbHelper.COLUMN_NAME_NAME + " = ?";
        String[] selectionArgs = {name};

        //Tri ORDER BY
        String sortOrder = PhoneBookDbHelper.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                PhoneBookDbHelper.TABLE_NAME, //La table
                projection, //résultats
                selection, //la clause WHERE
                selectionArgs, // les valeurs de la clause WHERE
                null, //GROUP BY
                null, //HAVING
                sortOrder //SORT BY
        );

        //conversion du curseur en liste
        List<String> result = new ArrayList<>();
        while(cursor.moveToNext()){
            String number = cursor.getString(cursor.getColumnIndexOrThrow(PhoneBookDbHelper.COLUMN_NAME_NUMBER));
            result.add(number);
        }
        cursor.close();

        return result;
    }
}