package com.eui.ebalance.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.TabWidget;
import android.widget.TextView;

import com.eui.ebalance.R;


public class LeTabWidget extends TabWidget {

    private Drawable mTopStrip;
    private int mTopStripHeight;

    private int mSelectedTab = -1;
    private OnTabSelectionChanged mSelectionChangedListener;

    public LeTabWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public LeTabWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LeTabWidget(Context context) {
        super(context);
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int left = getLeft();
        int top = 0;
        int right = getRight();
        int bottom = mTopStripHeight;

        final Drawable topStrip = mTopStrip;
        if (topStrip != null && mTopStripHeight != 0) {
            topStrip.setBounds(left, top, right, bottom);
            topStrip.draw(canvas);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null)
            return;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LeTabWidget);

        int topStripRes = ta.getResourceId(R.styleable.LeTabWidget_topStrip, -1);
        if (topStripRes != -1) {
            mTopStrip = getContext().getResources().getDrawable(topStripRes);
        } else { // set default strip
            // mTopStrip =
            // getContext().getResources().getDrawable(R.color.le_tab_indicator_top_strip_color);
        }

        int topStripHeight = (int) ta.getDimension(R.styleable.LeTabWidget_topStripHeight, 0);
        if (topStripHeight != 0 && mTopStrip != null) {
            mTopStripHeight = topStripHeight;
        } else {
            // mTopStripHeight =
            // getResources().getDimensionPixelSize(R.dimen.le_tab_indicator_top_strip_height);
        }

        ta.recycle();
    }

    public void setTopStripDrawable(int resId) {
        setTopStripDrawable(getContext().getResources().getDrawable(resId));
    }

    public void setTopStripDrawable(Drawable drawable) {
        mTopStrip = drawable;
        // requestLayout();
        invalidate();
    }

    public void setTopStripHeight(int height) {
        mTopStripHeight = height;
        invalidate();
    }

    public ImageView getTabIcon(int pos) {
        View tab = getTabView(pos);
        if (tab == null)
            return null;

        ImageView img = (ImageView) tab.findViewById(R.id.icon);
        return img;
    }

    public TextView getTabTitle(int pos) {
        View tab = getTabView(pos);
        if (tab == null)
            return null;

        TextView tv = (TextView) tab.findViewById(R.id.title);
        return tv;
    }

    public void setTitleTextColor(int resId) {
        for (int i = 0; i < getChildCount(); i++) {
            TextView tv = getTabTitle(i);
            if (tv != null)
                tv.setTextColor(getResources().getColorStateList(resId));
        }
    }

    public void setTitleTextColor(int unselected_color, int selected_color) {
        final int[][] states = new int[][] {
                {
                    android.R.attr.state_selected
                },
                {}
        };
        final int[] colors = new int[] {
                selected_color, unselected_color
        };
        ColorStateList list = new ColorStateList(states, colors);
        for (int i = 0; i < getChildCount(); i++) {
            TextView tv = getTabTitle(i);
            if (tv != null)
                tv.setTextColor(list);
        }
    }

    /*
     * return a RelativeLayout object if mode is MODE_ICON_ONLY, RelativeLayout
     * object contains a icon if mode is MODE_ICON_TEXT, RelativeLayout object
     * contains a icon and a text view
     */
    public View getTabView(int pos) {

        if (pos < 0 || pos >= getChildCount())
            return null;

        return this.getChildAt(pos);
    }

    // It's not override to TabWidget.OnTabSelectionChanged, and it is a new
    // interface
    public static interface OnTabSelectionChanged {
        void onTabSelectionChanged(int tabIndex, boolean clicked);
    }

    public void setTabSelectionListener(OnTabSelectionChanged listener) {
        mSelectionChangedListener = listener;
    }

    @Override
    public void setCurrentTab(int index) {
        if (index < 0 || index >= getTabCount() || index == mSelectedTab) {
            return;
        }

        mSelectedTab = index;
        super.setCurrentTab(index);
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
        mSelectedTab = -1;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus && getTabCount() > 0) {
            getChildTabViewAt(mSelectedTab).requestFocus();
            return;
        }

        if (hasFocus) {
            int i = 0;
            int numTabs = getTabCount();
            while (i < numTabs) {
                if (getChildTabViewAt(i) == v) {
                    setCurrentTab(i);
                    mSelectionChangedListener.onTabSelectionChanged(i, false);
                    if (isShown()) {
                        // a tab is focused so send an event to announce the tab
                        // widget state
                        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED);
                    }
                    break;
                }
                i++;
            }
        }
    }

    // registered with each tab indicator so we can notify tab host
    private class TabClickListener implements OnClickListener {

        private final int mTabIndex;

        private TabClickListener(int tabIndex) {
            mTabIndex = tabIndex;
        }

        public void onClick(View v) {
            mSelectionChangedListener.onTabSelectionChanged(mTabIndex, true);
        }
    }

    @Override
    public void addView(View child) {
        if (child.getLayoutParams() == null) {
            final LayoutParams lp = new LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            lp.setMargins(0, 0, 0, 0);
            child.setLayoutParams(lp);
        }

        // Ensure you can navigate to the tab with the keyboard, and you can
        // touch it
        child.setFocusable(true);
        child.setClickable(true);

        super.addView(child);

        // TODO: detect this via geometry with a tabwidget listener rather
        // than potentially interfere with the view's listener
        child.setOnClickListener(new TabClickListener(getTabCount() - 1));
        child.setOnFocusChangeListener(this);
    }

}
