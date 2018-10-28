package com.qc.language.common.view.panterdialog.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.qc.language.R;


public class SingleChoiceAdapter extends RecyclerView.Adapter<SingleChoiceAdapter.ViewHolder> {

    public final String[] list;
    public int lastCheckedPosition = -1;

    public SingleChoiceAdapter(String[] list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_choice_item,
                parent,
                false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.choice.setText(list[position]);
        holder.radioButton.setChecked(position == lastCheckedPosition);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView choice;
        RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            choice = (TextView) itemView.findViewById(R.id.choice);
            radioButton = (RadioButton) itemView.findViewById(R.id.radio_button);

            // Handle click events here
            View.OnClickListener selectionListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastCheckedPosition = getAdapterPosition();
                    radioButton.setChecked(true);
                    notifyItemRangeChanged(0, list.length);
                }
            };

            itemView.setOnClickListener(selectionListener);
            radioButton.setOnClickListener(selectionListener);
        }
    }
}