/*
package com.markrap.attendance;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.markrap.R;
import com.markrap.base.BaseActivity;
import com.markrap.fragments.AttendanceFragments;

public class AttendanceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment = AttendanceFragments.newInstance(this);
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment).commit();
    }

    @Override
    protected void setUp() {

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_attendance_new;
    }

  */
/*  @Override
    protected void setContextView() {
        setContentView(R.layout.activity_attendance_new);
    }
*//*

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
*/
