package com.company.taskman;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

public class admin_list extends AppCompatActivity {

    //Переменная Обновляемый список
    private RecyclerView recyclerView;
    private Button add_new;

    private DatabaseReference database;
    //Переменная держателя образа элемента
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        //Привязываем обновляемый список к переменной
        recyclerView=findViewById(R.id.recyclerView_admin);
        add_new=findViewById(R.id.new_admin);

        //Кнопка для перехода на добавление товара
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(admin_list.this,add_new_task.class);
                startActivity(intent);
            }
        });

        //Иницилизация обновляемого списка
        initRecyclerView();
    }

    private void initRecyclerView() {
        //Устанавливаем расположение списка
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Декорация для разделения элементов
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);



        //Участок кода отвечающий за свайп влево и удаление
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position=viewHolder.getAdapterPosition();
                //После свайпа происходит удаление,за счет того,что мы получаем ссылку на объект и затем удаляем его из таблицы
                Log.d(TAG,"Позиция элемента "+((FirebaseRecyclerAdapter)recyclerView.getAdapter()).getRef(position));
                //database.child().removeValue();
                ((FirebaseRecyclerAdapter)recyclerView.getAdapter()).getRef(position).removeValue();
            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Получаем ссылку на таблицу с товаром
        database= FirebaseDatabase.getInstance().getReference().child("task");
        //Прописываем настройки для обновляемого списка,заточенного для Firebase,он из отдельной библиотеки подключенной в градле,последняя имплементация
        FirebaseRecyclerOptions<task> options=new FirebaseRecyclerOptions.Builder<task>()//Грубо говоря,данные поступающие из базы конвертируются в тип объекта Sweat
                .setQuery(database,task.class).build();
        //Далее мы прописываем адаптер,как и куда вставляются данные из объекта Sweat
        FirebaseRecyclerAdapter<task, ItemViewHolder> adapter = new FirebaseRecyclerAdapter<task, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull task model) {
                Log.d(TAG,"DESC: "+model.getDescription());
                //Собественно сдесь и вставляются
                holder.theme.setText("Тема "+model.getTheme());
                holder.desc.setText("Описание "+model.getDescription());
                holder.status.setText("Статус "+model.getStatus());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String task_id=getRef(position).getKey();

                        Intent intent=new Intent(admin_list.this,item_task.class);
                        intent.putExtra("task_id",task_id);
                        startActivity(intent);
                    }
                });

            }


            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                //Прописываем обертку,как она будет выглядеть
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element,parent,false);//в данном случае прообраз это лист элемент
                ItemViewHolder itemViewHolder=new ItemViewHolder(view);
                //Пихаем в переменную обертки созданную нами обертку,чуть выше и возвращаем ее.
                return itemViewHolder;
            }
        };
        firebaseRecyclerAdapter=adapter;
        recyclerView.setAdapter(adapter);//Устанавливаем адаптер
        adapter.startListening();//на адаптер устанавливаем слушатель,когда данные будут меняться,это сразу отобразиться,воть
        //Все тоже самое и в списке для юзера

    }
    public  class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView theme,desc,status;



        public ItemViewHolder(View view) {
            super(view);
            theme = view.findViewById(R.id.theme_el);
            desc=view.findViewById(R.id.desc_el);
            status=view.findViewById(R.id.status_el);



        }
    }
}