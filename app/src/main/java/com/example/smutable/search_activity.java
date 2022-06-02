package com.example.smutable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class search_activity extends AppCompatActivity {

    private final List<item> items = new ArrayList<>();
    private final RecyclerView.Adapter adapter = new ItemAdapter(this.items);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


    }
    private static final class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private final List<item> items;
            public ItemAdapter(List<item> items){
                this.items = items;
            }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return this.items.size();
        }
    }

}
