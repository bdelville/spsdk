package eu.hithredin.spsdk.app;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import eu.hithredin.spsdk.R;

/**
 * Created by benoit on 2/2/16.
 */
public abstract class BaseTabFragment extends BaseLayoutFragment {
    protected ViewPager viewPager;
    protected TabLayout tabLayout;

    @Override
    protected void assignViews(View root) {
        super.assignViews(root);
        tabLayout = (TabLayout) root.findViewById(R.id.tablayout);
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
    }


    @Override
    protected void populateViews(Bundle savedInstanceState, ScreenStatus status) {
        super.populateViews(savedInstanceState, status);
        viewPager.setAdapter(buildPageAdapter());
        if(tabLayout != null){
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    protected FragmentPagerAdapter buildPageAdapter(){
        return new TabbarAdapter(getChildFragmentManager());
    }

    protected abstract int getCountTab();

    protected abstract Fragment getPageFragmentAt(int index);

    protected abstract String getTitleTab(int index);

    protected class TabbarAdapter extends FragmentPagerAdapter{

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


}
