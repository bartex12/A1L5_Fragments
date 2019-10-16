package com.geekbrains.city_weather.cityAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geekbrains.city_weather.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewCityAdapter extends RecyclerView.Adapter<RecyclerViewCityAdapter.ViewHolder>{
    private static final String TAG = "33333";
    private ArrayList<String> data;
    private Context context;
    private OnCityClickListener onCityClickListener;

    public RecyclerViewCityAdapter(ArrayList<String> data, OnCityClickListener onCityClickListener) {
        if (data != null) {
            this.data = data;
        }
        this.onCityClickListener = onCityClickListener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        final int pos = position;
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = data.get(pos);
                //вызываем метод интерфейса onCityClick и передаём в него название города
                //метод сработает у всех подписанных на него - у нас  в ChooseCityFrag
                Log.d(TAG, "RecyclerViewCityAdapter onBindViewHolder city =  " + city);
                onCityClickListener.onCityClick(city);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_list,
                parent, false);
        return new ViewHolder(view);
    }

    public interface OnCityClickListener {
        void onCityClick(String city);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewCity);
        }
    }
}
