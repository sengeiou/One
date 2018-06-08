package com.ubt.en.alpha1e.action.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.ubt.baselib.BlueTooth.BTReadData;
import com.ubt.baselib.BlueTooth.BTServiceStateChanged;
import com.ubt.baselib.btCmd1E.BTCmd;
import com.ubt.baselib.btCmd1E.BluetoothParamUtil;
import com.ubt.baselib.btCmd1E.ProtocolPacket;
import com.ubt.baselib.btCmd1E.cmd.BTCmdActionDoDefault;
import com.ubt.baselib.btCmd1E.cmd.BTCmdActionStopPlay;
import com.ubt.baselib.btCmd1E.cmd.BTCmdCtrlAllEngine;
import com.ubt.baselib.btCmd1E.cmd.BTCmdCtrlOneEngineLostPower;
import com.ubt.baselib.btCmd1E.cmd.BTCmdEnterCource;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPlayAction;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPlaySound;
import com.ubt.baselib.btCmd1E.cmd.BTCmdPowerOff;
import com.ubt.baselib.btCmd1E.cmd.BTCmdReadAllEngine;
import com.ubt.baselib.btCmd1E.cmd.BTCmdSoundStopPlay;
import com.ubt.baselib.btCmd1E.cmd.BTCmdSwitchEditStatus;
import com.ubt.baselib.commonModule.ModuleUtils;
import com.ubt.baselib.customView.BaseBTDisconnectDialog;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.ByteHexHelper;
import com.ubt.bluetoothlib.base.BluetoothState;
import com.ubt.bluetoothlib.blueClient.BlueClientUtil;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.adapter.CourseItemAdapter;
import com.ubt.en.alpha1e.action.dialog.PrepareActionUtil;
import com.ubt.en.alpha1e.action.util.ActionCourseDataManager;
import com.ubt.htslib.base.NewActionInfo;
import com.vise.log.ViseLog;
import com.vise.utils.convert.HexUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ActionsEditHelper  {


    private Context mContext;

    public enum StartType {
        edit_type, new_type
    }

    public enum Command_type {
        Do_play, Do_pause_or_continue, Do_Stop
    }

    public final static String StartTypeStr = "StartTypeStr";
    public final static String New_ActionInfo = "NewActionInfo";

    public final static String MAP_FRAME = "MAP_FRAME";
    public final static String MAP_FRAME_NAME = "MAP_FRAME_NAME";
    public final static String MAP_FRAME_TIME = "MAP_FRAME_TIME";
    public final static String MAP_FRAME_SHOW_TIME = "MAP_FRAME_SHOW_TIME";

    public static final int SaveActionReq = 11001;
    public static final String SaveActionResult = "SaveActionResult";

    private static final int msg_on_read_all_eng = 1001;
    private static final int msg_on_change_action_finish = 1002;

    public static final int GetUserHeadRequestCodeByShoot = 1001;
    public static final int GetUserHeadRequestCodeByFile = 1002;
    public static final int GetThumbnailRequestCodeByVideo = 1008;

    private NewActionPlayer mNewPlayer;
    // private NewActionsManager mNewActionsManager;
    private IEditActionUI mUI;

    private BlueClientUtil mBlueClientUtil;


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == msg_on_read_all_eng) {
                mUI.onReadEng((byte[]) msg.obj);
            }

        }
    };


    public ActionsEditHelper(Context context, IEditActionUI _ui) {
        this.mContext = context;
        EventBus.getDefault().register(this);
        mBlueClientUtil = BlueClientUtil.getInstance();

        mUI = _ui;
        try {  //以下会报空错误，先try catch
            if (mNewPlayer == null) {
                mNewPlayer = NewActionPlayer
                        .getPlayer(mBlueClientUtil.getConnectedDevice().getAddress());
            }

        } catch (Exception e) {
            ViseLog.d("ActionsEditHelper", "e:" + e.getMessage());
        }

        if (mNewPlayer != null) {
            mNewPlayer.addListener(mUI);
        }

    }

    /**
     * 读取所有角度
     */
    public void doReadAllEng() {
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdReadAllEngine().toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }
    }

    //读取某个动作帧
    public void doCtrlAllEng(byte[] datas) {

        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdCtrlAllEngine(datas).toByteArray());
        }
