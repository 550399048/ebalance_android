package com.eui.ebalance.widget;

/*
 * The difference between LeFragmentTabHost and FragmentTabHost
 * 1. You can give a FrameLayout object to LeFragmentTabHost besides containerId when calling setup(...)
 * 2. provide an static createIndicatorView(...) to create an indicator view according to Leui's UI design
 * 3. provide an static setTabWidgetLayout(...) to adjust indicator views layout according to Leui's UI design
 * 
 * Example:
 * 
 *  protected void onCreate(Bundle savedInstanceState) {
 *      ...
 *      FrameLayout realTabContent = (FrameLayout)findViewById(R.id.realtabcontent);
 *       
 *      LeFragmentTabHost mTabHost = (LeFragmentTabHost) findViewById(android.R.id.tabhost);
 *      mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent, realTabContent);
 *       
 *      // removing divider
 *      mTabHost.getTabWidget().setDividerDrawable(null);
 *       
 *      View indicatorView;
 *
 *      // add an indicator view with icon and title
 *      indicatorView = mTabHost.createIndicatorView(R.drawable.calendar, getString(R.string.calendar)); 
 *      mTabHost.addTab(mTabHost.newTabSpec("calendar").setIndicator(indicatorView), BrowserFragment.class, null);
 *       
 *      // add an indicator view with icon only
 *      indicatorView = mTabHost.createIndicatorView(R.drawable.writer, null); 
 *      mTabHost.addTab(mTabHost.newTabSpec("writer").setIndicator(indicatorView), Fragment2.class, null);
 *       
 *      mTabHost.setTabWidgetLayout(this);
 *      ...
 *  }
 *  
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;


import com.eui.ebalance.R;

import java.util.ArrayList;
import java.util.List;


public class LeFragmentTabHost extends FrameLayout {
    private static final int DOUBLE_CLICK_TIME_MS = 350;
    
    private Context mContext;
    private FragmentManager mFragmentManager;
    private boolean mAttached; // whether this object is attached to window
    
    private LeTabWidget mTabWidget; // bottom widget
    
    // TabSpec used to include tag, tab, fragment
    private List<TabSpec> mTabSpecs = new ArrayList<TabSpec>(2); 

    protected int mCurrentTab = -1;  // current tab index which is selected
    private TabSpec mCurrentTabSpec; // current tab detail info which is selected

    private TabHost.OnTabChangeListener mOnTabChangeListener;
    
    // mRealTabContent: FrameLayout used to include Fragments, and it can be out of LeFragmentTabHost
    // mContainerId: id of mRealTabContent
    private FrameLayout mRealTabContent; 
    private int mContainerId;

    // true: old fragment is hidden and new fragment is attached or show
    // false: old fragment is detached and new fragment is attached
    private boolean mHideFragment = false;
    
    static class SavedState extends BaseSavedState {
        String curTab;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            curTab = in.readString();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(curTab);
        }

        @Override
        public String toString() {
            return "FragmentTabHost.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " curTab=" + curTab + "}";
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public LeFragmentTabHost(Context context) {
        // Note that we call through to the version that takes an AttributeSet,
        // because the simple Context construct can result in a broken object!
        super(context, null);
        initTabHost();
    }

    public LeFragmentTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTabHost();
    }
    
    private void initTabHost() {
        setFocusableInTouchMode(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        mCurrentTab = -1;
    }
    
    /**
     * Removes all tabs from the tab widget associated with this tab host.
     */
    public void clearAllTabs() {
        mTabWidget.removeAllViews();
        initTabHost();
        mTabSpecs.clear();
        requestLayout();
        invalidate();
    }
    
    /**
     * User can gives an tab content container which is not a child of
     * LeFragmentTabHost
     */
    public void setup(Context context, FragmentManager manager, int containerId,
            FrameLayout realContent) {
        if (realContent == null) {
            throw new IllegalStateException(
                    "realContent can't be null");
        } else {
            mTabWidget = (LeTabWidget) findViewById(android.R.id.tabs);
            if (mTabWidget == null) {
                throw new RuntimeException(
                        "Your TabHost must have a TabWidget whose id attribute is 'android.R.id.tabs'");
            }

            mTabWidget.setTabSelectionListener(new LeTabWidget.OnTabSelectionChanged() {
                public void onTabSelectionChanged(int tabIndex, boolean clicked) {
                    setCurrentTab(tabIndex, clicked);
                }
            });
            
            mContext = context;
            mFragmentManager = manager;
            mContainerId = containerId;

            mRealTabContent = realContent;
            if (mRealTabContent.getId() != containerId) {
                throw new IllegalStateException(
                        "mRealTabContent and containerId mismatch");
            }

            // We must have an ID to be able to save/restore our state. If
            // the owner hasn't set one at this point, we will set it ourself.
            if (getId() == View.NO_ID) {
                setId(android.R.id.tabhost);
            }
        }
    }


    public void setOnTabChangedListener(TabHost.OnTabChangeListener l) {
        mOnTabChangeListener = l;
    }
    
    public void setFragmentHiddenEnabled(boolean hideFragment) {
        mHideFragment = hideFragment;
    }
    
    /**
     * Get a new {@link TabSpec} associated with this tab host.
     * @param tag required tag of tab.
     */
    public TabSpec newTabSpec(String tag) {
        return new TabSpec(tag);
    }

    public void addTab(TabSpec tabSpec, Class<?> clss, Bundle args) {
        String tag = tabSpec.getTag();
        tabSpec.clss = clss;
        tabSpec.args = args;

        if (mAttached) {
            // If we are already attached to the window, then check to make
            // sure this tab's fragment is inactive if it exists. This shouldn't
            // normally happen.
            tabSpec.fragment = mFragmentManager.findFragmentByTag(tag);
            if (tabSpec.fragment != null && !tabSpec.fragment.isDetached()) {
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                if (!mHideFragment) {
                    ft.detach(tabSpec.fragment);
                } else {
                    ft.hide(tabSpec.fragment);
                }
                ft.commit();
            }
        }
        
        if (tabSpec.indicatorStrategy == null) {
            throw new IllegalArgumentException("you must specify a way to create the tab indicator.");
        }

        View tabIndicator = tabSpec.indicatorStrategy.createIndicatorView();

        // If this is a custom view, then do not draw the bottom strips for
        // the tab indicators.
        if (tabSpec.indicatorStrategy instanceof ViewIndicatorStrategy) {
            mTabWidget.setStripEnabled(false);
        }

        mTabWidget.addView(tabIndicator);
        mTabSpecs.add(tabSpec);

        if (mCurrentTab == -1) {
            setCurrentTab(0);
        }
    }
    
    public void replaceTab(int position, Class<?> clss, Bundle args) {
        if (clss == null || position < 0 || position >= mTabSpecs.size()) {
            return;
        }
        
        TabSpec tab = mTabSpecs.get(position);
        if (tab == null) {
            return;
        }
        
        if (tab.fragment != null) {
            // remove old fragment
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.detach(tab.fragment);
             
            // show new fragment if necessary
            if (getCurrentTab() == position) {
                tab.fragment = Fragment.instantiate(mContext,
                        clss.getName(), args);
                ft.add(mContainerId, tab.fragment, tab.tag);
            } else {
                tab.fragment = null;
            }
             
            ft.commit();
        } 
        
        // store new fragment parameters
        tab.clss = clss;
        tab.args = args;
    }
    
    public void replaceTab(String tag, Class<?> clss, Bundle args) {
        if (tag == null || tag.isEmpty() || clss == null) {
            return;
        }
        
        for (int i = 0; i < mTabSpecs.size(); i++) {
            if (mTabSpecs.get(i).getTag().equals(tag)) {
                replaceTab(i, clss, args);
                break;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        
        String currentTab = getCurrentTabTag();

        // Go through all tabs and make sure their fragments match
        // the correct state.
        FragmentTransaction ft = null;
        for (int i = 0; i < mTabSpecs.size(); i++) {
            TabSpec tab = mTabSpecs.get(i);
            tab.fragment = mFragmentManager.findFragmentByTag(tab.tag);
            if (tab.fragment != null && !tab.fragment.isDetached()) {
                if (tab.tag.equals(currentTab)) {
                    // The fragment for this tab is already there and
                    // active, and it is what we really want to have
                    // as the current tab. Nothing to do.
                    mCurrentTabSpec = tab;
                } else {
                    // This fragment was restored in the active state,
                    // but is not the current tab. Deactivate it.
                    if (ft == null) {
                        ft = mFragmentManager.beginTransaction();
                    }

                    if (!mHideFragment) {
                        ft.detach(tab.fragment);
                    } else {
                        ft.hide(tab.fragment);
                    }
                }
            }
        }

        // We are now ready to go. Make sure we are switched to the
        // correct tab.
        mAttached = true;
        ft = doTabChanged(currentTab, ft);
        if (ft != null) {
            //ft.commit();
            ft.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAttached = false;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.curTab = getCurrentTabTag();
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setCurrentTabByTag(ss.curTab);
    }
    
    public TabWidget getTabWidget() {
        return mTabWidget;
    }

    public LeTabWidget getLeTabWidget() {
        TabWidget tw = getTabWidget();
        if (tw instanceof LeTabWidget) {
            return (LeTabWidget) tw;
        }
        return null;
    }
    
    public void setCurrentTabByTag(String tag) {
        int i;
        for (i = 0; i < mTabSpecs.size(); i++) {
            if (mTabSpecs.get(i).getTag().equals(tag)) {
                setCurrentTab(i);
                break;
            }
        }
    }
    
    public int getCurrentTab() {
        return mCurrentTab;
    }
    
    public View getCurrentTabView() {
        if (mCurrentTab >= 0 && mCurrentTab < mTabSpecs.size()) {
            return mTabWidget.getChildTabViewAt(mCurrentTab);
        }
        return null;
    }
    
    public String getCurrentTabTag() {
        if (mCurrentTab >= 0 && mCurrentTab < mTabSpecs.size()) {
            return mTabSpecs.get(mCurrentTab).getTag();
        }
        return null;
    }
    
    public void setCurrentTab(int index, boolean click) {
        if (index < 0 || index >= getTabWidget().getChildCount())
            return;

        if (click) {
            if (index == getCurrentTab()) {
                if (mOnClickTabNotChangeListener != null) {
                    mOnClickTabNotChangeListener.onClickTabNotChanged(getCurrentTabTag());
                }

                long currentTimeMs = System.currentTimeMillis();
                if (mLastClickTime != -1l) {
                    if (currentTimeMs - mLastClickTime <= DOUBLE_CLICK_TIME_MS) {
                        mLastClickTime = -1l;
                        if (mOnDoubleClickTabNotChangeListener != null) {
                            mOnDoubleClickTabNotChangeListener
                                    .onDoubleClickTabNotChanged(getCurrentTabTag());
                        }
                    } else {
                        mLastClickTime = currentTimeMs;
                    }
                } else {
                    mLastClickTime = currentTimeMs;
                }
            } else {
                mLastClickTime = -1l;
            }
        }

        
        if (index < 0 || index >= mTabSpecs.size()) {
            return;
        }

        if (index == mCurrentTab) {
            return;
        }

        mCurrentTab = index;

        // Call the tab widget's focusCurrentTab(), instead of just
        // selecting the tab.
        mTabWidget.focusCurrentTab(mCurrentTab);

        //mTabContent.requestFocus(View.FOCUS_FORWARD);
        invokeOnTabChangeListener(getCurrentTabTag());
    }
    
    public void setCurrentTab(int index) {
        setCurrentTab(index, false);
    }

    private FragmentTransaction doTabChanged(String tabId, FragmentTransaction ft) {
        TabSpec newTab = null;
        for (int i = 0; i < mTabSpecs.size(); i++) {
            TabSpec tab = mTabSpecs.get(i);
            if (tab.tag.equals(tabId)) {
                newTab = tab;
            }
        }
        if (newTab == null) {
            throw new IllegalStateException("No tab known for tag " + tabId);
        }
        if (mCurrentTabSpec != newTab) {
            if (ft == null) {
                ft = mFragmentManager.beginTransaction();
                //slideFragment(mCurrentTabSpec, newTab, ft);
                
                // ft.setCustomAnimations(R.anim.slide_left_enter,
                // R.anim.slide_left_exit);
                // ft.setCustomAnimations(R.anim.slide_right_enter,
                // R.anim.slide_left_exit,
                // R.anim.slide_left_enter, R.anim.slide_right_exit);

            }
            if (mCurrentTabSpec != null) {
                if (mCurrentTabSpec.fragment != null) {
                    if (!mHideFragment) {
                        ft.detach(mCurrentTabSpec.fragment);
                    } else {
                        ft.hide(mCurrentTabSpec.fragment);
                    }
                }
            }
            if (newTab != null) {
                if (newTab.fragment == null) {
                    newTab.fragment = Fragment.instantiate(mContext,
                            newTab.clss.getName(), newTab.args);
                    ft.add(mContainerId, newTab.fragment, newTab.tag);
                } else {
                    if (!mHideFragment) {
                        ft.attach(newTab.fragment);
                    } else {
                        ft.show(newTab.fragment);
                    }
                }
            }
            // ft.addToBackStack(null);

            mCurrentTabSpec = newTab;
        }
        return ft;
    }

    private void invokeOnTabChangeListener(String tag) {
        if (mAttached) {
            FragmentTransaction ft = doTabChanged(tag, null);
            if (ft != null) {
                ft.commit();
            }
        }
        if (mOnTabChangeListener != null) {
            mOnTabChangeListener.onTabChanged(tag);
        }
    }



    /**
     * Interface definition for a callback to be invoked when tab changed
     */
    public interface OnClickTabNotChangeListener {
        void onClickTabNotChanged(String tabId);
    }

    public interface OnDoubleClickTabNotChangeListener {
        void onDoubleClickTabNotChanged(String tabId);
    }

    private OnClickTabNotChangeListener mOnClickTabNotChangeListener;
    private OnDoubleClickTabNotChangeListener mOnDoubleClickTabNotChangeListener;
    private long mLastClickTime = -1l;

    
    public void setOnClickTabNotChangeListener(OnClickTabNotChangeListener l) {
        mOnClickTabNotChangeListener = l;
    }

    public void setOnDoubleClickTabNotChangeListener(OnDoubleClickTabNotChangeListener l) {
        mOnDoubleClickTabNotChangeListener = l;
    }

    private int getTabSequenceNumber(TabSpec tab) {
        for (int i = 0; i < mTabSpecs.size(); i++) {
            TabSpec t = mTabSpecs.get(i);
            if (tab == t) {
                return i;
            }
        }
        return -1;
    }

    private void slideFragment(TabSpec oldTab, TabSpec newTab, FragmentTransaction ft) {
        int oldTabSn = getTabSequenceNumber(oldTab);
        int newTabSn = getTabSequenceNumber(newTab);

        if (oldTabSn == -1)
            return;

        if (oldTabSn > newTabSn) {

            ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        } else if (oldTabSn < newTabSn) {
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
    
    


    
    
    public class TabSpec {

        private String tag; // tag associated with a tab

        private IndicatorStrategy indicatorStrategy; // used to create a view for tab

        private Class<?> clss; // Fragment class
        private Bundle args;   // 
        private Fragment fragment;   // Fragment object

        private TabSpec(String _tag) {
            this(_tag, null, null);
        }
        
        private TabSpec(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }

        /**
         * Specify a view as the tab indicator.
         */
        public TabSpec setIndicator(View view) {
            indicatorStrategy = new ViewIndicatorStrategy(view);
            return this;
        }

        public String getTag() {
            return tag;
        }
    }

    /**
     * Specifies what you do to create a tab indicator.
     */
    private static interface IndicatorStrategy {

        /**
         * Return the view for the indicator.
         */
        View createIndicatorView();
    }

    /**
     * How to create a tab indicator by specifying a view.
     */
    private class ViewIndicatorStrategy implements IndicatorStrategy {

        private final View mView;

        private ViewIndicatorStrategy(View view) {
            mView = view;
        }

        public View createIndicatorView() {
            return mView;
        }
    }
    
    
    
    
    
    
    
    //  create views

    /**
     * Create an indicator view according to Leui's UI design
     */
    public View createIndicatorView(int iconId, String title) {
        final Context context = getContext();
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = null;
        if (title != null && !title.isEmpty()) {
            v = inflater.inflate(R.layout.le_bottom_tab_with_icon_title, getTabWidget(), false);
            TextView tv = (TextView) v.findViewById(R.id.title);
            if (tv != null)
                tv.setText(title);
        } else {
            // v = inflater.inflate(R.layout.le_tab_indicator_with_icon_only2,
            // getTabWidget(), false);
            v = inflater.inflate(R.layout.le_bottom_tab_with_icon_only, getTabWidget(), false);
        }

        ImageView imgView = (ImageView) v.findViewById(R.id.icon);
        if (imgView != null)
            imgView.setImageResource(iconId);

        return v;
    }


    public void setTabWidgetLayout(Activity activity) {
        if (activity == null || getTabWidget() == null || getTabWidget().getTabCount() == 0)
            return;
        LeTabWidgetUtils.setTabWidgetLayout(activity, this.getTabWidget());
        // LeTabWidgetUtils.setTabWidgetLayout(activity, this.getTabWidget(),
        // true);
    }

    public void setTabWidgetLayout(Activity activity, boolean isDialerApp) {
        if (activity == null || getTabWidget() == null || getTabWidget().getTabCount() == 0)
            return;
        LeTabWidgetUtils.setTabWidgetLayout(activity, this.getTabWidget(), isDialerApp);
        // LeTabWidgetUtils.setTabWidgetLayout(activity, this.getTabWidget(),
        // true);
    }
}
