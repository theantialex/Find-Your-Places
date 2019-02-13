package com.example.alexandra.expendablechecklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter{

    private final View mWindow;
    private Context mContext;
    private String snippet2;

    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        snippet2 = "";
        String snippet = "";
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        if (!title.equals("")){
            tvTitle.setText(title);
        }
        snippet = marker.getSnippet();
        RatingBar rating = view.findViewById(R.id.rating);
        rating.setStepSize((float) 0.1);
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);
        if (snippet.contains("Рейтинг")) {
            rating.setVisibility(View.VISIBLE);
            String stars = snippet.substring(snippet.indexOf("Рейтинг: "), snippet.indexOf("Рейтинг: ") + 12);
            Float r = Float.valueOf(snippet.substring(snippet.indexOf("Рейтинг: ")+ 9 , snippet.indexOf("Рейтинг: ") + 12 ));
            rating.setRating(r);
            snippet2 = snippet.substring(0,snippet.indexOf(stars)) + snippet.substring(snippet.indexOf(stars) + stars.length());
            tvSnippet.setText(snippet2);
        }
        else{
            rating.setVisibility(View.GONE);
            if (!snippet.equals("")){
                tvSnippet.setText(snippet);
            } else{
                tvSnippet.setText("");
            }
        }




    }


    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowText(marker, mWindow);
        return mWindow;
    }
}
