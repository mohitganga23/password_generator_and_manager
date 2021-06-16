package com.example.passwordgeneratormanager;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    public Context context;
    public List<Password> passwordList;
    public DatabaseHandler db;

    public PasswordAdapter(List<Password> passwordList) {
        this.passwordList = passwordList;
    }

    @NonNull
    @Override
    public PasswordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password_db_layout, parent, false);
        context = parent.getContext();
        db = new DatabaseHandler(context);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordAdapter.ViewHolder holder, int position) {

        holder.setIsRecyclable(false);

        String title = passwordList.get(position).getTitle();
        String password = passwordList.get(position).getPassword();

        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        holder.setTitleAndPassword(title, password);

        holder.copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("Generated Password", password);
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(context, "PASSWORD COPIED!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete this stored password?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deletePassword(passwordList.get(position));
                        passwordList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, passwordList.size());
                        holder.itemView.setVisibility(View.GONE);
                        Toast.makeText(context, "DELETED!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        if (passwordList.size() != 0) {
            return passwordList.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private final ImageButton copy_btn;
        private final ImageButton delete_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            copy_btn = mView.findViewById(R.id.copy_btn);
            delete_btn = mView.findViewById(R.id.delete_btn);
        }

        public void setTitleAndPassword(String title, String password) {
            TextView saved_title = mView.findViewById(R.id.saved_title);
            TextView saved_password = mView.findViewById(R.id.saved_password);

            saved_title.setText(title);
            saved_password.setText(password);
        }
    }
}
