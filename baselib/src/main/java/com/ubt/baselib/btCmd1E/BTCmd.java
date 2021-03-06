package com.ubt.baselib.btCmd1E;

/**
 * @作者：bin.zhang@ubtrobot.com
 * @日期: 2018/1/25 14:17
 * @描述:
 */

public interface BTCmd {

    /**
     * 握手
     */
    byte DV_HANDSHAKE = 0x01;
    /**
     * 获取动作表名
     */
    byte DV_GETACTIONFILE = 0x02;
    /**
     * 执行动作表
     */
    byte DV_PLAYACTION = 0x03;
    /**
     * 停止播放
     */
    byte DV_STOPPLAY = 0x05;
    /**
     * 声音控制：0x06 参数： 00 － 静音 01 - 打开声音
     */
    byte DV_VOICE = 0x06;
    /**
     * 播放控制：0x07 参数：00 － 暂停 01 － 继续
     */
    byte DV_PAUSE = 0x07;

    /**
     * 心跳
     */
    byte DV_XT = 0x08;

    /**
     * 修改设备名 参数：新的设备名
     */
    byte DV_MODIFYNAME = 0x09;
    /**
     * 读取状态：0x0a 下位机返回：声音状态(00+声音状态(00 静音 01有声音)) 播放状态(01+(播放状态00 暂停 01非暂停))
     * 音量状态(02+1B(255)低字节在前) 灯状态：（03+00:关， 01开） SD卡状态：04 1-OK 0-NO
     */
    byte DV_READSTATUS = 0x0a;

    /**
     * 调整音量 参数：(0~255)
     */
    byte DV_VOLUME = 0x0b;

    /**
     * 掉电
     */
    byte DV_DIAODIAN = 0x0c;

    /**
     * 灯控制 参数：0-关 1 开
     */
    byte DV_LIGHT = 0x0d;

    /**
     * 时间校准
     **/
    byte DV_ADJUST_TIME = 0x0e;
    /**
     * 读取闹铃时间
     **/
    byte DV_READ_ALARM = 0x0f;
    /**
     * 设置闹铃时间
     **/
    byte DV_WRITE_ALARM = 0x10;
    /**
     * 读版软件版本号
     **/
    byte DV_READ_SOFTWARE_VERSION = 0x11;
    /**
     * 删除动作表
     **/
    byte DV_DELETE_FILE = 0x12;
    /**
     * 修改文件名
     **/
    byte DV_MODIFY_FILENAME = 0x13;
    /**
     * 传输开始
     **/
    byte DV_FILE_UPLOAD_START = 0x14;
    /**
     * 传输中
     **/
    byte DV_FILE_UPLOADING = 0x15;
    /**
     * 传输结束
     **/
    byte DV_FILE_UPLOAD_END = 0x16;
    /**
     * 传输取消
     **/
    byte DV_FILE_UPLOAD_CANCEL = 0x17;
    /**
     * 读电量
     * 参数1：2B    电压（mV），高字节先发，低字节后发
     * 参数2：1B    是否充电    00/否 01/是 02/没有电池  03/已充满电, 适配器接入
     * 参数3：1B    电量百分比(0~100)
     **/
    byte DV_READ_BATTERY = 0x18;
    /**
     * 低电量
     **/
    byte DV_READ_LOWBATTERY = 0x19;
    /**
     * 读硬件版本号
     **/
    byte DV_READ_HARDWARE_VERSION = 0x20;
    /**
     * 读版蓝牙版本号
     **/
    byte DV_READ_BLUETOOTH_VERSION = (byte) 0x9A;
    /**
     * 蓝牙升级
     **/
    byte DV_BLUETOOTH_UPGRADE = 0x1A;

    /**
     * 蓝牙升级
     **/
    byte DV_BLUETOOTH_UPGRADE_PERCENT = 0x2A;

