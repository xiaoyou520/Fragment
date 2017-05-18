package you.xiaochen.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import you.xiaochen.R;
import you.xiaochen.adapter.Tab3Adapter;

/**
 * Created by you on 2016/3/28.
 * test FragmentPagerAdapter
 */

public class MainTabFragment3 extends Fragment {

    private static final String[] titles = {"Pager1", "Pager2", "Pager3", "Pager4"};

    private TabLayout pager_tabs;

    private ViewPager vp_list;

    public static MainTabFragment3 newInstance() {
        return new MainTabFragment3();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("you", "MainTabFragment3 onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("you", "MainTabFragment3 onCreateView");
        View v = inflater.inflate(R.layout.fragment_tab3, container, false);
        pager_tabs = (TabLayout) v.findViewById(R.id.pager_tabs);
        vp_list = (ViewPager) v.findViewById(R.id.vp_list);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i("you", "MainTabFragment3 onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("you", "MainTabFragment3 onActivityCreated");
        vp_list.setAdapter(new Tab3Adapter(getChildFragmentManager(), titles));//***  getChildFragmentManager
        pager_tabs.setupWithViewPager(vp_list);
        super.onActivityCreated(savedInstanceState);
    }

}
