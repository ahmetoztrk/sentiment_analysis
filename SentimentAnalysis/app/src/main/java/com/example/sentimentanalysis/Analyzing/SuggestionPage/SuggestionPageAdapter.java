package com.example.sentimentanalysis.Analyzing.SuggestionPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.sentimentanalysis.Analyzing.FilterPage.FilterPageAdapter;
import com.example.sentimentanalysis.R;

import java.util.ArrayList;

public class SuggestionPageAdapter extends PagerAdapter {

    ArrayList<SuggestionPageModel> modelArrayList;

    Context context;

    public SuggestionPageAdapter(Context context, ArrayList<SuggestionPageModel> modelArrayList) {
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

        SuggestionPageModel model = modelArrayList.get(position);

        final String title = model.getTitle();
        final String description = model.getDescription();
        int image = model.getImage();

        bannerIv.setImageResource(image);
        titleTv.setText(title);
        descriptionTv.setText(description);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0; i< FilterPageAdapter.S_ADAPTER_SIZE; i++) {
                    if (position == i) {
                        Toast.makeText(context, "Position " + i, Toast.LENGTH_SHORT).show();
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