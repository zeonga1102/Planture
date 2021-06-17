package org.tensorflow.lite.examples.classification.view.myplants;

import android.app.Activity;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyPlantsRecyclerViewDecoration extends RecyclerView.ItemDecoration {
    private int spanCount, spacing, outerMargin;

    public MyPlantsRecyclerViewDecoration(Activity activity) {
        spanCount = 2;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int maxCount = parent.getAdapter().getItemCount();
        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        int row = position / spanCount;
        int lastRow = (maxCount - 1) / spanCount;

        outRect.left = column * spacing / spanCount;
        outRect.right = spacing - (column + 1) * spacing / spanCount;
        outRect.top = spacing * 2;

        if (row == lastRow) {
            outRect.bottom = outerMargin;   //left, right, top, bottom 값으로 margin 설정
        }
    }
}
