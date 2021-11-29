package com.company.taskman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class add_new_task extends AppCompatActivity {

    //Переменые для записи данных из полей и путь для картинки(URI)
    private String id, theme, desc,status;
    //Переменные полей
    private EditText theme_field, desc_field,status_field;
    private ImageView img_field;
    private Button add_db;
    //ссылку ты уже знаешь
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);

        theme_field = findViewById(R.id.theme_add);
        desc_field=findViewById(R.id.desc_add);
        status_field=findViewById(R.id.status_add);

        add_db = findViewById(R.id.add);
        //устанавливаем слушателя
        add_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Как только прошли проверку на заполнение полей и картинки,то добавляем данные в бд
                theme = theme_field.getText().toString();
                desc = desc_field.getText().toString();
                status = status_field.getText().toString();




                if (TextUtils.isEmpty(theme)) {
                    Toast.makeText(add_new_task.this, "Добавьте тему", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(desc)) {
                    Toast.makeText(add_new_task.this, "Добавьте описание", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(status)) {
                    Toast.makeText(add_new_task.this, "Добавьте статус", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Функция для добавления
                    AddInDataBase();
                }


            }

        });

    }


    private void AddInDataBase()
    {
        //Сдесь мы устанавливаем нужный айдишник и приводим его в инту
        id=Integer.toString((int) (1+Math.random()*1000));

        Log.d(TAG,"Айдишник при добавлении "+id);

        database= FirebaseDatabase.getInstance().getReference("task");

        HashMap<String,Object> taskMap = new HashMap<>();

        taskMap.put("id",id);
        taskMap.put("theme",theme);
        taskMap.put("description",desc);
        taskMap.put("status",status);


        //Здесь мы пихаем данные в бд через Хэшмэп и делаем проверку на результат
        database.child(id).updateChildren(taskMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(add_new_task.this, "task добавлен", Toast.LENGTH_SHORT).show();
                    //finish возвращает нас обратно если успешно
                    finish();
                }
                else {
                    Toast.makeText(add_new_task.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}