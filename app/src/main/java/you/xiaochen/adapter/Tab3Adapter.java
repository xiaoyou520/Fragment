package you.xiaochen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import you.xiaochen.base.FragmentPagerAdapter;
import you.xiaochen.fragment.Fragment1;

/**
 * Created by dgaz on 2017/3/28.
 */

public class Tab3Adapter extends FragmentPagerAdapter {

    private final String[] titles;

    public Tab3Adapter(FragmentManager mManager, String[] titles) {
        super(mManager);
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment1.newInstance(titles[position]);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