    /**
     * 设置default命令
     */
    byte DV_SET_ACTION_DEFAULT = 0X21;
    /**
     * 传动作表
     */
    byte UV_GETACTIONFILE = (byte) 0X80;
    byte UV_STOPACTIONFILE = (byte) 0x81;
    /**
     * 动作结束主动上报
     */
    byte DV_ACTION_FINISH = (byte) 0x31;

    /**
     * 控制1个舵机动作
     */
    byte CTRL_ONE_ENGINE_ON = (byte) 0x22;

    /**
     * 控制16个舵机动作
     */
    byte CTRL_ALL_ENGINE = (byte) 0x23;

    /**
     * 单个舵机掉电
     */

    byte CTRL_ONE_ENGINE_LOST_POWER = (byte) 0x24;


    /**
     * 读写SN命令。参数<P1><P2>。 P1 == 0，表示读SN，无P2参数；P1 ==
     * 1，表示写SN，P2为写入的设备SN，不定长度最大16字节字符串。 设备应答：<P1><P2>。参数P1 ==
     * 0，表示读SN，P2为读取的设备SN，不定长度最大16字节字符串； P1 ==
     * 1，表示写SN，P2为写入状态，P2==0成功，P2==1失败。如果下发的SN和设备端一样，设备端不会进行写操作，但会反回成功标志。
     */

    byte READ_SN_CODE = (byte) 0x33;

    /**
     * 读UDID(Unique device ID register)命令。无参。 设备应答：<P1>。参数 P1==设备MCU UDID，
     * 不定长度，最大16字节，16进制。
     */
    byte READ_UID_CODE = (byte) 0x34;

    /**
     * 设置边充边玩状态。参数<P1>。P1==1，表示允许边充边玩；P1==0，表示禁止边充边玩。
     * 当主机发送动作执行命令且机器人处于禁止边充边玩状态时有回复，参数是0。
     */
    byte SET_PALYING_CHARGING = (byte) 0x32;

    /**
     * 读取16个舵机角度 回复：1个参数，长度16B（对应1-16号舵机的角度）。 单个舵机角度含义：FF，舵机没应答；FE，舵机ID不对
     */
    byte READ_ALL_ENGINE = (byte) 0x25;

    /**
     * 用FBCF协议头读B7看舵机个数和列表,以便后面操作舵机,表示支持UTF8,不回复B7表示为GBK旧版,不支持多舵机控制,并且主机只能使用FBBF协议头下发.
     */
    byte DV_HANDSHAKE_B_SEVEN = (byte) 0xB7;

    /**
     * 读取音源状态.
     */
    byte DV_READ_AUDIO_SOURCE_STATE = (byte) 0xB5;

    /**
     * 切换音源.
     */
    byte DV_SET_AUDIO_SOURCE = (byte) 0x35;


    /** ===================Alpha 1E start==================== **/

    /**
     * 获取WIFI列表
     */
    byte DV_FIND_WIFI_LIST = (byte) 0x36;

    /**
     * 机器人回复WIFI信息
     */
    byte DV_GET_WIFI_LIST_INFO = (byte) 0x37;

    /**
     * 机器人回复WIFI获取结束
     */
    byte DV_GET_WIFI_LIST_FINISH = (byte) 0x38;
    /**
     * 网络连接
     * 返回参数：0 未连接 1 连接中 2 连接成功 3 连接失败
     */
    byte DV_DO_NETWORK_CONNECT = (byte) 0x39;

    /**
     * 获取联网状态
     * status 连接状态 true 已连接  false 未联网
     * name 连接名称 当status = true 时， name=当前联网名称 ip=联网时IP 当status = false时  name等于空 ip等于null
     * 返回 {"status":"true","name":"","ip":""}
     */
    byte DV_READ_NETWORK_STATUS = (byte) 0x40;

    /**
     * 读取自动升级状态.
     * 0 为未开启， 1为已开启
     */
    byte DV_READ_AUTO_UPGRADE_STATE = (byte) 0x41;

