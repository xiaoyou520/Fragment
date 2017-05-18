package you.xiaochen.base;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by you on 2015/8/27.
 *
 * adapter模式管理fragment
 *
 */

public class FragmentFrameLayout extends FrameLayout {
    /**
     * fragment adapter
     */

    private Adapter mAdapter;
    /**
     * 当前选中的fragment所在位置
     */
    private int mCurrentPosition = -1;
    /**
     * (Fragment)是否附着窗体上
     */
    private boolean isAttachedToWindow;
    /**
     * 内存不足时的意外回收保存的数据
     */
    private Parcelable mRestoredAdapterState;

    private int mRestoredPosition = -1;

    private ClassLoader mRestoredClassLoader;

    public FragmentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FragmentFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FragmentFrameLayout(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (getId() == View.NO_ID) {
            this.setId(hashCode());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isAttachedToWindow) {
            isAttachedToWindow = true;
            setCurrentItem(mCurrentPosition);
        }
    }

    /**
     * 设置显示当前位置的Fragment
     */
    public void setCurrentItem(int position) {
        if (mAdapter != null && position >= 0) {
            mCurrentPosition = position;
            if (isAttachedToWindow) {
                mAdapter.instantFragment(this, position);
            }
        }
    }

    /**
     * 设置管理Fragment适配器
     */
    public void setFrameAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            if (this.mRestoredPosition >= 0) {
                mAdapter.restoreState(mRestoredAdapterState, mRestoredClassLoader);
                setCurrentItem(mRestoredPosition);
                mRestoredPosition = -1;
                mRestoredAdapterState = null;
                mRestoredClassLoader = null;
            }
        }
    }

    public Adapter getFrameAdapter() {
        return this.mAdapter;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.position = mCurrentPosition;
        if (mAdapter != null) {
            ss.adapterState = mAdapter.saveState();
        }
        return ss;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        if (mAdapter != null) {
            mAdapter.restoreState(ss.adapterState, ss.loader);
            setCurrentItem(ss.position);
        } else {
            mRestoredPosition = ss.position;
            mRestoredAdapterState = ss.adapterState;
            mRestoredClassLoader = ss.loader;
        }
    }

    /**
     * Frame默认适配器,切换时只保存fragment对象引用,不保存状态(不会调用onCreate,onActivityCreated会调用onCreateView),
     * 内存不足时的意外回收会保存(Fragment.SaveState)状态
     * 多层嵌套时注意FragmentManager的获取  Fragment.getChildFragmentManager()
     *
     * @author you
     */
    public static abstract class Adapter {
        /**
         * fragmentManager
         */
        protected final FragmentManager mManager;
        /**
         * 当前加载的Fragment
         */
        protected Fragment mCurrentFragment;
        /**
         * 当前加载fargment的位置
         */
        protected int mCurrentPosition = -1;

        public Adapter(FragmentManager mFragmentManager) {
            this.mManager = mFragmentManager;
        }

        /**
         * fragments count
         * @return
         */
        public abstract int getCount();

        /**
         * 获取fragment
         * @param position
         * @return
         */
        public abstract Fragment getItem(int position);

        public void instantFragment(FrameLayout fm, int position) {
            if (this.mCurrentPosition == position)
                return;
            FragmentTransaction ft = mManager.beginTransaction();
            if (mCurrentFragment != null) {
                ft.detach(mCurrentFragment);
                mCurrentFragment.setMenuVisibility(false);
                mCurrentFragment.setUserVisibleHint(false);
            }
            final long itemId = getItemId(position);
            String name = makeFragmentName(fm.getId(), itemId);
            Fragment fragment = mManager.findFragmentByTag(name);
            if (fragment != null) {
                ft.attach(fragment);
            } else {
                fragment = getItem(position);
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

        /**
         * 保存fragment的状态
         * @return
         */
        public Parcelable saveState() {
            return null;
        }

        /**
         * 恢复fragment的状态,
         * @param state
         * @param loader
         */

        public void restoreState(Parcelable state, ClassLoader loader) {

        }


        public Fragment getCurFragment() {
            return mCurrentFragment;
        }

        /**
         * fragment唯一标记
         * @param position
         * @return
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * 生成FragmentManager管理的tag
         * @param viewId
         * @param id
         * @return
         */
        protected String makeFragmentName(int viewId, long id) {
            return "FrameAdapter:" + viewId + ":" + id;
        }
    }

    /**
     * 内存不足时的状态保存
     */
    static class SavedState extends BaseSavedState {
        int position;
        Parcelable adapterState;
        ClassLoader loader;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in, ClassLoader loader) {
            super(in);
            if (loader == null) {
                loader = getClass().getClassLoader();
            }
            position = in.readInt();
            adapterState = in.readParcelable(loader);
            this.loader = loader;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
            out.writeParcelable(adapterState, flags);
        }

        public static final Creator<SavedState> CREATOR
                = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                return new SavedState(in, loader);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        });
    }


}
