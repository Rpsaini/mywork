package com.markrap.dashboard;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.preferences.SavePreferences;
import com.markrap.R;
import com.markrap.base.BaseActivity;
import com.markrap.fragments.AttendanceFragments;
import com.markrap.fragments.ChangePassword;
import com.markrap.fragments.HomeFragments;
import com.markrap.fragments.AllStoresFrg;
import com.markrap.fragments.MyOrderFrg;
import com.markrap.fragments.MyPointsFragments;
import com.markrap.fragments.NotificationFrgmenst;
import com.markrap.fragments.TaskListFragments;
//import com.markrap.services.LocService;
import com.markrap.services.MyService;
import com.markrap.splash.SplashActivity;
import com.markrap.utility.AppConstants;

import org.json.JSONObject;


public class MainActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "MainActivity";
    public static final String mBroadcastStringAction = "location";
    RelativeLayout relativeLayouts;
    private int drawerCloseTime = 300;
    private DrawerLayout drawerLayout;
    private ImageButton menuBtn;
    TextView txtToolbartext;
    ImageView imgICBack;
    private LinearLayout linearHome_btn,linearAttendance,linearTaskList,linearMAnageOrder,linearMyPoints,linearNotification,linearChangePasswords,llLogout;
    TextView home_btn,btn_attendance,btn_taskList,btn_manageOrder,btnMyPoints,btnNotification,btnChangePassword,btnMyOrders,btnLogout;
    private boolean doubleBackToExitPressedOnce;

    public static Intent getIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initListners();
        setupHomeFragment();

        Intent intent = new Intent(this, MyService.class);
        startService(intent);

        registerReceiver(receiver,new IntentFilter(mBroadcastStringAction));


    }

    private void initView() {
//        imgNotification=(ImageButton)findViewById(R.id.imgNotification);
         txtToolbartext =findViewById(R.id.txtToolbartext);
        imgICBack=(ImageView)findViewById(R.id.imgICBack);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuBtn = (ImageButton) findViewById(R.id.menu_btn);
        linearHome_btn = (LinearLayout) findViewById(R.id.linearHome_btn);

        home_btn = (TextView) findViewById(R.id.home_btn);
        menuBtn = (ImageButton) findViewById(R.id.menu_btn);
        linearAttendance = (LinearLayout) findViewById(R.id.linearAttendance);
        linearNotification=(LinearLayout)findViewById(R.id.linearNotification);
        btnNotification=(TextView) findViewById(R.id.btnNotification);
        btn_attendance = (TextView) findViewById(R.id.btn_attendance);
        linearTaskList = (LinearLayout) findViewById(R.id.linearTaskList);

        btn_taskList = (TextView) findViewById(R.id.btn_taskList);
        linearMAnageOrder = (LinearLayout) findViewById(R.id.linearMAnageOrder);

        btn_manageOrder = (TextView) findViewById(R.id.btn_manageOrder);
        linearMyPoints = (LinearLayout) findViewById(R.id.linearMyPoints);

        btnMyPoints = (TextView) findViewById(R.id.btnMyPoints);
        linearChangePasswords=(LinearLayout)findViewById(R.id.linearChangePasswords);
        btnChangePassword=(TextView)findViewById(R.id.btnChangePassword);
        btnMyOrders=(TextView)findViewById(R.id.btnMyOrders);
        llLogout=(LinearLayout)findViewById(R.id.llLogout);
        btnLogout=(TextView)findViewById(R.id.btnLogout);


    }

    @Override
    protected void setUp() {

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }


    private void initListners() {
        menuBtn.setOnClickListener(this);
        home_btn.setOnClickListener(this);
        linearHome_btn.setOnClickListener(this);
        btn_attendance.setOnClickListener(this);
        linearAttendance.setOnClickListener(this);
        btn_taskList.setOnClickListener(this);
        linearTaskList.setOnClickListener(this);
        btn_manageOrder.setOnClickListener(this);
        linearMAnageOrder.setOnClickListener(this);
        btnMyPoints.setOnClickListener(this);
        linearMyPoints.setOnClickListener(this);
//
//        .setOnClickListener(this);
        linearNotification.setOnClickListener(this);
        btnNotification.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        linearChangePasswords.setOnClickListener(this);
        btnMyOrders.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        imgICBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
//            case R.id.imgNotification:
////                it will check drawer state
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        showNotificationonManageStore();
//                    }
//                }, drawerCloseTime);
//
//                closeDrawer();
//                break;
            case R.id.menu_btn:
//                it will check drawer state
                checkDrawerState();
                break;
            case R.id.home_btn:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupHomeFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.linearHome_btn:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupHomeFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.btn_taskList:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupTaskListFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.linearTaskList:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupTaskListFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.btn_attendance:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupAttendanceFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.linearAttendance:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupAttendanceFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.btn_manageOrder:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupTManageOrdersFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.linearMAnageOrder:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupTManageOrdersFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.btnMyPoints:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupTMyPointsFragment();
                    }
                }, drawerCloseTime);

                break;
            case R.id.linearMyPoints:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupTMyPointsFragment();
                    }
                }, drawerCloseTime);

                break;

        case R.id.linearNotification:
