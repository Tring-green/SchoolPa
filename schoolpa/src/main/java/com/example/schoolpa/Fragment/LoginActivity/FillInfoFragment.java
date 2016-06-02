package com.example.schoolpa.fragment.LoginActivity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.schoolpa.activity.MainActivity;
import com.example.schoolpa.domain.Account;
import com.example.schoolpa.domain.BackTask;
import com.example.schoolpa.domain.NetTask;
import com.example.schoolpa.R;
import com.example.schoolpa.service.BackgroundService;
import com.example.schoolpa.utils.BackTaskFactory;
import com.example.schoolpa.utils.CommonUtil;
import com.example.schoolpa.utils.DirUtil;
import com.example.schoolpa.utils.SerializableUtil;
import com.example.schoolpa.utils.ToastUtils;
import com.example.schoolpa.widget.DialogChooseImage;
import com.example.schoolpa.db.AccountDao;
import com.example.schoolpa.db.BackTaskDao;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class FillInfoFragment extends Fragment {

    private final static int REQUEST_CODE_GALLERY = 0x11;
    private final static int REQUEST_CODE_CAMERA = 0x12;
    private final static int REQUEST_CODE_CROP = 0x13;
    private int crop = 200;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private ImageView mIv_icon;
    private Button mBt_clear;

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private Button mBt_ok;
    private View mView;
    private EditText mEt_name;

    public FillInfoFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_fill_info, container, false);
        verifyStoragePermissions(getActivity());
        initData();
        initView();
        return mView;
    }

    private AccountDao mDao;
    private Account mAccount;
    private String mIconDir;
    private File mSdcardTempFile;
    private String TAG = "SignUpFra";

    private void initData() {
        mDao = new AccountDao(getActivity());
        mAccount = mDao.getCurrentAccount();
        if (mAccount != null) {
            mIconDir = DirUtil.getIconDir(getActivity());
            mSdcardTempFile = new File(mIconDir, CommonUtil.string2MD5(mAccount.getUserId()));
            if (!mSdcardTempFile.getParentFile().exists()) {
                mSdcardTempFile.getParentFile().mkdirs();
            }
            mAccount.setIcon(mSdcardTempFile.getAbsolutePath());
        }
    }


    private void initView() {
        mEt_name = (EditText) mView.findViewById(R.id.fill_info_et_name);
        mBt_ok = (Button) mView.findViewById(R.id.fill_info_btn_ok);
        mBt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    String name = mEt_name.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        ToastUtils.showTestShort(getActivity(), "名字不能为空");
                    }

                    mAccount.setName(name);
                    mDao.updateAccount(mAccount);


                    // 添加name任务
                    addNameTask();

                    startActivity(new Intent(getActivity(), MainActivity.class));
                    getActivity().finish();

                }
            }
        });

        mIv_icon = (ImageView) mView.findViewById(R.id.fill_info_iv_icon);
        mIv_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogChooseImage dialog = new DialogChooseImage(getActivity());
                dialog.show();

                dialog.setClickCameraListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getImageFromCamera();
                        dialog.dismiss();
                    }
                });
                dialog.setClickGalleryListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getimageFromGallery();
                        dialog.dismiss();
                    }
                });
            }
        });

        mBt_clear = (Button) mView.findViewById(R.id.fill_info_btn_clear_name);
        mBt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEt_name.setText("");
            }
        });
    }

    protected void getImageFromCamera() {
        Uri uri = Uri.fromFile(mSdcardTempFile);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", uri);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    protected void getimageFromGallery() {

        Intent intent = new Intent("android.intent.action.PICK");
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                "image/*");
        intent.putExtra("output", Uri.fromFile(mSdcardTempFile));
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", crop);// 输出图片大小
        intent.putExtra("outputY", crop);
        startActivityForResult(intent, REQUEST_CODE_GALLERY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_GALLERY: {
                performImageBack();
                break;
            }
            case REQUEST_CODE_CAMERA: {
                Uri uri = Uri.fromFile(mSdcardTempFile);
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(uri, "image/*");
                intent.putExtra("output", uri);
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);// 裁剪框比例
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", crop);// 输出图片大小
                intent.putExtra("outputY", crop);
                startActivityForResult(intent, REQUEST_CODE_CROP);
                break;
            }
            case REQUEST_CODE_CROP: {
                performImageBack();
                break;
            }
            default:
                break;
        }
    }

    private void performImageBack() {
        String path = mSdcardTempFile.getAbsolutePath();
        Bitmap bmp = BitmapFactory.decodeFile(path);
        mIv_icon.setImageBitmap(bmp);

        // 更新地址
        mAccount.setIcon(path);
        mDao.updateAccount(mAccount);

        // 添加icon上传任务
        addIconTask();
    }

    private void addIconTask() {
        File iconFile = new File(mSdcardTempFile.getAbsolutePath());

        if (!iconFile.exists()) {
            return;
        }

        // 存储到后台任务中
        String taskDir = DirUtil.getTaskDir(getActivity());
        String fileName = CommonUtil.string2MD5(mAccount.getUserId() + "_"
                + SystemClock.currentThreadTimeMillis());
        String path = new File(taskDir, fileName).getAbsolutePath();

        BackTask task = new BackTask();
        task.setOwner(mAccount.getUserId());
        task.setPath(path);
        task.setState(0);
        new BackTaskDao(getActivity()).addTask(task);

        NetTask netTask = BackTaskFactory.userIconChangeTask(mAccount.getIcon());

        try {
            // 写入到缓存
            SerializableUtil.write(netTask, path);

            // 开启后台服务
            getActivity().startService(
                    new Intent(getActivity(), BackgroundService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNameTask() {
        // 存储到后台任务中
        String taskDir = DirUtil.getTaskDir(getActivity());
        String fileName = CommonUtil.string2MD5(mAccount.getUserId() + "_"
                + SystemClock.currentThreadTimeMillis());
        String path = new File(taskDir, fileName).getAbsolutePath();

        BackTask task = new BackTask();
        task.setOwner(mAccount.getUserId());
        task.setPath(path);
        task.setState(0);
        new BackTaskDao(getActivity()).addTask(task);

        NetTask netTask = BackTaskFactory.userNameChangeTask(mAccount.getName());

        try {
            // 写入到缓存
            SerializableUtil.write(netTask, path);

            // 开启后台服务
            getActivity().startService(
                    new Intent(getActivity(), BackgroundService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
