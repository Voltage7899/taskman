package com.company.taskman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class item_task extends AppCompatActivity {

    private String id_from_intent;
    private DatabaseReference databaseReference;
    private TextView theme,desc,status;
    private DatabaseReference database;
    private Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_task);
        //Привязываем поля к переменным
        theme=findViewById(R.id.theme_item);
        desc=findViewById(R.id.desc_item);
        status=findViewById(R.id.status_item);
        update=findViewById(R.id.update_item);

        //Получаем данные из интента
        id_from_intent=getIntent().getExtras().get("task_id").toString();
        //получаем ссылку
        databaseReference= FirebaseDatabase.getInstance().getReference();
        //слушатель на взаимодействие с базой данных
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("task").child(id_from_intent).exists()){
                    task task=snapshot.child("task").child(id_from_intent).getValue(task.class);
                    theme.setText(task.getTheme());
                    desc.setText(task.getDescription());
                    status.setText(task.getStatus());

                }
                else {
                    //Выводит всплывающее сообщение
                    Toast.makeText(item_task.this, "Таска не существует", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> taskMap = new HashMap<>();
                taskMap.put("id",id_from_intent);
                taskMap.put("theme",theme.getText().toString());
                taskMap.put("description",desc.getText().toString());
                taskMap.put("status",status.getText().toString());
                database= FirebaseDatabase.getInstance().getReference("task");

                database.child(id_from_intent).updateChildren(taskMap);

                finish();

            }
        });


    }
}