//                it will check drawer state
        checkDrawerState();
//                this function will inflate the My payment screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showNotificationonNavMenu();
            }
        }, drawerCloseTime);

        break;
            case R.id.btnNotification:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNotificationonNavMenu();
                    }
                }, drawerCloseTime);

                break;
            case R.id.linearChangePasswords:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showChangePassword();
                    }
                }, drawerCloseTime);

                break;
            case R.id.btnChangePassword:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showChangePassword();

                    }
                }, drawerCloseTime);
                break;

            case R.id.btnMyOrders:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showMyOrderFrg();

                    }
                }, drawerCloseTime);
                break;
            case R.id.btnLogout:
//                it will check drawer state
                checkDrawerState();
//                this function will inflate the My payment screen
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        logoutDialog();
                    }
                }, drawerCloseTime);
                break;

    }


    }


    private void checkDrawerState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }

    private void openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }



    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showFragment(Fragment fragment, String tag) {


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }
    public void setupHomeFragment() {
        txtToolbartext.setText("Home");
        imgICBack.setVisibility(View.GONE);

        showFragment(HomeFragments.newInstance(this), HomeFragments.TAG);

    }
    private void setupAttendanceFragment() {
        txtToolbartext.setText("Attendance");
        imgICBack.setVisibility(View.VISIBLE);
       showFragment(new AttendanceFragments(), AttendanceFragments.TAG);



        /*Intent in =new Intent(getApplicationContext(), AttendanceActivity.class);
        startActivity(in);*/

    }
    private void setupTaskListFragment() {
        txtToolbartext.setText("Task");
        imgICBack.setVisibility(View.GONE);

        showFragment(TaskListFragments.newInstance(this), TaskListFragments.TAG);

    }
    private void setupTManageOrdersFragment() {
//        imgNotification.setVisibility(View.VISIBLE);
        imgICBack.setVisibility(View.GONE);

        txtToolbartext.setText(getResources().getString(R.string.manageorder));
        showFragment(AllStoresFrg.newInstance(this), AllStoresFrg.TAG);

    }
    private void setupTMyPointsFragment() {
        txtToolbartext.setText("My Points");
        imgICBack.setVisibility(View.GONE);

        showFragment(MyPointsFragments.newInstance(this), MyPointsFragments.TAG);

    }

    private void showNotificationonManageStore() {
//        imgNotification.setVisibility(View.VISIBLE);
        imgICBack.setVisibility(View.GONE);

        txtToolbartext.setText("Notifcations ");
        showFragment(NotificationFrgmenst.newInstance(this), NotificationFrgmenst.TAG);

    }
    private void showNotificationonNavMenu() {
//        imgNotification.setVisibility(View.VISIBLE);
        imgICBack.setVisibility(View.GONE);

        txtToolbartext.setText("Notifcations ");
        showFragment(NotificationFrgmenst.newInstance(this), NotificationFrgmenst.TAG);

    }
    private void showChangePassword() {
        imgICBack.setVisibility(View.GONE);

        txtToolbartext.setText("Change Password ");
        showFragment(ChangePassword.newInstance(this), ChangePassword.TAG);

    }
    private void showMyOrderFrg() {
        imgICBack.setVisibility(View.GONE);

        txtToolbartext.setText("My Orders");
        showFragment(new MyOrderFrg(), MyOrderFrg.TAG);

    }

    private void logoutDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new SavePreferences().savePreferencesData(MainActivity.this, "", AppConstants.logindata);
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                finishAffinity();

            }
        });
        builder.show();
    }

     Dialog dialog;
    public  void showSettingsAlert()
    {
           if(dialog!=null&&dialog.isShowing())
            {
              dialog.dismiss();
            }
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.showlocationdialog);
            dialog.show();

            dialog.findViewById(R.id.txtNext).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });



    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
           boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
           if (!isGPSEnabled)
            {
                showSettingsAlert();
            }



        }
    };


}

