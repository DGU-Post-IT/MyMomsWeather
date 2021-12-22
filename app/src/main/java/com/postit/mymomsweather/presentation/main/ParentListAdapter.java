package com.postit.mymomsweather.presentation.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postit.mymomsweather.model.ParentUser;
import com.postit.mymomsweather.databinding.ItemParentDurationCallBinding;

import java.util.ArrayList;

public class ParentListAdapter extends RecyclerView.Adapter<ParentListAdapter.ParentCallInfoViewHolder> {

    ArrayList<ParentUser> data = new ArrayList<>();
    Context mContext;

    public ParentListAdapter(Context context){
        mContext=context;
    }

    @NonNull
    @Override
    public ParentCallInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ParentCallInfoViewHolder(ItemParentDurationCallBinding.inflate(LayoutInflater.from(mContext),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParentCallInfoViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ParentCallInfoViewHolder extends RecyclerView.ViewHolder{
        ItemParentDurationCallBinding binding;
        ParentCallInfoViewHolder(ItemParentDurationCallBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
        void bind(ParentUser parentUser){
            binding.nameTextView.setText(parentUser.getName());
            Long secs = Long.parseLong(parentUser.getCallDuration());
            //binding.durationTextView.setText(String.format("%03d:%02d:%02d", secs / 3600, (secs % 3600) / 60, secs % 60));
            long secs_mins = secs/60;
            binding.progress.setProgress((int)secs_mins);
            binding.progressTextview.setText(secs_mins+"분/"+"100분");
        }
    }

    public void setItem(ArrayList<ParentUser> data){
        this.data = data;
        notifyDataSetChanged();
    }
}
