package you.xiaochen.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import you.xiaochen.R;
import you.xiaochen.base.FragmentFrameLayout;

/**
 * Created by you on 2016/3/28.
 *
 * test FragmentFrameLayout.Adapter
 *
 */

public class MainTabFragment1 extends Fragment {

    private RadioGroup rg_tab;

    private FragmentFrameLayout fl_tab;

    public static MainTabFragment1 newInstance() {
        return new MainTabFragment1();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("you", "MainTabFragment1 onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("you", "MainTabFragment1 onCreateView");
        View v = inflater.inflate(R.layout.fragment_tab1, container, false);
        rg_tab = (RadioGroup) v.findViewById(R.id.rg_tab);
        fl_tab = (FragmentFrameLayout) v.findViewById(R.id.fl_tab);

        fl_tab.setFrameAdapter(new FragmentFrameLayout.Adapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return Fragment1.newInstance("Adapter test1");
                    case 1:
                        return Fragment1.newInstance("Adapter test2");
                    case 2:
                        return Fragment1.newInstance("Adapter test3");
                }
                return null;
            }
        });
        rg_tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        fl_tab.setCurrentItem(0);
                        break;
                    case R.id.rb2:
                        fl_tab.setCurrentItem(1);
                        break;
                    case R.id.rb3:
                        fl_tab.setCurrentItem(2);
                        break;
                }
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.i("you", "MainTabFragment1 onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("you", "MainTabFragment1 onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            fl_tab.setCurrentItem(0);//saveinstancestate 会自动管理
        }
    }
}
