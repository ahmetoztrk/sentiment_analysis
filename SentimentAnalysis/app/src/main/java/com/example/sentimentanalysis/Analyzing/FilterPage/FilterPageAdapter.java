package com.example.sentimentanalysis.Analyzing.FilterPage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.sentimentanalysis.Analyzing.SuggestionPage.SuggestionPageActivity;
import com.example.sentimentanalysis.R;

import java.util.ArrayList;

public class FilterPageAdapter extends PagerAdapter {

    ArrayList<FilterPageModel> modelArrayList;

    Context context;

    public FilterPageAdapter(Context context, ArrayList<FilterPageModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, container, false);

        ImageView bannerIv = view.findViewById(R.id.bannerIv);
        TextView titleTv = view.findViewById(R.id.titleTv);
        TextView descriptionTv = view.findViewById(R.id.descriptionTv);

        FilterPageModel model = modelArrayList.get(position);

        final String title = model.getTitle();
        final String description = model.getDescription();
        int image = model.getImage();

        bannerIv.setImageResource(image);
        titleTv.setText(title);
        descriptionTv.setText(description);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0; i<5; i++){
                    if(position == i){
                        Intent intent = new Intent(context, SuggestionPageActivity.class);
                        intent.putExtra("position", position);

                        context.startActivity(intent);
                    }
                }
            }
        });

        container.addView(view, position);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}