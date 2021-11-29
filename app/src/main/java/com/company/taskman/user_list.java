package com.company.taskman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class user_list extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static  View.OnClickListener listener;


    private DatabaseReference database;
    private ItemViewHolder itemViewHolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        recyclerView=findViewById(R.id.recyclerView_User);

        initRecyclerView();
    }
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    protected void onStart() {
        super.onStart();

        database= FirebaseDatabase.getInstance().getReference().child("task");
        FirebaseRecyclerOptions<task> options=new FirebaseRecyclerOptions.Builder<task>()
                .setQuery(database,task.class).build();
        FirebaseRecyclerAdapter<task, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<task, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull task model) {

                holder.theme.setText("Тема "+model.getTheme());
                holder.desc.setText("Описание "+model.getDescription());
                holder.status.setText("Статус "+model.getStatus());



                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task_id=getRef(position).getKey();


                        Intent intent=new Intent(user_list.this,Item_User.class);
                        intent.putExtra("task_id",task_id);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element,parent,false);
                itemViewHolder=new ItemViewHolder(view);
                return itemViewHolder;
            }
        };
        recyclerView.setAdapter(adapter);//Устанавливаем адаптер
        adapter.startListening();

    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView theme,desc,status;



        public ItemViewHolder(View view) {
            super(view);
            theme = view.findViewById(R.id.theme_el);
            desc=view.findViewById(R.id.desc_el);
            status=view.findViewById(R.id.status_el);


        }
    }
}