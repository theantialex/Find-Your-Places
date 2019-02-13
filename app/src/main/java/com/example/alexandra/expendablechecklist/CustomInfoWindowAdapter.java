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

    public CustomInfoWindowAdapter(Context mContext) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowText(Marker marker, View view){
        String title = marker.getTitle();
        TextView tvTitle = (TextView) view.findViewById(R.id.title);
        if (!title.equals("")){
            tvTitle.setText(title);
        }
        String snippet = marker.getSnippet();
        RatingBar rating = view.findViewById(R.id.rating);
        rating.setStepSize((float) 0.1);
        if (snippet.contains("Рейтинг")) {
            String stars = snippet.substring(snippet.indexOf("Рейтинг: "), snippet.indexOf("Рейтинг: ") + 12);
            Float r = Float.valueOf(snippet.substring(snippet.indexOf("Рейтинг: ")+ 9 , snippet.indexOf("Рейтинг: ") + 12 ));
            rating.setRating(r);
            snippet = snippet.replaceAll(stars, "");
        }
        else{
            rating.setVisibility(View.GONE);
        }
        TextView tvSnippet = (TextView) view.findViewById(R.id.snippet);
        if (!snippet.equals("")){
            tvSnippet.setText(snippet);
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
