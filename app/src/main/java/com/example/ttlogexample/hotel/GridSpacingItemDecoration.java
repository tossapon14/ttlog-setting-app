package com.example.ttlogexample.hotel;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

    private final int spacing; // Space size in pixels
    private final int spanCount; // Number of columns
    private final boolean includeEdge; // Whether to include spacing at the edges

    public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = spacing - column * spacing / spanCount; // space at the left edge
            outRect.right = (column + 1) * spacing / spanCount; // space at the right edge

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // space between items
            outRect.right = spacing - (column + 1) * spacing / spanCount; // space between items

            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}
