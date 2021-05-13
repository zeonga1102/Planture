package org.tensorflow.lite.examples.classification.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class ViewpagerAdapter extends PagerAdapter {
    private Context ctx;
    private List<String> imgUrls;

    public ViewpagerAdapter(Context ctx, List<String> imgUrls) {
        this.ctx = ctx;
        this.imgUrls = imgUrls;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        ImageView imageView = new ImageView(ctx);
        Picasso.get()
                .load(imgUrls.get(position))
                .resize(400, 400)
                //.transform(PicassoTransformations.resizeTransformation)
                .centerCrop()
                .into(imageView);
        collection.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public float getPageWidth(int position) {
        return (0.9f);
    }
}
