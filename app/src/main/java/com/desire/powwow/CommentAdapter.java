package com.desire.powwow;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.desire.powwow.Chat.ChatActivity;
import com.desire.powwow.Chat.UserDetails;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    List<Comment> commentList;

    Context mContext;

    public CommentAdapter(Context mContext, List<Comment> commentList) {
        Log.d("CommentAdapter1 ", "" + commentList);
        this.mContext = mContext;
        this.commentList = commentList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtComment, txteid;
        LinearLayout chatHeader;

        public MyViewHolder(View view) {
            super(view);
            txtComment = view.findViewById(R.id.comment_body);
            txteid = view.findViewById(R.id.comment_username);
            chatHeader = view.findViewById(R.id.commentview);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final Comment comment = commentList.get(i);
        Log.d("CommentAdapter ", "" + comment);
        myViewHolder.txtComment.setText(comment.getComment());
        myViewHolder.txteid.setText(comment.getUsername());
        myViewHolder.chatHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String receiverEmail = comment.getUsername();
                String[] receiver = receiverEmail.split("@");
                UserDetails.chatWith = receiver[0];
                if (!UserDetails.username.equalsIgnoreCase(UserDetails.chatWith)) {
                    Intent startChatActivity = new Intent(mContext, ChatActivity.class);

                    mContext.startActivity(startChatActivity);
                } else {
                    //Cannot chat with his/her own self
                    Utils.showDialog(mContext, "Hello " + UserDetails.username + ",",
                            "It seems like you have selected your self for personal one-to-one chat.Pick someone else to initiate chat.",
                            "Got it!", null,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                }
                //Toast.makeText(mContext, "Click" + receiver[0], Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

}

//Dialog Code

/*
AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(posBtnMsg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setNegativeButton(negBtnMsg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
*/