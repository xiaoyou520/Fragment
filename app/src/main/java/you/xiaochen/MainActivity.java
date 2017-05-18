package you.xiaochen;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import you.xiaochen.adapter.MainTabAdapter;
import you.xiaochen.base.FragmentFrameLayout;

public class MainActivity extends AppCompatActivity {

    private FragmentFrameLayout fl_main;

    private RadioGroup rg_main;

    private MainTabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main = (FragmentFrameLayout) findViewById(R.id.fl_main);
        rg_main = (RadioGroup) findViewById(R.id.rg_main);

        adapter = new MainTabAdapter(getSupportFragmentManager());
        fl_main.setFrameAdapter(adapter);



        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        fl_main.setCurrentItem(0);
                        break;
                    case R.id.rb2:
                        fl_main.setCurrentItem(1);
                        break;
                    case R.id.rb3:
                        fl_main.setCurrentItem(2);
                        break;
                }
            }
        });
        if (savedInstanceState == null) {
            fl_main.setCurrentItem(0);//saveinstancestate 会自动管理
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
