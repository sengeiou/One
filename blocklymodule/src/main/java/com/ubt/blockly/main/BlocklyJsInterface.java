package com.ubt.blockly.main;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.ubt.baselib.globalConst.Constant1E;
import com.ubt.baselib.model1E.BlocklyProjectMode;
import com.ubt.baselib.model1E.UserInfoModel;
import com.ubt.baselib.utils.ByteHexHelper;
import com.ubt.baselib.utils.GsonImpl;
import com.ubt.baselib.utils.SPUtils;
import com.ubt.blockly.main.bean.BlocklyEvent;
import com.ubt.blockly.main.bean.RobotSensor;
import com.ubt.blockly.main.bean.SensorObservable;
import com.ubt.blockly.main.bean.SensorObserver;
import com.ubt.blockly.main.bean.WalkDirectionEnum;
import com.ubt.blockly.main.bean.WalkSpeedEnum;
import com.vise.log.ViseLog;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @className BlocklyJsInterface
 *
 * @author wmma
 * @description android端和js交互接口
 * @date  2017/03/06
 * @update
 */


public class BlocklyJsInterface {

    private String TAG = "BlocklyJsInterface";

    private Activity mBaseActivity;
    private String mActions = "";

    public static final String FORWARD = "forward";
    public static final String BACK = "back";
    public static final String LEFT = "left";
    public static final String RIGHT= "right";
    public static final String FAST = "fast";
    public static final String MID = "mid";
    public static final String SLOW = "slow";



    public BlocklyJsInterface(Activity baseActivity) {
        mBaseActivity = baseActivity;
    }

    /**
     * 向js传递内置动作列表
     *
     * @return 返回动作列表，每个动作之间以&来拼接，若未连接机器人则传递"";
     */
    @JavascriptInterface
    public String setActionParams() {
        ViseLog.d(TAG, "setActionParams");
        mActions = "";
        return parseActionList();
    }

    private String parseActionList() {

        List<String> names = ((BlocklyActivity) mBaseActivity).getActionList();
        ViseLog.d(TAG, "parseActionList=" + names);
        if (names != null && names.size() > 0) {
            for (int i = 0; i < names.size(); i++) {
                String actionName = names.get(i);
                if (actionName.startsWith("@") || actionName.startsWith("#") || actionName.startsWith("%")) {
                    actionName = actionName.substring(1);
                }
                mActions += actionName + "&";
            }
            ViseLog.d(TAG, "mActions=" + mActions);
        }else{
            ViseLog.d(TAG, "mActions=" + mActions);
            return mActions;
        }
        return mActions.substring(0, mActions.length()-1);
    }

    /**
     * jimu项目遗留的接口，初始化时需要，后续需要web端屏蔽此方法。
     *
     * @return
     */
    @JavascriptInterface
    public String getServoID() {
        return "&servos=1|2|3";
    }

    /**
     * 沿用jimu的机器人执行命令接口
     *
     * @param params 动作名称
     */
    @JavascriptInterface
    public void executeByCmd(String params) {
        String action = params;
        ViseLog.d(TAG, "params:" + params);
        ((BlocklyActivity) mBaseActivity).playRobotAction(action);

    }


    /**
     * 用于回调给js动作是否执行完成， 该接口已废弃, 目前用于和js做调试使用。
     *
     * @return true 执行结束 false 未结束
     */
    @Deprecated
    @JavascriptInterface
    public boolean actionState() {
//        boolean state = ((BlocklyActivity) mBaseActivity).getPlayFinishState();
        return true;
    }

    /**
     * 用户停止执行blockly程序时停止机器人播放。
     */
    @JavascriptInterface
    public void stopRobot() {
        ViseLog.d(TAG, "stopRobot");
        ((BlocklyActivity) mBaseActivity).stopPlay();
    }



    /**
     * 关闭blockly逻辑编程页面
     */
    @JavascriptInterface
    public void closeBlocklyWindow() {
        ViseLog.d(TAG, "closeBlocklyWindow");
        ((BlocklyActivity) mBaseActivity).finish();
//        ((BlocklyActivity) mBaseActivity).overridePendingTransition(0, R.anim.activity_close_down_up);
//        ((BlocklyActivity) mBaseActivity).startOrStopRun((byte)0x02);

    }

    /**
     * js请求连接蓝牙
     */
    @JavascriptInterface
    public void bluetoothConnect() {
        ViseLog.d(TAG, "js call bluetoothConnect");
        ((BlocklyActivity) mBaseActivity).connectBluetooth();

    }


    /**
     * js获取当前蓝牙的连接状态
     *
     * @return true 连接， false 未连接
     */
    @JavascriptInterface
    public boolean getBluetoothState() {
//        ViseLog.d(TAG, "getBluetoothState=" + ((BlocklyActivity) mBaseActivity).isBulueToothConnected());
        return ((BlocklyActivity) mBaseActivity).isBlueToothConnected();
    }

