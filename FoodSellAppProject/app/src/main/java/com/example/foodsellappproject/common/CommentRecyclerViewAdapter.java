package com.example.foodsellappproject.common;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsellappproject.R;
import com.example.foodsellappproject.entity.Comment;
import com.example.foodsellappproject.entity.User;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CommentRecyclerViewAdapter extends RecyclerView.Adapter<CommentRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Comment> getCommentsByFoodId;
    private ArrayList<User> getUsers;

    public CommentRecyclerViewAdapter(ArrayList<Comment> getCommentsByFoodId, ArrayList<User> getUsers) {
        this.getCommentsByFoodId = getCommentsByFoodId;
        this.getUsers = getUsers;
    }

    @NonNull
    @Override
    public CommentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_custom,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerViewAdapter.ViewHolder holder, int position) {
        //handle ivAvt,tvName
        for (int i = 0; i <getUsers.size() ; i++) {
            if(getCommentsByFoodId.get(position).getUserId().equalsIgnoreCase(getUsers.get(i).getUsername())){
                holder.ivAvt.setImageResource(getUsers.get(i).getAvtImageId());
                holder.tvName.setText(getUsers.get(i).getFullName());
                break;
            }
        }
        //handle
        holder.tvComment.setText(getCommentsByFoodId.get(position).getCommentContent());
        holder.tvCommentDate.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(getCommentsByFoodId.get(position).getCommentTime()));

    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public int getItemCount() {
        return getCommentsByFoodId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAvt;
        private TextView tvName,tvComment,tvCommentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvt = itemView.findViewById(R.id.ivAvt);
            tvName = itemView.findViewById(R.id.tvName);
            tvComment = itemView.findViewById(R.id.tvComment);
            tvCommentDate = itemView.findViewById(R.id.tvCommentDate);
        }
    }
}
