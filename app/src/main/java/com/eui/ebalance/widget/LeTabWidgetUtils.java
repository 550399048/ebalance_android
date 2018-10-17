package com.eui.ebalance.widget;

/*
 * Usage:
 * 1. provide an static createIndicatorView(...) to create an indicator view according to Leui's UI design
 * 2. provide an static setTabWidgetLayout(...) to adjust indicator views layout according to Leui's UI design
 * 
 * Example:
 *
 *  import com.leui.widget.LeTabWidgetUtils;
 *  import android.support.v4.app.LeFragmentTabHost;
 *  
 *  public class MainActivity extends FragmentActivity {
 *      private LeFragmentTabHost mTabHost;
 * 
 *      @Override
 *      protected void onCreate(Bundle savedInstanceState) {
 *      super.onCreate(savedInstanceState);
 *          setContentView(R.layout.activity_main);
 *
 *          FrameLayout realTabContent = (FrameLayout)findViewById(R.id.realtabcontent);
 *
 *          mTabHost = (LeFragmentTabHost) findViewById(android.R.id.tabhost);
 *          mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent, realTabContent);
 *
 *          // removing divider
 *          TabWidget tw = mTabHost.getTabWidget();
 *          tw.setDividerDrawable(null);
 *
 *          View indicatorView;
 *          LayoutInflater inflater =
 *                  (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 *
 *          // create an tab indicator view with icon and title
 *          indicatorView = LeTabWidgetUtils.createIndicatorView(inflater, 
 *                  tw, 
 *                  R.drawable.calendar, 
 *                  getString(R.string.calendar));
 *          mTabHost.addTab(mTabHost.newTabSpec("calendar").setIndicator(indicatorView), 
 *                  BrowserFragment.class, null);
 *
 *          // create an tab indicator view with icon only
 *          indicatorView = LeTabWidgetUtils.createIndicatorView(inflater, 
 *                  tw, 
 *                  R.drawable.writer, 
 *                  null);
 *          mTabHost.addTab(mTabHost.newTabSpec("writer").setIndicator(indicatorView), 
 *                  Fragment2.class, null);
 *
 *          // adjust tab indicator views layout to fit Leui's UI design
 *          LeTabWidgetUtils.setTabWidgetLayout(this, tw);
 *          ...
 *      }    
 *  }
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabWidget;
import android.widget.TextView;

import com.eui.ebalance.R;


public class LeTabWidgetUtils {
    private static final float SCALE_TAB_WIDTH_IF_1_TAB_ONLY = 3f;

    /**
     * Create an indicator view according to Leui's UI design 
     */
    public static View createIndicatorView(LayoutInflater inflater, TabWidget tw, int iconId, String title) {
        if (inflater == null || tw == null)
            return null;

        View v = null;
        if (title != null && !title.isEmpty()) {
            //v = inflater.inflate(R.layout.le_tab_indicator_with_icon_title_linearlayout, tw, false);
            v = inflater.inflate(R.layout.le_bottom_tab_with_icon_title, tw, false);
            TextView tv = (TextView) v.findViewById(R.id.title);
            tv.setText(title);
        } else {
            //v = inflater.inflate(R.layout.le_tab_indicator_with_icon_only_linearlayout, tw, false);
            v = inflater.inflate(R.layout.le_bottom_tab_with_icon_only, tw, false);
        }

        ImageView imgView = (ImageView) v.findViewById(R.id.icon);
        imgView.setImageResource(iconId);

        return v;
    }

    private static boolean hasTitle(TabWidget tw) {
      if (tw == null || tw.getTabCount() == 0)
            return false;
        for (int i = 0; i < tw.getTabCount(); i++) {
            View tab = tw.getChildAt(i);
            if (tab != null && tab instanceof ViewGroup) {
                ViewGroup vGroup = (ViewGroup) tab;
                if (vGroup.getChildCount() <= 1)
                    continue;

                for (int j = 0; j < vGroup.getChildCount(); j++) {
                    if (vGroup.getChildAt(j) instanceof TextView)
                        return true;
                }
            }
        }

        return false;
    }

    /**
     * set tab indicator view's leftMargin and rightMargin to fit Leui's UI design 
     */
    public static void setTabWidgetLayout(Activity activity, TabWidget tw) {
        setTabWidgetLayout(activity, tw, false);
    }

    public static void setTabWidgetLayout(Activity activity, TabWidget tw, boolean isDialerApp) {
        if (activity == null || tw == null || tw.getChildCount() == 0)
            return;

        setTabWidgetLayout(activity, tw, hasTitle(tw), isDialerApp);
    }

    public static void setTabWidgetLayout(Activity activity, LinearLayout bottomView, boolean hasTitle) {
        if (activity == null || bottomView == null || bottomView.getChildCount() == 0)
            return;

        setTabWidgetLayout(activity.getApplicationContext(), bottomView, hasTitle, false);
    }


    public static void setTabWidgetLayout(Context context, LinearLayout bottomView, boolean hasTitle, boolean isDialerApp) {
        if (context == null || bottomView == null || bottomView.getChildCount() == 0)
            return;
        
        Resources res = context.getResources();
        
        // get screen width in px
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        
        // get tab width in px
        int tabWidth = 0;
        
        int count = bottomView.getChildCount();
        int end = 0;
        
        switch (count) {
            case 1:
                tabWidth = res.getDimensionPixelOffset(R.dimen.le_bottom_tab_width_for_1_tab);
                end = (screenWidth - tabWidth * count) / 2;
                break;
            case 2:
                tabWidth = res.getDimensionPixelOffset(R.dimen.le_bottom_tab_width_for_2_tab);
                end = (int)((screenWidth - tabWidth * 2 ) / 2);
                break;
            case 3:
                end = res.getDimensionPixelOffset(R.dimen.le_bottom_tab_end_for_3_tab);
                tabWidth = (screenWidth - end * 2) / 3;
                break;
            case 4:
                tabWidth = screenWidth / 4;
                break;
            case 5:
                tabWidth = screenWidth / 5;
                break;

            default:
                break;
        }
        
        
        // layout tabs
        for (int i=0; i<count; i++) {
            final View tab = bottomView.getChildAt(i);

            // set width/height/leftMargin/rightMargin for tab indicator view
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)tab.getLayoutParams();
            if (params == null) {
                params = new ViewGroup.MarginLayoutParams(tabWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 0, 0);
            }

            
            if (i == 0) {
                params.leftMargin = end;
                
                if (count == 1) {
                    params.rightMargin = end;
                } else {
                    params.rightMargin = 0;
                }
            } else if (i == count - 1) {
                params.leftMargin = 0;
                params.rightMargin = end;
            } else {
                params.leftMargin = 0;
                params.rightMargin = 0;
            }
            
            params.width = tabWidth;

            
            tab.setLayoutParams(params);
        }
        
        //setTouchGlowWidth2HeightRatio(res, bottomView);
    }


}
