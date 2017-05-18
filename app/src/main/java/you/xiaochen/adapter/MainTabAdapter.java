package you.xiaochen.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import you.xiaochen.base.FrameAdapter;
import you.xiaochen.fragment.MainTabFragment1;
import you.xiaochen.fragment.MainTabFragment2;
import you.xiaochen.fragment.MainTabFragment3;

/**
 * Created by dgaz on 2017/3/28.
 */

public class MainTabAdapter extends FrameAdapter {

    public MainTabAdapter(FragmentManager mFragmentManager) {
        super(mFragmentManager);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MainTabFragment1.newInstance();
            case 1:
                return MainTabFragment2.newInstance();
            case 2:
                return MainTabFragment3.newInstance();
        }
        return null;
    }
}
