package com.codex.easytourmanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.codex.easytourmanager.AddExpenseActivity;
import com.codex.easytourmanager.R;
import com.codex.easytourmanager.event_class.Expense;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ExpenseViewAdapter extends RecyclerView.Adapter<ExpenseViewAdapter.ExpenseViewHolder>{


    private List<Expense> expenseList;
    private Context context;

    public ExpenseViewAdapter(List<Expense> expenseList, Context context) {
        this.expenseList = expenseList;
        this.context = context;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.expense_view_model,parent,false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        final Expense expense = expenseList.get(position);
        String amount = expense.getExpenseCost()+" "+"TK";
        holder.amountView.setText(amount);
        holder.dateView.setText(expense.getExpenseDateTime());
        holder.detailsView.setText(expense.getExpenseDetails());

        final PopupMenu popupMenu = new PopupMenu(context,holder.itemView);
        popupMenu.inflate(R.menu.event_up_del_menu);
        Menu popUp = popupMenu.getMenu();
        MenuItem item = popUp.findItem(R.id.share_event);
        item.setVisible(false);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()){

                            case R.id.update_event :
                                Intent intent = new Intent(context, AddExpenseActivity.class);
                                intent.putExtra("expense",expense);
                                context.startActivity(intent);
                                break;
                            case R.id.delete_event :

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                reference.child("user").child(user.getUid()).child("expense").child(expense.getExpenseKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Expense Entry Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        e.printStackTrace();
                                    }
                                });
                                break;

                        }

                        return false;
                    }
                });

                popupMenu.show();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{

        TextView detailsView,amountView,dateView;

        public ExpenseViewHolder(View itemView) {
            super(itemView);
            amountView = itemView.findViewById(R.id.expense_amount_view);
            detailsView = itemView.findViewById(R.id.expense_details_view);
            dateView = itemView.findViewById(R.id.expense_date_view);

        }
    }
}

