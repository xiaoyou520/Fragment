package you.xiaochen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import you.xiaochen.R;

/**
 * Created by you on 2016/3/27.
 */

public class Fragment1 extends Fragment {


    private String fragmentName;

    private TextView tv_test;

    public static Fragment1 newInstance(String fragmentName) {
        Fragment1 f = new Fragment1();
        Bundle bundle = new Bundle();
        bundle.putString("fragmentName", fragmentName);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentName = getArguments().getString("fragmentName");
        Log.i("you", "fragment "+fragmentName+" onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test, container, false);
        tv_test = (TextView) v.findViewById(R.id.tv_test);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("you", "fragment "+fragmentName+" onViewCreated");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_test.setText(fragmentName);
        Log.i("you", "fragment "+fragmentName+" onActivityCreated");
    }

}
