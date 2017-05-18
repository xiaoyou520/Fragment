package you.xiaochen.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by you on 15/8/31.
 * 即保存Fragment对象引用 ,切换时不调用onCreateView,onActivityCreated又保存了fragment的state(Fragment.SaveState)
 * 内存不足时的意外回收也保存了(Fragment.SaveState)
 */
public abstract class FrameAdapter extends FragmentFrameLayout.Adapter {

    private static final String KEY = "FrameAdapter";
    private List<Fragment> mFragments = new ArrayList<Fragment>();

    public FrameAdapter(FragmentManager mFragmentManager) {
        super(mFragmentManager);
    }

    @Override
    public void instantFragment(FrameLayout fm, int position) {
        if (this.mCurrentPosition == position) return;
        FragmentTransaction ft = mManager.beginTransaction();
        if (mCurrentFragment != null) {
            ft.hide(mCurrentFragment);
            mCurrentFragment.setMenuVisibility(false);
            mCurrentFragment.setUserVisibleHint(false);
        }
        final long itemId = getItemId(position);
        String name = makeFragmentName(fm.getId(), itemId);
        Fragment fragment = mManager.findFragmentByTag(name);
        if (fragment != null) {
            ft.show(fragment);
        } else {
            fragment = getItem(position);
            while (mFragments.size() <= position) {
                mFragments.add(null);
            }
            mFragments.set(position, fragment);
            ft.add(fm.getId(), fragment, makeFragmentName(fm.getId(), itemId));
        }
        if (fragment != null) {
            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);
        }
        mCurrentFragment = fragment;
        this.mCurrentPosition = position;
        ft.commitAllowingStateLoss();
        ft = null;
        mManager.executePendingTransactions();
    }

    @Override
    public Parcelable saveState() {
        Bundle state = new Bundle();
        state.putInt("currentPosition", mCurrentPosition);
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
            mCurrentPosition = bundle.getInt("currentPosition");
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
            FragmentTransaction ft = mManager.beginTransaction();
            for (int i = 0; i < mFragments.size(); i++) {
                Fragment f = mFragments.get(i);
                if (f != null) {
                    if (i == mCurrentPosition) {
                        mCurrentFragment = f;
                    } else {
                        ft.hide(f);
                        f.setMenuVisibility(false);
                        f.setUserVisibleHint(false);
                    }
                }
            }
            ft.commitAllowingStateLoss();
            ft = null;
            mManager.executePendingTransactions();
        }
    }

}
