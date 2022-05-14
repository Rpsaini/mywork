package com.markrap.base;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.app.preferences.SavePreferences;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.markrap.R;
import com.markrap.communication.CallBack;
import com.markrap.communication.ImageAttendanceCallBack;
import com.markrap.util.AppUtils;
import com.markrap.utility.AppConstants;
import com.markrap.utility.PrefHelper;
import com.markrap.view.RoundedImageView;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;


public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final int IMAGE_REQUEST_GALLERY = 328;
    public static final int IMAGE_REQUEST_CAMERA = 329;
//    private static final String PROFILE_IMAGE = "PROFILE_IMAGE";
    String imagePathUrl;
    File file;
    private Uri cameraImageUri;
    RoundedImageView attendanceUserProfile;
    ImageAttendanceCallBack imageAttendanceCallBack;

    LinearLayout llShowbutton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        setUp();
        PrefHelper.initInstance(this); // init PrefHelper to store some constants in preferences.

    }

    @Override
    protected void onDestroy() {

        hideLoading();
        super.onDestroy();
    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = AppUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (isLoading()) {
            mProgressDialog.cancel();
        }
    }

    public boolean isLoading() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    protected void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = sbView
                .findViewById(R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        snackbar.show();
    }

    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    public void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void setUpStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = 1024;
        decorView.setSystemUiVisibility(uiOptions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O && Build.VERSION.SDK_INT != Build.VERSION_CODES.O_MR1) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    protected abstract void setUp();

    @LayoutRes
    protected abstract int setLayout();



    public   void  showErrorDialog(String msg)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.showerror_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView txtError=dialog.findViewById(R.id.txtError);
        txtError.setText(msg);

        dialog.findViewById(R.id.txtNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    public   void  confirmWithBack(String msg,CallBack callBack)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.showerror_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        TextView txtError=dialog.findViewById(R.id.txtError);
        txtError.setText(msg);

        dialog.findViewById(R.id.txtNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callBack.getRespone("true",null);
            }
        });

        dialog.show();
    }

    public   void  confirmBox(String msg,String type, CallBack callBack, ImageAttendanceCallBack imageAttendanceCallBack)
    {

        this.imageAttendanceCallBack=imageAttendanceCallBack;



            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.confirmbox);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            RelativeLayout relativeCheckOutimage = dialog.findViewById(R.id.relativeImage);
            attendanceUserProfile = dialog.findViewById(R.id.attendanceUserProfile);
            llShowbutton = dialog.findViewById(R.id.llShowbutton);
            TextView txtVehicleImage = dialog.findViewById(R.id.txtVehicleImage);


            relativeCheckOutimage.findViewById(R.id.relativeImage).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkPermission(imageAttendanceCallBack);
                }
            });

            TextView txtError = dialog.findViewById(R.id.txtError);
            txtError.setText(msg);

            TextView txtYes = dialog.findViewById(R.id.txtyes);
            TextView txtNo = dialog.findViewById(R.id.txtNo);
            if (type.equalsIgnoreCase("outlet")) {
                txtVehicleImage.setText("Take Outlet Image");
                txtYes.setText("Visit Done");
                txtNo.setText("Not Visited");

            }


            txtYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    callBack.getRespone("true", null);
                }
            });

            txtNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    callBack.getRespone("false", null);
                }
            });


            dialog.show();
        }






    public String getLoginData(String dataType) {
       try {
           JSONObject data = new JSONObject(new SavePreferences().reterivePreference(this, AppConstants.logindata).toString());
           return data.getString(dataType);

       } catch (Exception e) {
           e.printStackTrace();
       }

            return  "";
   }


    public void checkPermission(ImageAttendanceCallBack imageAttendanceCallBack)
    {
        this.imageAttendanceCallBack=imageAttendanceCallBack;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            cameraIntent();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraIntent();
            }
        }
    }

//    public void openDailogForImagePickOption() {
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View dialogView = inflater.inflate(R.layout.layout_popup_image_option, null, false);
//        final Dialog dialog = new Dialog(this);
//        RelativeLayout relativeLayoutCamera = (RelativeLayout) dialogView.findViewById(R.id.relativeBlockCamera);
//    //    RelativeLayout relativeLayoutGallery = (RelativeLayout) dialogView.findViewById(R.id.relativeBlockGallery);
//
//        relativeLayoutCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cameraIntent();
//                dialog.dismiss();
//            }
//        });
//
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setContentView(dialogView);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
//    }
    private void cameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileCamera = new File(getExternalFilesDir("MarkApp"), System.currentTimeMillis()+"".concat(".jpeg"));
        cameraImageUri = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", fileCamera);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
        startActivityForResult(cameraIntent, IMAGE_REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                cropImage(cameraImageUri);
            }

        } else if (requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Log.i("@@resulturi",""+resultUri);
            if (resultUri != null && attendanceUserProfile!=null) {
                imageAttendanceCallBack.getImageUri(resultUri);
                Glide.with(this)
                        .load(resultUri)
                        .placeholder(R.drawable.side_image)
                        .centerCrop()
                        .into(attendanceUserProfile);

                llShowbutton.setVisibility(View.VISIBLE);
                imageAttendanceCallBack.getImageUri(resultUri);

            }
            else
            {
                imageAttendanceCallBack.getImageUri(resultUri);
            }
        }

    }

    private void cropImage(Uri uri) {
        File fileCamera = new File(getExternalFilesDir("MarkApp"), System.currentTimeMillis()+"out.jpeg");
        Uri outCamera = Uri.fromFile(fileCamera);
        UCrop.of(uri, outCamera)
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .start( this);
    }


}
