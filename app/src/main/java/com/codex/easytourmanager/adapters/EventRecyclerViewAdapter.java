package com.codex.easytourmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.codex.easytourmanager.AddEventActivity;
import com.codex.easytourmanager.EventActivity;
import com.codex.easytourmanager.R;
import com.codex.easytourmanager.ShareEventActivity;
import com.codex.easytourmanager.event_class.EventInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.MyViewHolder> {

    private List<EventInfo> eventList;
    private Context context;

    public EventRecyclerViewAdapter(Context context, List<EventInfo> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_view_model, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        String budget = eventList.get(position).getEventBudget() + " " + "TK";
        holder.budget.setText(budget);
        holder.from.setText(eventList.get(position).getFromDate());
        holder.to.setText(eventList.get(position).getToDate());
        holder.destination.setText(eventList.get(position).getEventDestination());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final EventInfo eventInfo = eventList.get(position);

                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.inflate(R.menu.event_up_del_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.update_event:
                                Intent intent = new Intent(context, AddEventActivity.class);
                                intent.putExtra("updateEvent", eventInfo);
                                context.startActivity(intent);
                                break;
                            case R.id.delete_event:
                                //Do database delet code
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference();
                                rootReference.child("user").child(user.getUid()).child("event").child(eventInfo.getEventKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Event Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();

                                    }
                                });
                            case R.id.share_event:
                                Intent gotoShare = new Intent(context, ShareEventActivity.class);
                                gotoShare.putExtra("eventData", eventList.get(position).getEventKey());
                                context.startActivity(gotoShare);
                                break;


                        }
                        return false;
                    }
                });

                popupMenu.show();
                return false;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("event", eventList.get(position));
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView destination, from, to, budget;

        public MyViewHolder(View itemView) {
            super(itemView);

            destination = itemView.findViewById(R.id.event_destination_view);
            from = itemView.findViewById(R.id.from_date_view);
            to = itemView.findViewById(R.id.to_date_view);
            budget = itemView.findViewById(R.id.budget_view);

        }

    }
}
