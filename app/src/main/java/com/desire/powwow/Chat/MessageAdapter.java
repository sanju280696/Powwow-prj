package com.desire.powwow.Chat;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.desire.powwow.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

    List<MessageDetails> messages = new ArrayList<MessageDetails>();
    Context context;
    String TAG = MessageAdapter.class.getName();

    public MessageAdapter(Context context, List<MessageDetails> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: "+messages.size());
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        MessageDetails messageDetails = messages.get(i);

        Log.d(TAG, "getView: "+messageDetails.getMsg());
        if (messageDetails.getId() == 1) {
            view = messageInflater.inflate(R.layout.my_message, null);
            holder.messageBody = (TextView) view.findViewById(R.id.message_body);
            view.setTag(holder);
            holder.messageBody.setText(messageDetails.getMsg());
        } else {
            view = messageInflater.inflate(R.layout.their_message, null);
            holder.messageBody = (TextView) view.findViewById(R.id.message_body);
            view.setTag(holder);
            holder.messageBody.setText(messageDetails.getMsg());
        }
        return view;
    }

    class MessageViewHolder {
        public TextView messageBody;
    }
}
