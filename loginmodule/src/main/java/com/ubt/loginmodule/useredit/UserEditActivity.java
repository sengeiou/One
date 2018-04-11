package com.ubt.loginmodule.useredit;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.ShapedImageView;
import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BaseResponseModel;
import com.ubt.baselib.model1E.UserAllModel;
import com.ubt.baselib.model1E.UserModel;
import com.ubt.baselib.mvp.MVPBaseActivity;
import com.ubt.baselib.utils.FileUtils;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.MyTextWatcher;
import com.ubt.baselib.utils.NameLengthFilter;
import com.ubt.baselib.utils.PermissionUtils;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.baselib.utils.http1E.OkHttpClientUtils;
import com.ubt.globaldialog.customDialog.loading.LoadingDialog;
import com.ubt.loginmodule.LoginHttpEntity;
import com.ubt.loginmodule.R;
import com.ubt.loginmodule.R2;
import com.ubt.loginmodule.requestModel.UpdateUserInfoRequest;
import com.vise.log.ViseLog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class UserEditActivity extends MVPBaseActivity<UserEditContract.View, UserEditPresenter> implements UserEditContract.View, MyTextWatcher.WatcherListener {


    @BindView(R2.id.img_head)
    ShapedImageView mImgHead;
    @BindView(R2.id.edit_user_name)
    EditText mTvUserName;
    @BindView(R2.id.male)
    RadioButton mMale;
    @BindView(R2.id.female)
    RadioButton mFemale;
    @BindView(R2.id.radio_group_sex)
    RadioGroup mRadioGroupSex;
    @BindView(R2.id.tv_user_age)
    TextView mTvUserAge;
    @BindView(R2.id.tv_user_grade)
    TextView mTvUserGrade;
    @BindView(R2.id.iv_main_back)
    ImageView mIvMainBack;
    @BindView(R2.id.tv_main_title)
    TextView mTvMainTitle;
    @BindView(R2.id.iv_complete_info)
    ImageView ivSave;

    private Dialog mDialog;

    private Uri mImageUri;
    public static final int GetUserHeadRequestCodeByShoot = 1001;
    public static final int GetUserHeadRequestCodeByFile = 1002;


    private String[] greadeList = new String[]{"幼儿园小班", "幼儿园中班", "幼儿园大班", "小学一年级", "小学二年级", "小学三年级", "小学四年级"
            , "小学五年级", "小学六年级及以上"};

    private List<String> ageList = new ArrayList<>();
    private List<String> gradeList = new ArrayList<>();
    private String sex = "1";
    private String age;
    private String grade;
    private String path;

    private UserModel mUserModel;
    private static final String TAG = "UserEditActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initUI();
    }


    @Override
    public void onResume() {
        super.onResume();
        mUserModel = (UserModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        ViseLog.d( "mUserModel:" + mUserModel.toString());

        InputFilter[] filters = { new NameLengthFilter(20) };
        mTvUserName.setFilters(filters);
        mTvUserName.addTextChangedListener(new MyTextWatcher(mTvUserName, this));
        if(mTvUserName.getText().toString().length()==0){
            String name = FileUtils.utf8ToString(mUserModel.getNickName());
            mTvUserName.setText(name);
            mTvUserName.setSelection(mTvUserName.getText().length());
        }
        if(TextUtils.isEmpty(age)){
            mTvUserAge.setText(TextUtils.isEmpty(mUserModel.getAge())?"未填写":mUserModel.getAge());
        }
        if(TextUtils.isEmpty(grade)){
            mTvUserGrade.setText(TextUtils.isEmpty(mUserModel.getGrade())?"未填写":mUserModel.getGrade());
        }

        if(TextUtils.isEmpty(path)){
            Glide.with(this).load(mUserModel.getHeadPic()).centerCrop().placeholder(R.drawable.login_sec_action_logo).into(mImgHead);
        }

        checkSaveEnable();


        mPresenter.getLoopData();

    }

    private void checkSaveEnable(){
        if(mTvUserName.getText().toString().trim().length()>0 && !mTvUserName.getText().toString().trim().equals("") && !mTvUserAge.getText().toString().equals("未填写")
                && !mTvUserGrade.getText().toString().equals("未填写")){
            ivSave.setEnabled(true);
        }else{
            ivSave.setEnabled(false);
        }
    }

    /**
     * 性别选中事件
     *
     * @param view
     * @param ischanged
     */
    @OnCheckedChanged({R2.id.male, R2.id.female})
    public void onRadioCheck(CompoundButton view, boolean ischanged) {
        int i = view.getId();
        if (i == R.id.male) {
            if (ischanged) {
                sex = "1";
            }

        } else if (i == R.id.female) {
            if (ischanged) {
                sex = "2";
            }

        }
    }

    @OnClick({R2.id.img_head, R2.id.tv_user_age, R2.id.tv_user_grade, R2.id.iv_complete_info})
    public void onClickView(View view) {
        int viewId = view.getId();
        if(viewId == R.id.img_head){
            mPresenter.showImageHeadDialog(UserEditActivity.this);
        }else if(viewId == R.id.tv_user_age){
            if(ageList.size()==0){
                ToastUtils.showShort("网络异常");
                return;
            }
            mPresenter.showAgeDialog(UserEditActivity.this, ageList, mPresenter.getPosition(age,ageList));

        }else if(viewId == R.id.tv_user_grade){
            if(gradeList.size()==0){
                ToastUtils.showShort("网络异常");
                return;
            }
            mPresenter.showGradeDialog(UserEditActivity.this, mPresenter.getPosition(grade,gradeList), gradeList);

        }else if(viewId == R.id.iv_complete_info){
            LoadingDialog.show(UserEditActivity.this);
            UpdateUserInfoRequest request = new UpdateUserInfoRequest();
            request.setAge(age);
            request.setGrade(grade);
            request.setNickName(FileUtils.stringToUtf8(mTvUserName.getText().toString()));
            request.setSex(sex);
            File file = null;
            if(!TextUtils.isEmpty(path)){
                file = new File(path);
            }
            OkHttpClientUtils.getJsonByPostRequest(LoginHttpEntity.UPDATE_USERINFO, file, request, 11).execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ViseLog.d( "Exception:" + e.getMessage());
                    LoadingDialog.dismiss(UserEditActivity.this);
                    ToastUtils.showShort("用户信息保存失败");
                }

                @Override
                public void onResponse(String response, int id) {
                    ViseLog.d( "response:" + response);
                    LoadingDialog.dismiss(UserEditActivity.this);
                    BaseResponseModel<UserModel> baseResponseModel = GsonImpl.get().toObject(response,
                            new TypeToken<BaseResponseModel<UserModel>>() {
                            }.getType());
                    if (baseResponseModel.status) {
                        ViseLog.d( "userModel:" + baseResponseModel.models);
                        SPUtils.getInstance().saveObject(Constant1E.SP_USER_INFO, baseResponseModel.models);
                        ARouter.getInstance().build(ModuleUtils.Main_MainActivity).navigation();
                        UserEditActivity.this.finish();
                    }

                }
            });
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;//拦截事件传递,从而屏蔽back键。
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 拍照
     */
    @Override
    public void takeImageFromShoot() {
         // getShootCamera();
        //首先判断是否开启相机权限，如果开启直接调用，未开启申请
        PermissionUtils.getInstance()
                .request(new PermissionUtils.PermissionLocationCallback() {
                    @Override
                    public void onSuccessful() {
                       //  ToastUtils.showShort("申请拍照权限成功");
                        getShootCamera();
                    }

                    @Override
                    public void onFailure() {
                         //ToastUtils.showShort("申请拍照权限失败");
                    }

                    @Override
                    public void onRationSetting() {
                        // ToastUtils.showShort("申请拍照权限已经被拒绝过");
                    }

                    @Override
                    public void onCancelRationSetting() {
                    }


                }, PermissionUtils.PermissionEnum.CAMERA,this);

    }

    public void getShootCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String catchPath = FileUtils.getCacheDirectory(this, "");
        // File path = new File(FileTools.image_cache);
        File path = new File(catchPath + "/images");
        if (!path.exists()) {
            path.mkdirs();
        }
        mImageUri = Uri.fromFile(new File(path, System.currentTimeMillis() + ""));
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        cameraIntent.putExtra("return-data", true);
        startActivityForResult(cameraIntent, GetUserHeadRequestCodeByShoot);
    }
    /**
     * 从相册获取照片
     */
    @Override
    public void takeImageFromAblum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GetUserHeadRequestCodeByFile);
    }

    /**
     * 年龄选择滚动回调
     *
     * @param type 0表示Age 1表示grade
     * @param item 选择内容
     */
    @Override
    public void ageSelectItem(int type, String item) {
        if (type == 0) {
            age = item;
            mTvUserAge.setText(age);
            checkSaveEnable();
        } else if (type == 1) {
            grade = item;
            mTvUserGrade.setText(grade);
            checkSaveEnable();
        }
    }

    @Override
    public void updateUserModelSuccess(UserModel userModel) {

    }

    /**
     * 更新信息失败
     */
    @Override
    public void updateUserModelFailed(String str) {

    }

    @Override
    public void updateLoopData(UserAllModel userAllModel) {
        if (null != userAllModel) {
            ageList = userAllModel.getAgeList();
            gradeList = userAllModel.getGradeList();
        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GetUserHeadRequestCodeByFile
                || requestCode == GetUserHeadRequestCodeByShoot) {
            if (resultCode == RESULT_OK) {
                ContentResolver cr = this.getContentResolver();
                if (requestCode == GetUserHeadRequestCodeByFile) {
                    if (data == null) {
                        return;
                    }

                    String type = cr.getType(data.getData());
                    if (type == null) {
                        return;
                    }
                    mImageUri = data.getData();
                }
                try {
                    Bitmap bitmap = FileUtils.getBitmapFormUri(this, mImageUri);
                    mImgHead.setImageBitmap(bitmap);
                    path = FileUtils.SaveImage(this, "head", bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    protected void initUI() {


    }

    protected void initControlListener() {

    }

    protected void initBoardCastListener() {

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_login_user_edit;
    }

    @Override
    public void getAgeDataList(List<String> list) {

    }

    @Override
    public void longEditTextSize() {

    }

    @Override
    public void errorEditTextStr() {
        if(!mTvUserName.getText().toString().equals(mUserModel.getNickName())){
//            ToastUtils.showShort("仅限汉字、字母及数字");
        }

    }

    @Override
    public void textChange() {
        checkSaveEnable();
    }
}
