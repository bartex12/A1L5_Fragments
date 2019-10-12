package com.geekbrains.city_whether.cityAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.geekbrains.city_whether.R;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewCityAdapter extends RecyclerView.Adapter<RecyclerViewCityAdapter.ViewHolder>{
    private static final String TAG = "33333";
    private ArrayList<String> data;
    Context context;
    private OnCityClickListener onCityClickListener;

    public interface OnCityClickListener {
        void onCityClick(String city, int position);
    }

    public RecyclerViewCityAdapter( ArrayList<String> data, OnCityClickListener onCityClickListener){
        if (data!=null){
            this.data = data;
        }
        this.onCityClickListener = onCityClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_list,
                parent, false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       final int pos = position;
        final String[] towns = context.getResources().getStringArray(R.array.towns);
        holder.textView.setText(data.get(position));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = data.get(pos);
                for (int i = 0; i<towns.length; i++){
                    if (towns[i].equals(city)){
                        Log.d(TAG, "RecyclerViewCityAdapter onBindViewHolder city =  "+ city+
                                " cityPosition = " + i);
                        //вызываем метод интерфейса onCityClick и передаём в него название и позицию города
                        //метод сработает у всех подписанных на него - у нас  в ChooseCityFrag
                        onCityClickListener.onCityClick(city, i);

                        return;
                    }
                }
            }
        });
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
