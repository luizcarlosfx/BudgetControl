
package com.imersaovisual.budgetcontrol.ui.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/*public class MyLayoutManager extends RecyclerView.LayoutManager {

        private static final String TAG = "MyLayoutManager";

        private int mFirstPosition;

        private final int mScrollDistance;

        public MyLayoutManager(Context c) {
            final DisplayMetrics dm = c.getResources().getDisplayMetrics();
            mScrollDistance = (int) (80 * dm.density + 0.5f);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            final int parentBottom = getHeight() - getPaddingBottom();
            final View oldTopView = getChildCount() > 0 ? getChildAt(0) : null;
            int oldTop = getPaddingTop();
            if (oldTopView != null) {
                oldTop = oldTopView.getTop();
            }

            detachAndScrapAttachedViews(recycler);

            int top = oldTop;
            int bottom;
            final int left = getPaddingLeft();
            final int right = getWidth() - getPaddingRight();

            final int count = state.getItemCount();
            for (int i = 0; mFirstPosition + i < count && top < parentBottom; i++, top = bottom) {
                View v = recycler.getViewForPosition(mFirstPosition + i);
                addView(v, i);
                measureChildWithMargins(v, 0, 0);
                bottom = top + getDecoratedMeasuredHeight(v);
                layoutDecorated(v, left, top, right, bottom);
            }
        }

        @Override
        public RecyclerView.LayoutParams generateDefaultLayoutParams() {
            return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        @Override
        public boolean canScrollVertically() {
            return true;
        }

        @Override
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                      RecyclerView.State state) {
            if (getChildCount() == 0) {
                return 0;
            }

            int scrolled = 0;
            final int left = getPaddingLeft();
            final int right = getWidth() - getPaddingRight();
            if (dy < 0) {
                while (scrolled > dy) {
                    final View topView = getChildAt(0);
                    final int hangingTop = Math.max(-getDecoratedTop(topView), 0);
                    final int scrollBy = Math.min(scrolled - dy, hangingTop);
                    scrolled -= scrollBy;
                    offsetChildrenVertical(scrollBy);
                    if (mFirstPosition > 0 && scrolled > dy) {
                        mFirstPosition--;
                        View v = recycler.getViewForPosition(mFirstPosition);
                        addView(v, 0);
                        measureChildWithMargins(v, 0, 0);
                        final int bottom = getDecoratedTop(topView);
                        final int top = bottom - getDecoratedMeasuredHeight(v);
                        layoutDecorated(v, left, top, right, bottom);
                    } else {
                        break;
                    }
                }
            } else if (dy > 0) {
                final int parentHeight = getHeight();
                while (scrolled < dy) {
                    final View bottomView = getChildAt(getChildCount() - 1);
                    final int hangingBottom =
                            Math.max(getDecoratedBottom(bottomView) - parentHeight, 0);
                    final int scrollBy = -Math.min(dy - scrolled, hangingBottom);
                    scrolled -= scrollBy;
                    offsetChildrenVertical(scrollBy);
                    if (scrolled < dy && state.getItemCount() > mFirstPosition + getChildCount()) {
                        View v = recycler.getViewForPosition(mFirstPosition + getChildCount());
                        final int top = getDecoratedBottom(getChildAt(getChildCount() - 1));
                        addView(v);
                        measureChildWithMargins(v, 0, 0);
                        final int bottom = top + getDecoratedMeasuredHeight(v);
                        layoutDecorated(v, left, top, right, bottom);
                    } else {
                        break;
                    }
                }
            }
            recycleViewsOutOfBounds(recycler);
            return scrolled;
        }

        @Override
        public View onFocusSearchFailed(View focused, int direction,
                                        RecyclerView.Recycler recycler, RecyclerView.State state) {
            final int oldCount = getChildCount();

            if (oldCount == 0) {
                return null;
            }

            final int left = getPaddingLeft();
            final int right = getWidth() - getPaddingRight();

            View toFocus = null;
            int newViewsHeight = 0;
            if (direction == View.FOCUS_UP || direction == View.FOCUS_BACKWARD) {
                while (mFirstPosition > 0 && newViewsHeight < mScrollDistance) {
                    mFirstPosition--;
                    View v = recycler.getViewForPosition(mFirstPosition);
                    final int bottom = getDecoratedTop(getChildAt(0));
                    addView(v, 0);
                    measureChildWithMargins(v, 0, 0);
                    final int top = bottom - getDecoratedMeasuredHeight(v);
                    layoutDecorated(v, left, top, right, bottom);
                    if (v.isFocusable()) {
                        toFocus = v;
                        break;
                    }
                }
            }
            if (direction == View.FOCUS_DOWN || direction == View.FOCUS_FORWARD) {
                while (mFirstPosition + getChildCount() < state.getItemCount() &&
                        newViewsHeight < mScrollDistance) {
                    View v = recycler.getViewForPosition(mFirstPosition + getChildCount());
                    final int top = getDecoratedBottom(getChildAt(getChildCount() - 1));
                    addView(v);
                    measureChildWithMargins(v, 0, 0);
                    final int bottom = top + getDecoratedMeasuredHeight(v);
                    layoutDecorated(v, left, top, right, bottom);
                    if (v.isFocusable()) {
                        toFocus = v;
                        break;
                    }
                }
            }

            return toFocus;
        }

        public void recycleViewsOutOfBounds(RecyclerView.Recycler recycler) {
            final int childCount = getChildCount();
            final int parentWidth = getWidth();
            final int parentHeight = getHeight();
            boolean foundFirst = false;
            int first = 0;
            int last = 0;
            for (int i = 0; i < childCount; i++) {
                final View v = getChildAt(i);
                if (v.hasFocus() || (getDecoratedRight(v) >= 0 &&
                        getDecoratedLeft(v) <= parentWidth &&
                        getDecoratedBottom(v) >= 0 &&
                        getDecoratedTop(v) <= parentHeight)) {
                    if (!foundFirst) {
                        first = i;
                        foundFirst = true;
                    }
                    last = i;
                }
            }
            for (int i = childCount - 1; i > last; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
            for (int i = first - 1; i >= 0; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
            if (getChildCount() == 0) {
                mFirstPosition = 0;
            } else {
                mFirstPosition += first;
            }
        }
    }*/
