package com.desire.powwow.News;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.desire.powwow.R;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    private String TAG = NewsAdapter.class.getName();

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @SuppressLint("SetTextI18n")
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_item, parent, false);
        }

        News news = getItem(position);
        TextView titleView = listItemView.findViewById(R.id.news_title);
        assert news != null;
        titleView.setText(news.getTitle());
        TextView sectionView = listItemView.findViewById(R.id.news_section);
        sectionView.setText("Category: "+ news.getSection());

        Log.d(TAG, "getView: ");
        TextView authorView = listItemView.findViewById(R.id.author);
//        authorView.setText(R.string.news_contributors + news.getAuthor());


        return listItemView;
    }
}
