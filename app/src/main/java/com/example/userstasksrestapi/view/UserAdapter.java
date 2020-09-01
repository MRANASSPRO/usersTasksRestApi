package com.example.userstasksrestapi.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.userstasksrestapi.R;
import com.example.userstasksrestapi.UserDetails;
import com.example.userstasksrestapi.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    private List<User> userList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, username, email;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            username = (TextView) view.findViewById(R.id.username);
            email = (TextView) view.findViewById(R.id.email);
        }
    }

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.userlist_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final User user = userList.get(position);

        holder.name.setText(user.getName());
        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = holder.itemView.getContext();
                //pass tasks data of the user into intent (next activity)
                //TODO
                Intent intent = new Intent(context, UserDetails.class);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