public class MyLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "MyLayoutManager";
    private int mFirstPosition;
    private final int mScrollDistance;

    public MyLayoutManager(Context c) {
        final DisplayMetrics dm = c.getResources().getDisplayMetrics();
        mScrollDistance = (int) (80 * dm.density + 0.5f);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        int parentBottom = getHeight() - getPaddingBottom();

        final View oldTopView = getChildCount() > 0 ? getChildAt(0) : null;
        int oldTop = getPaddingTop();
        if (oldTopView != null) {
            oldTop = Math.min(oldTopView.getTop(), oldTop);
        }

        // Note that we add everything to the scrap, but we do not clean it up;
        // that is handled by the RecyclerView after this method returns
        detachAndScrapAttachedViews(recycler);

        int top = oldTop;
        int bottom = top;
        final int left = getPaddingLeft();
        final int right = getWidth() - getPaddingRight();

        int count = state.getItemCount();
        for (int i = 0; mFirstPosition + i < count && top < parentBottom; i++, top = bottom) {
            View v = recycler.getViewForPosition(mFirstPosition + i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
            addView(v);
            measureChild(v, 0, 0);
            bottom = top + v.getMeasuredHeight();
            v.layout(left, top, right, bottom);
            if (params.isItemRemoved()) {
                parentBottom += v.getHeight();
            }
        }

        if (!state.isPreLayout()) {
            // Now that we've run a full layout, figure out which views were not used
            // (cached in previousViews). For each of these views, position it where
            // it would go, according to its position relative to the visible
            // positions in the list. This information will be used by RecyclerView to
            // record post-layout positions of these items for the purposes of animating them
            // out of view

            View lastVisibleView = getChildAt(getChildCount() - 1);
            if (lastVisibleView != null) {
                RecyclerView.LayoutParams lastParams =
                        (RecyclerView.LayoutParams) lastVisibleView.getLayoutParams();
                int lastPosition = lastParams.getViewPosition();
                final List<RecyclerView.ViewHolder> previousViews = recycler.getScrapList();
                count = previousViews.size();
                for (int i = 0; i < count; ++i) {
                    View view = previousViews.get(i).itemView;
                    RecyclerView.LayoutParams params =
                            (RecyclerView.LayoutParams) view.getLayoutParams();
                    if (params.isItemRemoved()) {
                        continue;
                    }
                    int position = params.getViewPosition();
                    int newTop;
                    if (position < mFirstPosition) {
                        newTop = view.getHeight() * (position - mFirstPosition);
                    } else {
                        newTop = lastVisibleView.getTop() + view.getHeight() *
                                (position - lastPosition);
                    }
                    view.offsetTopAndBottom(newTop - view.getTop());
                }
            }
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler,
                                  RecyclerView.State state) {
        if (getChildCount() == 0) {
            return 0;
        }

        int scrolled = 0;
        final int left = getPaddingLeft();
        final int right = getWidth() - getPaddingRight();
        if (dy < 0) {
            while (scrolled > dy) {
                final View topView = getChildAt(0);
                final int hangingTop = Math.max(-topView.getTop(), 0);
                final int scrollBy = Math.min(scrolled - dy, hangingTop);
                scrolled -= scrollBy;
                offsetChildrenVertical(scrollBy);
                if (mFirstPosition > 0 && scrolled > dy) {
                    mFirstPosition--;
                    View v = recycler.getViewForPosition(mFirstPosition);
                    addView(v, 0);
                    measureChild(v, 0, 0);
                    final int bottom = topView.getTop(); // TODO decorated top?
                    final int top = bottom - v.getMeasuredHeight();
                    v.layout(left, top, right, bottom);
                } else {
                    break;
                }
            }
        } else if (dy > 0) {
            final int parentHeight = getHeight();
            while (scrolled < dy) {
                final View bottomView = getChildAt(getChildCount() - 1);
                final int hangingBottom = Math.max(bottomView.getBottom() - parentHeight, 0);
                final int scrollBy = -Math.min(dy - scrolled, hangingBottom);
                scrolled -= scrollBy;
                offsetChildrenVertical(scrollBy);
                if (scrolled < dy && state.getItemCount() > mFirstPosition + getChildCount()) {
                    View v = recycler.getViewForPosition(mFirstPosition + getChildCount());
                    final int top = getChildAt(getChildCount() - 1).getBottom();
                    addView(v);
                    measureChild(v, 0, 0);
                    final int bottom = top + v.getMeasuredHeight();
                    v.layout(left, top, right, bottom);
                } else {
                    break;
                }
            }
        }
        recycleViewsOutOfBounds(recycler);
        return scrolled;
    }

    @Override
    public View onFocusSearchFailed(View focused, int direction,
                                    RecyclerView.Recycler recycler, RecyclerView.State state) {
        final int oldCount = getChildCount();

        if (oldCount == 0) {
            return null;
        }

        final int left = getPaddingLeft();
        final int right = getWidth() - getPaddingRight();

        View toFocus = null;
        int newViewsHeight = 0;
        if (direction == View.FOCUS_UP || direction == View.FOCUS_BACKWARD) {
            while (mFirstPosition > 0 && newViewsHeight < mScrollDistance) {
                mFirstPosition--;
                View v = recycler.getViewForPosition(mFirstPosition);
                final int bottom = getChildAt(0).getTop(); // TODO decorated top?
                addView(v, 0);
                measureChild(v, 0, 0);
                final int top = bottom - v.getMeasuredHeight();
                v.layout(left, top, right, bottom);
                if (v.isFocusable()) {
                    toFocus = v;
                    break;
                }
            }
        }
        if (direction == View.FOCUS_DOWN || direction == View.FOCUS_FORWARD) {
            while (mFirstPosition + getChildCount() < state.getItemCount() &&
                    newViewsHeight < mScrollDistance) {
                View v = recycler.getViewForPosition(mFirstPosition + getChildCount());
                final int top = getChildAt(getChildCount() - 1).getBottom();
                addView(v);
                measureChild(v, 0, 0);
                final int bottom = top + v.getMeasuredHeight();
                v.layout(left, top, right, bottom);
                if (v.isFocusable()) {
                    toFocus = v;
                    break;
                }
            }
        }

        return toFocus;
    }

    public void recycleViewsOutOfBounds(RecyclerView.Recycler recycler) {
        final int childCount = getChildCount();
        final int parentWidth = getWidth();
        final int parentHeight = getHeight();
        boolean foundFirst = false;
        int first = 0;
        int last = 0;
        for (int i = 0; i < childCount; i++) {
            final View v = getChildAt(i);
            if (v.hasFocus() || (v.getRight() >= 0 && v.getLeft() <= parentWidth &&
                    v.getBottom() >= 0 && v.getTop() <= parentHeight)) {
                if (!foundFirst) {
                    first = i;
                    foundFirst = true;
                }
                last = i;
            }
        }
        for (int i = childCount - 1; i > last; i--) {
            removeAndRecycleViewAt(i, recycler);
        }
        for (int i = first - 1; i >= 0; i--) {
            removeAndRecycleViewAt(i, recycler);
        }
        if (getChildCount() == 0) {
            mFirstPosition = 0;
        } else {
            mFirstPosition += first;
        }
    }

    @Override
    public void onItemsAdded(RecyclerView recyclerView, int positionStart, int itemCount) {
        if (positionStart < mFirstPosition) {
            mFirstPosition += itemCount;
        }
    }

    @Override
    public void onItemsRemoved(RecyclerView recyclerView, int positionStart, int itemCount) {
        if (positionStart < mFirstPosition) {
            mFirstPosition -= itemCount;
        }
    }
}
