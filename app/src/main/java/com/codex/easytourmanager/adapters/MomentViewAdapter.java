package com.codex.easytourmanager.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.codex.easytourmanager.AddMomentActivity;
import com.codex.easytourmanager.R;
import com.codex.easytourmanager.event_class.EventInfo;
import com.codex.easytourmanager.event_class.Moments;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class MomentViewAdapter extends RecyclerView.Adapter<MomentViewAdapter.MomentViewHolder>{

    private List<Moments> momentsList;
    private Activity context;

    public MomentViewAdapter(List<Moments> momentsList, Activity context) {
        this.momentsList = momentsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MomentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.moment_view_model,parent,false);
        return new MomentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MomentViewHolder holder, final int position) {

        holder.captionView.setText(momentsList.get(position).getMomentCaption());
        holder.momentImageView.setImageBitmap(momentsList.get(position).retrieveMomentImage());
        holder.momentDateView.setText(momentsList.get(position).getMomentdateTime());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final Moments moment = momentsList.get(position);

                PopupMenu popupMenu = new PopupMenu(context,holder.itemView);
                popupMenu.inflate(R.menu.event_up_del_menu);
                Menu popUp = popupMenu.getMenu();
                MenuItem item = popUp.findItem(R.id.share_event);
                item.setVisible(false);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        EventInfo eventInfo = (EventInfo)context.getIntent().getSerializableExtra("expensefromevent");

                        switch (item.getItemId()){

                            case R.id.delete_event :
                                //DO firebase code
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference rootReference= FirebaseDatabase.getInstance().getReference();
                                rootReference.child("user").child(user.getUid()).child("moment").child(moment.getMomentKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Moment Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                break;
                            case R.id.update_event :


                                Intent intent = new Intent(context, AddMomentActivity.class);
                                intent.putExtra("updatemoment",moment);
                                intent.putExtra("fromadapter",eventInfo);
                                //intent.putExtra("updateMomentState","update");
                                context.startActivity(intent);
                                break;

                        }

                        return false;
                    }
                });

                popupMenu.show();

                holder.momentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        fullScreen();
                    }
                });

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return momentsList.size();
    }

    public class MomentViewHolder extends RecyclerView.ViewHolder{

        TextView captionView,momentDateView;
        ImageView momentImageView;

        public MomentViewHolder(View itemView) {
            super(itemView);
            momentImageView = itemView.findViewById(R.id.moment_picture_view);
            momentDateView = itemView.findViewById(R.id.moment_date_view);
            captionView = itemView.findViewById(R.id.moment_caption_view);

        }
    }

    public void fullScreen() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = context.getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.i(TAG, "Turning immersive mode mode on.");
        }

        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        context.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }

}