    /**
     * 切换自动升级状态.
     * 0 为未开启， 1为已开启 , 2 设置中
     */
    byte DV_SET_AUTO_UPGRADE = (byte) 0x42;

    /**
     * 升级本体软件.
     * 手动升级： 0x43 {小端模式}
     * 参数1: 0未知，　１请求升级，２确定升级（进入升级），　３暂时不升级，４永不升级
     * 回复：
     * 应答：0
     */
    byte DV_DO_UPGRADE_SOFT = (byte) 0x43;

    /**
     * 升级本体软件.
     * status : 1 下载中 2 下载成功 0 下载失败
     * progress : 下载进度
     * totalSize : 文件总大小
     * {"status":"1","progress":"20","totalSize":"16M"}
     */
    byte DV_DO_UPGRADE_PROGRESS = (byte) 0x44;

    /**
     * 是否进入安装软件（1 电量充足进入安装，0 电量不足，不进入安装）.
     */
    byte DV_DO_UPGRADE_STATUS = (byte) 0x45;

    /**
     * Alpha 1E 下载动作.
     * <p>
     * 发送：{"actionId":"1","actionName":"name","actionPath":"http://"}
     * actionId : 动作ID
     * actionName : 动作名称
     * actionPath : 动作下载URL
     * <p>
     * 回复：{"status":"1","progress":"20","actionId":"1"}
     * status : 1 下载中 2 下载成功 3 未联网 4 解压失败 0 下载失败
     * progress : 下载进度
     * actionId : 动作ID
     */
    byte DV_DO_DOWNLOAD_ACTION = (byte) 0x46;

    /**
     * 判断动作文件是否存在
     * 发送参数：文件名
     * 回复 0 不存在 1 存在
     */
    byte DV_DO_CHECK_ACTION_FILE_EXIST = (byte) 0x47;

    /**
     * 读取机器人版本号
     * 发送参数：文件名
     * 回复 0 不存在 1 存在
     */
    byte DV_READ_ROBOT_SOFT_VERSION = (byte) 0x48;

    /**
     * 测试安装固件(慎用)
     * 发送参数：0
     * 回复 0
     */
    byte DV_DO_TEST_FIRMWARE_UPGRADE = (byte) 0x49;

    /**
     * 读取红外传感器与障碍物的距离
     * 参数 01 开启上报， 00 停止上报
     */
    byte DV_READ_INFRARED_DISTANCE = (byte) 0x50;

    /**
     * 检查机器人是否听到固定语音状态
     */
    byte DV_CHECK_SPEECH_STATE = (byte) 0x51;

    /**
     * 读取机器人的姿态角度（前倾角/后倾角/左倾角/右倾角 角度）
     */
    byte DV_READ_ROBOT_GYROSCOPE_DATA = (byte) 0x52;

    /**
     * 读取机器人跌倒状态
     */
    byte DV_READ_ROBOT_FALL_DOWN = (byte) 0x53;

    /**
     * 读取机器人加速度, 0x01开启上报， 0x00关闭上报
     * update:目前未读取温湿度传感器数据接口
     */
    byte DV_READ_ROBOT_ACCELERATION = (byte) 0x54;


    /**
     * 设置播放音效
     */
    byte DV_SET_PLAY_SOUND = (byte) 0x60;

    /**
     * 设置led灯效
     */
    byte DV_SET_LED_LIGHT = (byte) 0x61;

    /**
     * 设置播放语音
     */
    byte DV_SET_PLAY_SPEECH = (byte) 0x62;

    /**
     * 设置播放表情
     */
    byte DV_SET_PLAY_EMOJI = (byte) 0x63;

    /**
     * 设置停止播放音效
     */
    byte DV_SET_STOP_VOICE = (byte) 0x64;

    /**
     * 停止播放表情
     */
    byte DV_SET_STOP_EMOJI = (byte) 0x69;

    /**
     * 停止眼睛灯光
     */

    byte DV_SET_STOP_LED_LIGHT = (byte) 0x65;

