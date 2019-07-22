package com.desire.powwow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ForumAdapter extends ArrayAdapter<ForumQuestion> {
    Context mContext;

    public ForumAdapter(Context context, List<ForumQuestion> questions) {
        super(context, 0, questions);
        this.mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.forum_list_item, parent, false);
        }

        ForumQuestion question = getItem(position);
        TextView titleView = listItemView.findViewById(R.id.forum_title);
        assert question != null;
        titleView.setText(question.getTitle());
        TextView titleView2 = listItemView.findViewById(R.id.forum_description);
        titleView2.setText(question.getDescription());
        TextView txtUserName = listItemView.findViewById(R.id.txtusername);
        txtUserName.setText(question.getUserName());
        ImageView imageView = listItemView.findViewById(R.id.icon_alphabet);
        imageView.setImageDrawable(question.getTextDrawable());

        return listItemView;
    }
}
