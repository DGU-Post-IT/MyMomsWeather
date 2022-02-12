package com.postit.mymomsweather.presentation.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postit.mymomsweather.R;
import com.postit.mymomsweather.model.EmotionRecord;
import com.postit.mymomsweather.databinding.ItemEmotionWeatherBinding;

import java.util.ArrayList;

public class InfoBoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    ArrayList<EmotionRecord> data = new ArrayList<>();

    InfoBoardAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(ItemEmotionWeatherBinding.inflate(LayoutInflater.from(mContext),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((WeatherViewHolder)(holder)).bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void setItem(EmotionRecord er){
        data.clear();
        data.add(er);
        notifyDataSetChanged();
    }

    class WeatherViewHolder extends RecyclerView.ViewHolder{

        ItemEmotionWeatherBinding binding;
        public WeatherViewHolder(ItemEmotionWeatherBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(EmotionRecord er){
            if(er == null){
                return;
            }
            Log.d("emotion",er.getEmotion()+" ");
            switch (er.getEmotion()){
                case 0:
                    binding.weatherImageView.setImageResource(R.drawable.ic_weather_good);
                    binding.weatherDescriptionTextView.setText("좋아요");
                    break;
                case 1:
                    binding.weatherImageView.setImageResource(R.drawable.ic_weather_soso);
                    binding.weatherDescriptionTextView.setText("슬픔");
                    break;
                case 2:
                    binding.weatherImageView.setImageResource(R.drawable.ic_weather_angry);
                    binding.weatherDescriptionTextView.setText("화나요");
                    break;
                case 3:
                    binding.weatherImageView.setImageResource(R.drawable.ic_weather_sad);
                    binding.weatherDescriptionTextView.setText("불안");
                    break;
                case 4:
                    binding.weatherImageView.setImageResource(R.drawable.ic_weather_wound);
                    binding.weatherDescriptionTextView.setText("상처");
                    break;
                case 5:
                    binding.weatherImageView.setImageResource(R.drawable.ic_weather_embarassed);
                    binding.weatherDescriptionTextView.setText("당황");
                    break;
                default:
                    binding.weatherImageView.setImageResource(R.drawable.ic_weather_nono);
                    binding.weatherDescriptionTextView.setText("어제 기록이 없어요");
                    break;

            }
        }


    }


}