    /**
     * Alpha1E 正在播放，参数：当前播放的动作名
     */
    byte DV_CURRENT_PLAY_NAME = (byte) 0x66;

    /**
     * 请求wifi传输port
     */
    byte REQUEST_WIFI_PORT = (byte) 0x6A;


    /**
     * 1E机器人拍头打断事件，该事件是机器人主动上报
     */
    byte DV_TAP_HEAD = (byte) 0x70;

    /**
     * 1E机器人跌倒事件，该事件是机器人主动上报
     */
    byte DV_FALL_DOWN = (byte) 0x71;
    /**
     * 1E 机器人机器人6D方位变化事件上报  1. 站立 2.倒立  3.左卧 4.右卧 5.躺着 6.趴着
     */
    byte DV_6D_GESTURE = (byte) 0x72;
    /**
     * 1E机器人 进入到休眠事件 （机器人 通知客户端）
     */
    byte DV_SLEEP_EVENT = (byte) 0x73;

    /**
     * 1E机器人进入到 低电量模式（机器人 通知客户端）
     */
    byte DV_LOW_BATTERY = (byte) 0x74;
    /**
     * 1E进入课程
     */
    byte DV_ENTER_COURSE = (byte) 0x75;

    /**
     * 步态算法行走
     */
    byte DV_WALK = (byte) 0x76;

    byte DV_STOP_WALK = (byte) 0x77;


    /**
     * 语音唤醒
     */
    byte DV_VOICE_WAIT = (byte) 0x78;

    /**
     * 1E启用禁用拍头课程
     */
    byte DV_CAN_CLICK_HEAD = (byte) 0x79;

    /**
     * 1E机器人获取产品productID和DSN
     */
    byte DV_PRODUCT_AND_DSN = (byte) 0x82;


    /**
     * 1E机器人发送CLIENT ID
     */
    byte DV_CLIENT_ID = (byte) 0x83;

    /**
     * 1E机器人发送CLIENT ID 分两段
     */
//	public static final byte DV_CLIENT_ID2 = (byte) 0x82;


    /**
     * 1E 机器人通用命令
     */
    int DV_COMMON_COMMAND = 0x93;


    /**
     * 进参数1（1B ）：状态切换类型：
     0:未知状态，
     １:进入逻辑编程状态，
     ２:退出逻辑编程状态，
     3:进入动作编缉状态，
     4:退出动作编缉状态
     */
    byte DV_INTO_EDIT = (byte) 0x95;

    /** 跌倒保护
     * 传感器控制命令 0 禁止 1 启用
     */
    byte DV_SENSOR_CONTROL = (byte) 0x96;
    /**
     * 1E 机器人控制舵机命令
     * 参数1 1B: 1.掉电；2.上电
     * 参数2 1B：1.左手；2.右手 3.双脚。
     */
    byte DV_CONTROL_ENGINE_COMMAND = (byte) 0x97;

    /*启用禁用红外传感器命令:0x98
    参数1(1B):0读取,1设置
    参数2(1B):0禁用,1启用(参数1为读取的时候,参数2设为0)

    回复:(1B) 0禁用 1启用*/
    byte DV_CONTROL_IR_SENSOR = (byte) 0x98;

    /**
     * 获取行为事项播放状态
     * 回复：{
     * "eventId": "123",
     * "playAudioSeq": "0",
     * "audioState": "" //no play,playing
     * }
     */
    byte DV_READ_HIBITS_PLAY_STATUS = (byte) 0x9A;

    /**
     * 获取行为事项播放状态
     * 回复：{"eventId": "123",
     * "playAudioSeq": "0"
     * "cmd": "start"    //stop,start,pause,unpause
     * }
     */
    byte DV_CONTROL_HIBITS_PLAY = (byte) 0x99;


    /**
     * 获取行为language状态
     * 回复：{"seq": "1", "event": "1" }
     */
    byte DV_COMMON_CMD = (byte) 0x6D;
}
