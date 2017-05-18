package you.xiaochen.base;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.Fragment.SavedState;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import java.util.ArrayList;

/**
 * Created by you on 15/8/31.
 * 与adapter类似,切换时回收fragment,生命方法(onCreateView,onActivityCreated)重新调用,但保存了fragment的state(Fragment.SaveState)
 * 内存不足时也会保存(Fragment.SaveState)
 */
public abstract class FrameStateAdapter extends FragmentFrameLayout.Adapter {
    private static final String KEY = "FrameStateAdapter";
    private ArrayList<SavedState> mSavedState;

    public FrameStateAdapter(FragmentManager mFragmentManager) {
        super(mFragmentManager);
        mSavedState = new ArrayList<SavedState>();
    }

    @Override
    public void instantFragment(FrameLayout fm, int position) {
        if (position == mCurrentPosition && mCurrentFragment != null) {
            mCurrentFragment.setMenuVisibility(true);
            mCurrentFragment.setUserVisibleHint(true);
            return;
        }
        FragmentTransaction ft = mManager.beginTransaction();
        while (mSavedState.size() <= mCurrentPosition) {
            mSavedState.add(null);
        }
        if (mCurrentFragment != null) {  //save state first
            SavedState saveState = mManager.saveFragmentInstanceState(mCurrentFragment);
            mSavedState.set(mCurrentPosition, saveState);
            mCurrentFragment.setMenuVisibility(false);
            mCurrentFragment.setUserVisibleHint(false);
            ft.remove(mCurrentFragment);
        }
        Fragment fragment = getItem(position);
        if (mSavedState.size() > position) {
            SavedState fss = mSavedState.get(position);
            if (fss != null) {
                fragment.setInitialSavedState(fss);
            }
        }
        ft.add(fm.getId(), fragment);
        if (fragment != null) {
            fragment.setMenuVisibility(true);
            fragment.setUserVisibleHint(true);
        }
        mCurrentFragment = fragment;
        mCurrentPosition = position;
        ft.commitAllowingStateLoss();
        ft = null;
        mManager.executePendingTransactions();
    }

    @Override
    public Parcelable saveState() {
        Bundle state = null;
        if (mSavedState.size() > 0) {
            state = new Bundle();
            SavedState[] fss = new SavedState[mSavedState.size()];
            mSavedState.toArray(fss);
            state.putParcelableArray("states", fss);
            state.putInt("currentPosition", mCurrentPosition);
        }
        if (state == null) {
            state = new Bundle();
        }
        if (mCurrentFragment != null) {
            String key = KEY + mCurrentPosition;
            mManager.putFragment(state, key, mCurrentFragment);
        }
        return state;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        if (state != null) {
            Bundle bundle = (Bundle) state;
            bundle.setClassLoader(loader);
            Parcelable[] fss = bundle.getParcelableArray("states");
            mCurrentPosition = bundle.getInt("currentPosition");
            mSavedState.clear();
            if (fss != null) {
                for (int i = 0; i < fss.length; i++) {
                    mSavedState.add((SavedState) fss[i]);
                }
            }
            String key = KEY + mCurrentPosition;
            mCurrentFragment = mManager.getFragment(bundle, key);
        }
    }

}
