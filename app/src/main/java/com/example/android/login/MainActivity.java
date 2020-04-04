package com.example.android.login;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText name, number;
    Button login, register, show;

    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("db_exists", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        name = findViewById(R.id.name);
        number = findViewById(R.id.number);

        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        show = findViewById(R.id.show);

        textView = findViewById(R.id.text);


        createDB();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
    }

    public void createDB() {
        int isExists = sharedPreferences.getInt("db_exists", 0);

        if (isExists == 0) {
            SQLiteDatabase databaseUser = openOrCreateDatabase("User", MODE_PRIVATE, null);
            String createTableQuery = "CREATE TABLE IF NOT EXISTS User (name VARCHAR, number INT UNIQUE);";
            databaseUser.execSQL(createTableQuery);
            databaseUser.close();

            Toast.makeText(this, "Table User Created", Toast.LENGTH_LONG).show();
            editor.putInt("exists", 1);
            editor.commit();
        }
    }


    public void insert() {
        String userName = name.getText().toString();
        String userNumber = number.getText().toString();

        if (!userName.equals("") && !userNumber.equals("")) {
            SQLiteDatabase databaseUser = openOrCreateDatabase("User", MODE_PRIVATE, null);
            String query = "INSERT OR REPLACE INTO User VALUES('" + userName + "','" + userNumber + "');";
            databaseUser.execSQL(query);
            databaseUser.close();
        } else {
            Toast.makeText(this, "Field must not empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void check() {
        SQLiteDatabase databaseUser = openOrCreateDatabase("User", MODE_PRIVATE, null);
        Cursor cursor = databaseUser.rawQuery("SELECT * FROM User;", null);

        String userName = name.getText().toString();
        String userNumber = number.getText().toString();

        int rowCount = cursor.getCount();

        if (rowCount <= 0) {
            Toast.makeText(MainActivity.this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            do {
                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String number = cursor.getString(cursor.getColumnIndex("number"));

                if (userName.equals(name) && userNumber.equals(number)) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Fill the fields", Toast.LENGTH_SHORT).show();
                }
            } while (cursor.moveToNext());
        }
    }

    public void show() {

        SQLiteDatabase databaseUser = openOrCreateDatabase("User", MODE_PRIVATE, null);

        //Fetching the whole table into a cursor type variable
        Cursor cursor = databaseUser.rawQuery("SELECT * FROM user ;", null);

        //Counting the rows
        int rowCount = cursor.getCount();

        String result = "";

        //If row count is or less than 0 that means there is no data in the table
        if (rowCount <= 0) {
            Toast.makeText(MainActivity.this, "No data available", Toast.LENGTH_SHORT).show();
        } else {
            //If there is data then start iterating from the first data
            cursor.moveToFirst();

            do {
                //Fetching data from each column using the Cursor and getColumnIndex
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String number = cursor.getString(cursor.getColumnIndex("number"));


                result = result + name + " " + number + "" + "\n";


            } while (cursor.moveToNext());
        }
        textView.setText(result);
    }
}

