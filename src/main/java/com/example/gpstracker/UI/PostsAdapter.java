package com.example.gpstracker.UI;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.gpstracker.R;
import com.example.gpstracker.UI.model.Post;

import java.util.ArrayList;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder>{
    private List<Post> PostList = new ArrayList<>();
    @NonNull
    @Override
    public PostsAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.PostViewHolder holder, int position) {
        holder.Email.setText(PostList.get(position).getEmail());
        holder.point.setText(PostList.get(position).getLocation().getType());
        holder.line1.setText(String.valueOf( PostList.get(position).getLocation().getCoordinates()[0]));
        holder.line2.setText(String.valueOf( PostList.get(position).getLocation().getCoordinates()[1]));
        // holder.line3.setText(String.valueOf(PostList.get(position).getCalculated().getCalculated()));
    }
    public void setList(List<Post> PostList) {
        this.PostList = PostList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (PostList==null)
            return 0;
        Log.d("lll", String.valueOf(PostList.size()));
        return PostList.size();

    }
    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView Email,point,line1,line2,line3;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            Email=itemView.findViewById(R.id.Email);
            point=itemView.findViewById(R.id.point);
            line1=itemView.findViewById(R.id.line1);
            line2=itemView.findViewById(R.id.line2);
            // line3=itemView.findViewById(R.id.line3);

        }
    }
}