//        else {
//            showBlutoohDisconnectDialog();
//        }
    }


    /**
     * 进入课程模式
     *
     * @param status 01表示进入 ，00表示离开
     */
    public void doEnterCourse(byte status) {
        ViseLog.d("ActionsEditHelper", "doEnterActionCourse status:" + status);
//        byte[] params = new byte[1];
//        params[0] = status;
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdEnterCource(status).toByteArray());
        } else {
            if (status == (byte) 1) {
                showBlutoohDisconnectDialog();
            }
        }
    }

    /**
     * 进入和退出动作编辑
     */
    public void doEnterOrExitActionEdit(byte status) {
//        byte[] params = new byte[2];
//        params[0] = status;
//        params[1] = 0;
//        doSendComm(ConstValue.DV_INTO_EDIT, params);
        ViseLog.d("ActionsEditHelper", "doEnterOrExitActionEdit status:" + status);
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdSwitchEditStatus(status).toByteArray());
        } else {
            if (status == (byte) 0x03) {
                showBlutoohDisconnectDialog();
            }
        }

    }


    /**
     * 读取蓝牙回调数据
     *
     * @param readData
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReadData(BTReadData readData) {
        //ViseLog.i("data:" + HexUtil.encodeHexStr(readData.getDatas()));
//        BTCmdHelper.parseBTCmd(readData.getDatas(), this);
        onProtocolPacket(readData);
    }

    /**
     * 蓝牙数据解析回调
     *
     * @param readData
     */
    private void onProtocolPacket(BTReadData readData) {
        ProtocolPacket packet = readData.getPack();
        int cmd = packet.getmCmd();
        byte[] param = packet.getmParam();
        // ViseLog.d("EditHelper", "cmd==" + cmd + "  params==" + ByteHexHelper.bytesToHexString(param));
        if (cmd == BTCmd.READ_ALL_ENGINE) {
            mUI.onReadEng(param);
        } else if (cmd == BTCmd.CTRL_ONE_ENGINE_LOST_POWER) {
            ViseLog.d("读取角度" + ByteHexHelper.bytesToHexString(param));
            Message msg = new Message();
            msg.what = msg_on_read_all_eng;
            msg.obj = param;
            mHandler.sendMessage(msg);
        } else if (cmd == BTCmd.DV_SET_PLAY_SOUND) {
            if (param != null) {
                ViseLog.d("EditHelper", "sound:" + ByteHexHelper.bytesToHexString(param) + "param[0]:" + param[0]);
                if (param[0] == 1) {
                    ViseLog.d("EditHelper", "播放完成");
                    if (mListener != null) {
                        mListener.playComplete();
                    }
                }
            }
        } else if (cmd == 0xE1) {
            ViseLog.d("EditHelper", "退出课程");
        } else if (cmd == BTCmd.DV_ACTION_FINISH)// 动作播放完毕
        {
            ViseLog.d("ActionEditHelper", "动作播放完成");
            if (mListener != null) {
                mListener.playComplete();
            }
        } else if (cmd == BTCmd.DV_TAP_HEAD) {
            ViseLog.d("EditHelper", "TAP_HEAD = " + cmd);
            if (mListener != null) {
                mListener.tapHead();
            }
        } else if (cmd == BTCmd.DV_INTO_EDIT) {
            if (param != null) {
                ViseLog.d("进入或退出动作编辑param:" + ByteHexHelper.bytesToHexString(param));
            }
        } else if (cmd == BTCmd.DV_READ_BATTERY) {
            ViseLog.i("电量data:" + HexUtil.encodeHexStr(packet.getmParam()));
            if(packet.getmParamLen() < 4){
                ViseLog.e("错误参数，丢弃!!!");
                return;
            }
            int power = packet.getmParam()[3];
            if (power <= 5) {
                if (mListener != null) {
                    mListener.lowerPower();
                }
            }
        }

    }

    /**
     * 蓝牙连接断开状态
     *
     * @param serviceStateChanged
     */
    @org.greenrobot.eventbus.Subscribe
    public void onBluetoothServiceStateChanged(BTServiceStateChanged serviceStateChanged) {
        ViseLog.i("getState:" + serviceStateChanged.toString());
        switch (serviceStateChanged.getState()) {
            case BluetoothState.STATE_CONNECTED://蓝牙配对成功
                ViseLog.d("蓝牙配对成功");
                break;
            case BluetoothState.STATE_CONNECTING://正在连接
                ViseLog.e("正在连接");
                break;
            case BluetoothState.STATE_DISCONNECTED:
                ViseLog.e("蓝牙连接断开");
                if (mListener != null) {
                    mListener.onDisconnect();
                }
                break;
            default:

                break;
        }
    }


    PlayCompleteListener mListener;

    public PlayCompleteListener getListener() {
        return mListener;
    }

    public void setListener(PlayCompleteListener listener) {
        mListener = listener;
    }

    public interface PlayCompleteListener {
        void playComplete();

        void onDisconnect();

        void tapHead();

        void lowerPower();
    }

    /**
     * 掉电
     */
    public void doLostPower() {
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdPowerOff().toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }
    }

    public void doLostOnePower(int id) {
        byte[] params = new byte[1];
        params[0] = ByteHexHelper.intToHexByte(id);
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdCtrlOneEngineLostPower(id).toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }
    }

    /**
     * 课程播放
     *
     * @param str
     */
    public void playCourse(String str) {
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdPlaySound(str).toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }
    }

    /**
     * 播放动作
     *
     * @param actionName
     */
    public void playAction(String actionName) {
        ViseLog.d("ActionEditHelper", " playAction actionName===" + actionName);
        byte[] actions = BluetoothParamUtil.stringToBytes(actionName);
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdPlayAction(actionName).toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }
    }

    /**
     * 结束动作
     */
    public void stopAction() {
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdActionStopPlay().toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }

    }

    /**
     * 播放音效
     *
     * @param params
     */

    public void playSoundAudio(String params) {
        ViseLog.d("playSoundAudio", "params = " + params);
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdPlaySound(params).toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }
    }

    /**
     * 停止音效
     */

    public void stopSoundAudio() {
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdSoundStopPlay().toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }
    }

    public void doLostLeftHandAndRead() {
        doLostOnePower(1);
        doLostOnePower(2);
        doLostOnePower(3);

    }

    public void doLostRightHandAndRead() {
        doLostOnePower(4);
        doLostOnePower(5);
        doLostOnePower(6);
    }

    public void doLostLeftFootAndRead() {
        doLostOnePower(7);
        doLostOnePower(8);
        doLostOnePower(9);
        doLostOnePower(10);
        doLostOnePower(11);

    }

    public void doLostRightFootAndRead() {
        doLostOnePower(12);
        doLostOnePower(13);
        doLostOnePower(14);
        doLostOnePower(15);
        doLostOnePower(16);
    }

    public NewActionPlayer.PlayerState getNewPlayerState() {
        if (this.mNewPlayer == null) {
            return NewActionPlayer.PlayerState.STOPING;
        } else {
            return this.mNewPlayer.getState();
        }
    }

    public void doActionCommand(Command_type comm_type, NewActionInfo action) {

        if (mBlueClientUtil.getConnectionState() != 3) {
            PrepareActionUtil.dismiss();
            showBlutoohDisconnectDialog();
            return;
        }

        if (comm_type == Command_type.Do_play) {
            ViseLog.d("Play Action " + action.toString());
            mNewPlayer.PlayAction(action, mContext);

        } else if (comm_type == Command_type.Do_pause_or_continue) {
            ViseLog.d("Do_pause_or_continue ");
            if (mNewPlayer.getState() != NewActionPlayer.PlayerState.STOPING) {
                if (mNewPlayer.getState() == NewActionPlayer.PlayerState.PAUSING) {
                    mNewPlayer.ContinuePlayer();
                } else if (mNewPlayer.getState() == NewActionPlayer.PlayerState.PLAYING) {
                    mNewPlayer.PausePalyer();
                }
            }
        } else if (comm_type == Command_type.Do_Stop) {
            if (mNewPlayer.getState() != NewActionPlayer.PlayerState.STOPING) {
                mNewPlayer.StopPlayer();
            }

        }

    }


    /**
     * 复位动作
     **/
    public void doDefaultActions() {
        byte[] param = new byte[1];
        param[0] = 0;
        if (mBlueClientUtil.getConnectionState() == 3) {
            mBlueClientUtil.sendData(new BTCmdActionDoDefault(param).toByteArray());
        } else {
            showBlutoohDisconnectDialog();
        }
    }

    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * 显示蓝牙掉线对话框
     */
    public void showBlutoohDisconnectDialog() {
        if (!BaseBTDisconnectDialog.getInstance().isShowing()) {
            BaseBTDisconnectDialog.getInstance().show(new BaseBTDisconnectDialog.IDialogClick() {
                @Override
                public void onConnect() {
                    ARouter.getInstance().build(ModuleUtils.Bluetooh_BleStatuActivity).navigation();
                    BaseBTDisconnectDialog.getInstance().dismiss();
                }

                @Override
                public void onCancel() {
                    BaseBTDisconnectDialog.getInstance().dismiss();
                }
            });
        }
    }

    /**
     * 显示下一课对话框
     *
     * @param context
     * @param course
     * @param level
     * @param clickListener
     */
    public void showNextDialog(Context context, int course, int level, final ClickListener clickListener) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_action_course_content, null);
        TextView title = contentView.findViewById(R.id.tv_card_name);
        //title.setText(getCourseDialogTitleLevel(course));
        title.setVisibility(View.INVISIBLE);
        TextView tvContent = contentView.findViewById(R.id.tv_card_content);
        tvContent.setText(getCourseDialogTitle(course));

        Button button = contentView.findViewById(R.id.btn_pos);
        button.setText(SkinManager.getInstance().getTextById(R.string.actions_lesson_next_part));

        RecyclerView mrecyle = contentView.findViewById(R.id.recyleview_content);
        mrecyle.setLayoutManager(new LinearLayoutManager(context));

        CourseItemAdapter itemAdapter = new CourseItemAdapter(R.layout.layout_action_course_dialog, ActionCourseDataManager.getCourseActionModel(course, level));
        mrecyle.setAdapter(itemAdapter);

        ViewHolder viewHolder = new ViewHolder(contentView);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int screenHeight = (int) (display.getHeight() * 0.7);
        int screenWidth = (int) (display.getWidth() * 0.7);
        int width = Math.max(screenWidth, screenHeight); //设置宽度

        DialogPlus.newDialog(context)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.CENTER)
                .setContentWidth(width)
                .setContentBackgroundResource(R.drawable.action_dialog_filter_rect)
                .setOnClickListener(new com.orhanobut.dialogplus.OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == R.id.btn_pos) {
                            if (clickListener != null) {
                                clickListener.confirm();
                            }
                            dialog.dismiss();
                        }
                    }
                })
                .setCancelable(false)
                .create().show();
    }

    public interface ClickListener {
        void confirm();
    }

    /**
     * 课程列表关卡名称
     *
     * @param course
     * @return
     */
    public static String getCourseDialogTitle(int course) {
        String title = "";
        if (course == 1) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_1);
        } else if (course == 2) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_2);
        } else if (course == 3) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_3);
        } else if (course == 4) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_4);
        } else if (course == 5) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_5);
        } else if (course == 6) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_6);
        } else if (course == 7) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_7);
        } else if (course == 8) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_8);
        } else if (course == 9) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_9);
        } else if (course == 10) {
            title = SkinManager.getInstance().getTextById(R.string.actions_lesson_10);
        }
        return title;
    }

    /**
     * 课程列表
     *
     * @param course
     * @return
     */
    public static String getCourseDialogTitleLevel(int course) {
        String title = "";
//        if (course == 1) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_1);
//        } else if (course == 2) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_2);
//        } else if (course == 3) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_3);
//        } else if (course == 4) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_4);
//        } else if (course == 5) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_5);
//        } else if (course == 6) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_6);
//        } else if (course == 7) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_7);
//        } else if (course == 8) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_8);
//        } else if (course == 9) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_9);
//        } else if (course == 10) {
//            title = SkinManager.getInstance().getTextById(R.string.action_level_10);
//        }
        String sAgeFormat = SkinManager.getInstance().getTextById(R.string.actions_lesson_list);
        return String.format(sAgeFormat, course);
    }
}
