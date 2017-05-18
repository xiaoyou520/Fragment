package you.xiaochen.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by you on 2015/9/6.
 * 与SDK自带FragmentPagerAdapter不同
 * ViewPager 切换时不调用onCreateView,onActivityCreated又保存了fragment的state(Fragment.SaveState)
 * 内存不足时的意外回收会保存(Fragment.SaveState)状态
 */

public abstract class FragmentPagerAdapter extends PagerAdapter {
    /**
     * save fragment key
     */
    private static final String KEY = FragmentPagerAdapter.class.getName();

    protected final FragmentManager mManager;

    protected Fragment mCurrentFragment;

    private List<Fragment> mFragments = new ArrayList<Fragment>();

    private FragmentTransaction mCurTransaction;

    public FragmentPagerAdapter(FragmentManager mManager) {
        this.mManager = mManager;
    }

    public abstract Fragment getItem(int position);

    @Override
    public void startUpdate(ViewGroup container) {
        if (container.getId() == View.NO_ID) {
            throw new IllegalStateException("ViewPager with adapter " + this
                    + " requires a view id");
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //Log.i("you", "instantItem "+position);
        if (mCurTransaction == null) {
            mCurTransaction = mManager.beginTransaction();
        }
        final long itemId = getItemId(position);
        String name = makeFragmentName(container.getId(), itemId);
        Fragment fragment = mManager.findFragmentByTag(name);
        if (fragment == null) {
            fragment = getItem(position);
            while (mFragments.size() <= position) {
                mFragments.add(null);
            }
            mFragments.set(position, fragment);
            mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
        } else {
            mCurTransaction.show(fragment);
        }
        if (fragment != null) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }
        return fragment;

    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        //Log.i("you", "setPrimaryItem "+position+"  "+fragment.getClass().getName());
        if (fragment != mCurrentFragment) {
            if (mCurrentFragment != null) {
                mCurrentFragment.setMenuVisibility(false);
                mCurrentFragment.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentFragment = fragment;
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment) object;
        if (mCurTransaction == null) {
            mCurTransaction = mManager.beginTransaction();
        }
        if (fragment != null) {
            mCurTransaction.hide(fragment);
        }
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mManager.executePendingTransactions();
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment)object).getView() == view;
    }

    @Override
    public Parcelable saveState() {
        Bundle state = new Bundle();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment f = mFragments.get(i);
            if (f != null) {
                String key = KEY + i;
                mManager.putFragment(state, key, f);
            }
        }
        return state;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            Iterable<String> keys = bundle.keySet();
            for (String key : keys) {
                if (key.startsWith(KEY)) {
                    int index = Integer.parseInt(key.substring(KEY.length()));
                    Fragment f = mManager.getFragment(bundle, key);
                    if (f != null) {
                        while (mFragments.size() <= index) {
                            mFragments.add(null);
                        }
                        f.setMenuVisibility(false);
                        mFragments.set(index, f);
                    }
                }
            }
        }
    }

    /**
     * Fragments标记
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return position;
    }

    protected String makeFragmentName(int viewId, long id) {
        return "FrameAdapter:" + viewId + ":" + id;
    }

    /**
     * 当前fragment
     * @return
     */
    public Fragment getmCurrentFragment() {
        return mCurrentFragment;
    }

}