    /**
     * 连接蓝牙的情况下跳转到机器人信息页面
     */

    @JavascriptInterface
    public void showRobotInfo() {
        ViseLog.d(TAG, "showRobotInfo");
//        RobotInfoActivity.launchActivity(mBaseActivity, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((BlocklyActivity) mBaseActivity).connectBluetooth();
    }


    /**
     * js获取手机/平板状态接口
     */
    @JavascriptInterface
    public int matchPhoneDirection(String direction) {
        ViseLog.d(TAG, "direction=" + direction);

        if (((BlocklyActivity) mBaseActivity).getmDirection().equalsIgnoreCase(direction)) {
            return 1;
        } else {
            return 0;
        }

    }


    /**
     * js获取机器人是否低电量接口
     *
     * @return
     */
    @JavascriptInterface
    public boolean lowBatteryState() {

        return false/*((BlocklyActivity) mBaseActivity).getBatteryState()*/;
    }

    /**
     * 显示表情接口
     *
     * @param params 表情ID
     */

    @JavascriptInterface
    public void showEmoji(String params) {
        //TODO 让机器人执行相应的表情动作
        ViseLog.d("showEmoji params=" + params);
        ((BlocklyActivity) mBaseActivity).showEmoji(params);
    }

    /**
     * 播放音效
     */

    @JavascriptInterface
    public void playAudio(String params) {
        ViseLog.d("playAudio params=" + params);
        ((BlocklyActivity) mBaseActivity).playSoundAudio(params);
    }


    /**
     * displayWalk, 行走模块
     *
     * @param params 参数以 " , " 分割方向、速度、时间
     */

    @JavascriptInterface
    public void displayWalk(String params) {
//        forward,fast,10
        ViseLog.d(TAG, "params:" + params);
        if (params != "" && params != null) {
            String direction = params.split(",")[0];
            String speed = params.split(",")[1];
            String time = params.split(",")[2];
           ((BlocklyActivity) mBaseActivity).doWalk(parseDirection(direction),parseSpeed(speed), ByteHexHelper.intToFourHexBytes(Integer.valueOf(time)));
        }

    }

    private byte parseDirection(String direct) {
        if(direct.equals(FORWARD)){
            return WalkDirectionEnum.FORWARD.getValue();
        }else if(direct.equals(BACK)){
            return WalkDirectionEnum.BACK.getValue();
        }else if(direct.equals(LEFT)){
            return  WalkDirectionEnum.LEFT.getValue();
        }else if(direct.equals(RIGHT)) {
            return WalkDirectionEnum.RIGHT.getValue();
        }
        return 0;
    }

    private byte parseSpeed(String speed) {
        if(speed.equals(FAST)){
            return WalkSpeedEnum.FAST.getValue();
        }else if(speed.equals(MID)) {
            return WalkSpeedEnum.MID.getValue();
        }else if(speed.equals(SLOW)){
            return  WalkSpeedEnum.SLOW.getValue();
        }
        return 1;
    }


    /**
     * 保存block程序
     *
     * @param xml js传递的参数
     */

    @JavascriptInterface
    public void saveXmlProject(String xml) {
        ViseLog.d(TAG, "xml=" + xml.toString());
    }


    /**
     * js获取用户信息
     *
     * @return 当前登录用户信息ID
     */

    @JavascriptInterface
    public String getUserID() {
        ViseLog.d(TAG, "getUserID");
        UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
        return userInfoModel.getUserId();
    }


    /**
     * 传感器相关接口
     *
     * @param params js传递给android端的数据
     */

