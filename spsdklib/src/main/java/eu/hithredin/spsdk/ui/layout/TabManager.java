package eu.hithredin.spsdk.ui.layout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import eu.hithredin.spsdk.R;
import eu.hithredin.spsdk.ui.ScreenStatus;

/**
 * Helper to handle a TabLayout and ViewPager within an activity or a fragment
 */
public abstract class TabManager {
    protected ViewPager viewPager;
    protected TabLayout tabLayout;

    private Fragment fragment;
    private AppCompatActivity activity;

    public TabManager(Fragment fragment) {
        this.fragment = fragment;
    }

    public TabManager(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void assignViews(View root) {
        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
    }

    public void assignViews() {
        tabLayout = (TabLayout) activity.findViewById(R.id.tablayout);
        viewPager = (ViewPager) activity.findViewById(R.id.viewpager);
    }

    public void populateViews(Bundle savedInstanceState, ScreenStatus status) {
        viewPager.setAdapter(buildPageAdapter());
        if(tabLayout != null){
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    public FragmentPagerAdapter buildPageAdapter(){
        if(fragment != null){
            return new TabbarAdapter(fragment.getChildFragmentManager());
        }
        else{
            return new TabbarAdapter(activity.getSupportFragmentManager());
        }
    }

    public abstract int getCountTab();

    public abstract Fragment getPageFragmentAt(int index);

    public abstract String getTitleTab(int index);

    public class TabbarAdapter extends FragmentPagerAdapter{

        public TabbarAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return getPageFragmentAt(position);
        }

        @Override
        public int getCount() {
            return getCountTab();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTitleTab(position);
        }
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public TabLayout getTabLayout() {
        return tabLayout;
    }
}
