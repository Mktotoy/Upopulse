package com.upo10.miage.upopulse.upoevent;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.upo10.miage.upopulse.R;

import java.util.ArrayList;

public class MyAdapterEvent extends RecyclerView.Adapter<MyAdapterEvent.ViewHolder> {
    private ArrayList<Event> itemsData;
    private static Event singleEvent;

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView firstLine;
        public TextView secondLine;
        public TextView hiddenId;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            firstLine = (TextView) itemLayoutView.findViewById(R.id.firstLine);
            secondLine = (TextView) itemLayoutView.findViewById(R.id.secondLine);
            hiddenId = (TextView) itemLayoutView.findViewById(R.id.hiddenIdEvent);
        }
    }

    public MyAdapterEvent(ArrayList<Event> listeEvent) {
        this.itemsData = listeEvent;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterEvent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_event, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.firstLine.setText(itemsData.get(position).getNom());
        viewHolder.firstLine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //get id event selected
                //search object corresponding to this id
                //Send the object to the new activity
                int idEvent = Integer.parseInt((String) viewHolder.hiddenId.getText());

                for (Event e : itemsData) {
                    if (e.getId() == idEvent) {
                        Intent in = new Intent(v.getContext(), SingleEventActivity.class);
                        singleEvent = e;
                        v.getContext().startActivity(in);
                    }
                }
            }
        });
        viewHolder.secondLine.setText(itemsData.get(position).getEmplacement().getNomRoom() + " - "+itemsData.get(position).getDateDebutFormated());
        viewHolder.hiddenId.setText(String.valueOf(itemsData.get(position).getId()));
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public static Event getSingleEvent(){
        return singleEvent;
    }
}