    @JavascriptInterface
    public void registerSensorObservers(String params) {
//        [{"sensorType":"phone","operator":"EQ","value":"left","sensorId":"0","branchId":"5","callback":"sensorCallback"}]
        ViseLog.d(TAG, "params=" + params.toString());
        JSONObject jsonObject = null;
        try {
            JSONArray jsonArray = new JSONArray(params);
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject != null) {
                        SensorObserver sensorObserver = GsonImpl.get().toObject(jsonObject.toString(), SensorObserver.class);
                        SensorObservable.getInstance().addObserver(sensorObserver);
                        if (jsonObject.get("sensorType").equals(RobotSensor.SensorType.INFRARED)) {
                            EventBus.getDefault().post(new BlocklyEvent(BlocklyEvent.CALL_START_INFRARED));
                        }
                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 移除所有的传感器观察者
     */
    @JavascriptInterface
    public void unRegisterAllSensorObserver() {
        ViseLog.d(TAG, "unRegisterAllSensorObserver");
        SensorObservable.getInstance().deleteObservers();
        SensorObservable.getInstance().clearActiveObserver();
    }


    /**
     * registerEventObservers js调用的所有事件的方法
     *
     * @param params
     * @return true or false
     * <p>
     * 机器人跌倒传var ifreamParam = {"sensorType":"fallDown", "value":""};
     * 电量查询var ifreamParam = {"sensorType":"lowBatter", "value":""};
     * 红外数据格式 {"sensorType":"infraredDistance","value":"10","symbol":"greater"}
     * {"sensorType":"robotState", "value":"pick_up"}
     * var robotStrArr = ["pick_up","gravity_forward","gravity_after","strike_front","strike_after"];
     * 机器人加速度{"sensorType":"robotAddSpeed", "value":"6",direction":"up,"sysmbol":"LT"}
     */

    @JavascriptInterface
    public void registerEventObservers(String params) {
        ViseLog.d("registerEventObservers params=" + params);
        try {
            JSONObject jsonObject = new JSONObject(params);
            String sensorType = jsonObject.getString("sensorType");
            if (sensorType.equalsIgnoreCase((EventType.lowBatter).toString())) {
//                ((BlocklyActivity) mBaseActivity).checkBattery();
            } else if (sensorType.equalsIgnoreCase((EventType.fallDown).toString())) {
                ((BlocklyActivity) mBaseActivity).checkRobotFall();
            } else if (sensorType.equalsIgnoreCase((EventType.infraredDistance).toString())) {
//                ((BlocklyActivity) mBaseActivity).startInfrared(params);
            } else if (sensorType.equalsIgnoreCase("robotState")) {
                String value = jsonObject.getString("value");
            } else if (sensorType.equalsIgnoreCase("robotAddSpeed")) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 给js传递语音数据列表
     *
     * @return 获取的语音数据，以json的格式传递
     */

    @JavascriptInterface
    public String setOfflineParams() {
        ViseLog.d(TAG, "setOfflineParams");
        // 以下模拟数据供调试，待机器人本地完善后获取真实数据处理。
        return "{\"action\":[{\"param\":\"你好\"},{\"param\":\"天天向上\"},{\"param\":\"回眸一笑百媚生\"}],\"emotion\":[{\"param\":\"开心\"},{\"param\":\"操蛋\"}],\"answer\":[{\"param\":\"搞事啊！\"},{\"param\":\"操蛋\"}]}";
    }

    @JavascriptInterface
    public void path() {

    }


    // jimu遗留
    @JavascriptInterface
    public void customSoundList() {

    }

    @JavascriptInterface
    public void stopPlayAudio() {
        ViseLog.d(TAG, "stopPlayAudio");
        ((BlocklyActivity) mBaseActivity).stopPlayAudio();
    }


    //眼镜灯光
    // ['red','orange','yellow','green','cyan','blue','purple','white'];
    @JavascriptInterface
    public void setEyeStatusParameter(String params) {
        ViseLog.d(TAG, "setEyeStatusParameter params=" + params);
        ((BlocklyActivity) mBaseActivity).setLedLight(params);

    }


    /**
     * TTS 语音播报
     *
     * @param tts
     */

    @JavascriptInterface
    public void playTTS(String tts) {
        ViseLog.d(TAG, "playTTS:" + tts);
//        ((BlocklyActivity) mBaseActivity).playTTSFinish();
    }



    @JavascriptInterface
    public void login() {
        ViseLog.d(TAG, "login");
//        ((BlocklyActivity) mBaseActivity).login();
    }

    //读取机器人硬件版本
    @JavascriptInterface
    public String getRobotHardVersion() {
        ViseLog.d(TAG, "getRobotHardVersion");
        return ""/*((AlphaApplication) mBaseActivity.getApplicationContext()).getRobotHardVersion()*/;
    }

    //左转右转
    //{"direction":"left","angle":"270"}

    @JavascriptInterface
    public void movementTurnParams(String params) {
        ViseLog.d(TAG, "turn params:" + params);
        try {
            JSONObject jsonObject = new JSONObject(params);
            String direction = jsonObject.getString("direction");
            String angle = jsonObject.getString("angle");
            ViseLog.d(TAG, "direction:" + direction + "angle:" + angle);

            int count = 1;
            if (isStringNumber(angle)) {
                count = Integer.parseInt(angle) / 45;
            }
            ViseLog.d(TAG, "turn params count : " + count);
//            ((BlocklyActivity) mBaseActivity).playRobotAction(direction, true, count + "", true);
            //TODO 通知机器人执行左转右转
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //播放音乐
    @JavascriptInterface
    public void playMusic(String params) {
//        {"filename":"啊啊啊.mp3","playStatus":1,"uploadStatus":0}
        ViseLog.d(TAG, "playMusic params=" + params);
        try {
            JSONObject jsonObject = new JSONObject(params);
            String filename = jsonObject.getString("filename");
            int playStatus = jsonObject.getInt("playStatus");
            int uploadStatus = jsonObject.getInt("uploadStatus");
            if (uploadStatus == 0) {
                //TODO 手机播放
                if (playStatus == 1) {
//                    ((BlocklyActivity) mBaseActivity).playMP3Recorder(filename);
                } else {
//                    ((BlocklyActivity) mBaseActivity).stopMP3Play();
                }

            } else {
                //TODO 机器人播放
                if (playStatus == 1) {
                    //播放
                } else {
                    //停止
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((BlocklyActivity) mBaseActivity).playSoundAudio(params);
    }










    /**
     * 判断边充边玩是否打开
     *
     * @return true or false
     */
/*    @JavascriptInterface
    public boolean checkCharge() {
        ViseLog.d(TAG, "checkCharge:" + ((BlocklyActivity) mBaseActivity).checkCharge());
        return ((BlocklyActivity) mBaseActivity).checkCharge();
    }*/





    @JavascriptInterface
    public void endInit() {
        ViseLog.d(TAG, "-endInit-");
    }

    @JavascriptInterface
    public String courseInitDefaultJs() {
        ViseLog.d(TAG, "-courseInitDefaultJs-");
        return "";
    }




    /**
     *开始执行动作
     */
    @JavascriptInterface
    public void startRunProgram(){
        ViseLog.d(TAG, "-startRunProgram-");
//        ((BlocklyActivity) mBaseActivity).startOrStopRun((byte)0x01);
    }

    /**
     * 执行完程序
     */
    @JavascriptInterface
    public void finishProgramRun() {
        ViseLog.d(TAG, "-finishProgramRun-");
//        ((BlocklyActivity) mBaseActivity).startOrStopRun((byte)0x02);

    }

    @JavascriptInterface
    public void debugLog(String log) {

    }


    @JavascriptInterface
    public void stopAllAction() {
        //TODO 停止所有动作
        ViseLog.d(TAG, "stopAllAction");
        ((BlocklyActivity) mBaseActivity).stopPlay();
    }

    /**
     * 保存项目
     *
     * @param saveString
     */
    @JavascriptInterface
    public void saveProject(String saveString) {
        ViseLog.d(TAG, "saveProject:" + saveString);
        try {
            JSONObject jsonObject = new JSONObject(saveString);
            String pid = jsonObject.getString("pid");
            String name = jsonObject.getString("name");
            String xml = jsonObject.getString("xml");
            ViseLog.d(TAG, "pid:" + pid + "_xml:" + xml);

            List<BlocklyProjectMode> projectModeList =  DataSupport.where("pid = ?", pid).find(BlocklyProjectMode.class);
            if(projectModeList.size()>0){
                ((BlocklyActivity) mBaseActivity).updateUserProgram(pid,name, xml);
            }else{
                ((BlocklyActivity) mBaseActivity).saveUserProgram(pid,name, xml);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除保存项目
     * @param xml
     */
    @JavascriptInterface
    public void deleteProjectXml(String xml) {
        ViseLog.d(TAG, "deleteProjectXml:" + xml);

        ((BlocklyActivity) mBaseActivity).deleteUserProgram(new String[]{xml});

    }

    /**
     * 获取保存的项目列表
     *
     * @return
     */
    @JavascriptInterface
    public String getProjectLists() {
        ViseLog.d(TAG, "getProjectLists:");
        return getProjectList();
    }

    private String getProjectList() {
        UserInfoModel userInfoModel = (UserInfoModel) SPUtils.getInstance().readObject(Constant1E.SP_USER_INFO);
       String userId = userInfoModel.getUserId();
        List<BlocklyProjectMode> projectModeList = DataSupport.findAll(BlocklyProjectMode.class);

        ViseLog.d(TAG, "json:" + projectModeList.toString());

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < projectModeList.size(); i++) {
            try {
                BlocklyProjectMode projectMode = projectModeList.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", projectMode.getProgramName());
                jsonObject.put("xml", projectMode.getProgramData());
                jsonObject.put("pid", projectMode.getPid());
                ViseLog.d(TAG, "projectMode:" + projectMode.getProgramData());
                jsonArray.put(i, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        String json = jsonArray.toString().replace("\\/", "/");
        ViseLog.d(TAG, "json:" + json);
        return json;


    }








    /***
     * 判断字符串是否都是数字
     */
    public  boolean isStringNumber(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 事件类型枚举； 低电量、机器人跌倒、红外
     */

    public enum EventType {
        lowBatter,
        fallDown,
        infraredDistance
    }





}
