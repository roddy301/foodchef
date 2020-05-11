package com.justifiers.foodchef.Instructions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.justifiers.foodchef.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class InstructionsViewPagerAdapter extends PagerAdapter {

    Context mContext;
    List<InstructionItem> mSteps;

    public InstructionsViewPagerAdapter(Context context, List<InstructionItem> steps) {
        mContext = context;
        mSteps = steps;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutStep = inflater.inflate(R.layout.layout_step, null);

        InstructionItem step = mSteps.get(position);

        ImageView imgStep = layoutStep.findViewById(R.id.img_step);
        Picasso.get().load(step.getImageURL())
                .error(R.drawable.ic_asset_image_placeholder)
                .placeholder(R.drawable.ic_asset_image_placeholder)
                .into(imgStep);


        TextView txtInstruction = layoutStep.findViewById(R.id.txt_step_description);
        txtInstruction.setText(step.getDescription());

        container.addView(layoutStep);
        return layoutStep;
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
