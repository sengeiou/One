package com.ubt.en.alpha1e.action.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baoyz.pg.PG;
import com.orhanobut.dialogplus.DialogPlus;
import com.ubt.baselib.customView.BaseDialog;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.baselib.utils.PermissionUtils;
import com.ubt.baselib.utils.TimeUtils;
import com.ubt.baselib.utils.ToastUtils;
import com.ubt.en.alpha1e.action.ActionSaveActivity;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.adapter.FrameRecycleViewAdapter;
import com.ubt.en.alpha1e.action.adapter.TimesHideRecycleViewAdapter;
import com.ubt.en.alpha1e.action.adapter.TimesRecycleViewAdapter;
import com.ubt.en.alpha1e.action.course.ActionSaveCourseActivity;
import com.ubt.en.alpha1e.action.course.CourseProgressListener;
import com.ubt.en.alpha1e.action.dialog.DialogMusic;
import com.ubt.en.alpha1e.action.dialog.DialogTips;
import com.ubt.en.alpha1e.action.dialog.PrepareActionUtil;
import com.ubt.en.alpha1e.action.dialog.PrepareMusicUtil;
import com.ubt.en.alpha1e.action.model.ActionDataModel;
import com.ubt.en.alpha1e.action.model.ActionsEditHelper;
import com.ubt.en.alpha1e.action.model.NewActionPlayer;
import com.ubt.en.alpha1e.action.model.PrepareDataModel;
import com.ubt.en.alpha1e.action.model.PrepareMusicModel;
import com.ubt.en.alpha1e.action.util.ActionConstant;
import com.ubt.en.alpha1e.action.util.DialogPreview;
import com.ubt.en.alpha1e.action.util.FileTools;
import com.ubt.htslib.base.ByteHexHelper;
import com.ubt.htslib.base.FrameActionInfo;
import com.ubt.htslib.base.NewActionInfo;
import com.vise.log.ViseLog;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.currentTimeMillis;

/**
 * @author：liuhai
 * @date：2017/11/15 15:30
 * @modifier：ubt
 * @modify_date：2017/11/15 15:30
 * [A brief description]
 * version
 */

public abstract class BaseActionEditLayout extends LinearLayout implements View.OnClickListener, PrepareActionUtil.OnDialogListener, DialogTips.OnLostClickListener, DialogMusic.OnMusicDialogListener, DialogPreview.OnActionPreviewListener, FrameRecycleViewAdapter.OnchangeCurrentItemTimeListener {
    private static final String TAG = "BaseActionEditLayout";

    public ImageView ivRobot;
    public ImageView ivHandLeft, ivHandRight, ivLegLeft, ivLegRight;
    public RecyclerView recyclerViewFrames;
    public List<Map<String, Object>> list_frames;

    public List<Map<String, Object>> list_autoFrames = new ArrayList<Map<String, Object>>();

    public FrameRecycleViewAdapter adapter;
    private LinearLayoutManager layoutManager;
    private LinearLayoutManager layoutManagerTime;

    public ImageView ivAddFrame;
    public ImageView ivBack, ivReset, ivAutoRead, ivSave, ivHelp;
    public ImageView ivActionLib, ivActionLibMore, ivActionBgm;

    public NewActionInfo mCurrentNewAction;
    public static String SCHEME_ID = "SCHEME_ID";
    public static String SCHEME_NAME = "SCHEME_NAME";
    public static String FROM_TYPE = "FROM_TYPE";
    private String mSchemeId = "";
    private String mSchemeName = "";

    private boolean isSaveSuccess = false;

    public boolean autoRead = false;


    //action edit frame view
    public RelativeLayout rlEditFrame;
    private ImageView ivPreview, ivCopy, ivChange, ivCut, ivPaste, ivDelete;
    private TextView tvCut;

    private Map<String, Object> mCurrentEditItem;
    private boolean change = false;
    private boolean copy = false;
    private boolean cut = false;
    private Map<String, Object> mCutItem = new HashMap<String, Object>();
    private int selectPos = -1;
    private Map<String, Object> mCopyItem = new HashMap<String, Object>();


    private int firstVisibleItemPosition = -1;
    private int lastVisibleItemPosition = -1;

    private RecyclerView recyclerViewTimes;
    private TimesRecycleViewAdapter timeAdapter;
    private List<Map<String, Object>> timeDatas = new ArrayList<Map<String, Object>>();
    public static final String TIME = "time";
    public static final String SHOW = "show";
    private SeekBar sbTime;
    private int current = 0;


    //    public String[] init = {"90", "90", "90", "90", "90", "90", "90", "60", "76", "110", "90", "90",
//            "120", "104", "70", "90"};
    public String[] init = {"93", "20", "66", "86", "156", "127", "90", "74", "95", "104", "89", "89",
            "104", "81", "76", "90"};
    public boolean lostLeftHand = false;
    public boolean lostRightHand = false;
    public boolean lostLeftLeg = false;
    public boolean lostRightLeg = false;
    public boolean needAdd = false;
    private List<Integer> ids = new ArrayList<Integer>();
    private int readCount = -1;
    public String autoAng = "";

    private ImageView ivZoomPlus, ivZoomMinus;
    private int currentPlus = 1;
    private int currentMinus = 1;
    private TextView tvZoomPlus, tvZoomMinus;

    public SeekBar sbVoice;
    private int touch = 0;

    private int timePosition = 0;

    private MediaPlayer mediaPlayer;
    private String mDir = "";
    public int musicTimes = 0;

    public ImageView ivPlay;
    private TextView tvMusicTime;

    private boolean playFinish = true;

    private long clickTime = 0;

    public ImageView ivResetIndex;

    private List<Map<String, Object>> listActionLib;
    public static final String ACTION_TIME = "action_time";
    public static final String ACTION_ANGLE = "action_angle";
    public static final String ACTION_NAME = "action_name";
    public static final String ACTION_ICON = "action_icon";


    private List<Map<String, Object>> listHighActionLib = new ArrayList<Map<String, Object>>();
    private String[] highActionName;
    private int[] advanceIconID;
    private String[] basicAction;
    private int[] basicIconID;
    private List<Map<String, Object>> listBasicActionLib = new ArrayList<Map<String, Object>>();


    private String[] songs = {"", "flexin", "jingle bells", "london bridge is falling down",
            "twinkle twinkle little star", "yankee doodle dandy", "kind of light", "so good",
            "Sun Indie Pop", "The little robot", "zombie"};
    public static final String SONGS_NAME = "songs_name";
    public static final String SONGS_TYPE = "songs_type"; //用来区分是内置音乐还是录音
    private List<Map<String, Object>> listSongs = new ArrayList<Map<String, Object>>();


    private ImageView ivCancelChange;

    private float density = 1;

    private ImageView ivDeleteMusic;
    private RelativeLayout rl_delete_music;
    private ImageView iv_del_music;
    private TextView tvDeleteMusic;

    private int time;


    private boolean doPlayPreview = false;

    private RecyclerView recyclerViewTimesHide;
    private TimesHideRecycleViewAdapter timeHideAdapter;
    private LinearLayoutManager layoutManagerTimeHide;

    private boolean isFinishFramePlay = true;

    public static String BACK_UP = "back_up";

    private int currentIndex = 1;
    private int scroll = 0;

    public Context mContext;
    PrepareActionUtil mPrepareActionUtil;
    PrepareMusicUtil mPrepareMusicUtil;

    public ActionsEditHelper mHelper;

    public boolean isOnCourse;//是否正在课程页面
    public boolean isCourseReading;//课程里面正在读取机器人角度变化

    private RelativeLayout rlRecordingState;
    private TextView tvRecordIndex;
    private TextView tvRecordTime;

    public BaseActionEditLayout(Context context) {
        super(context);
        mContext = context;
        init(context);
    }


    public BaseActionEditLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    public BaseActionEditLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    public abstract void setData(CourseProgressListener courseProgressListener);

    public abstract int getLayoutId();

    public abstract void playComplete();

    public abstract void onPause();

    public void init(Context context) {
        View.inflate(context, getLayoutId(), this);
        initUI();

    }

    public void setUp(ActionsEditHelper baseHelper) {
        this.mHelper = baseHelper;
    }

    OnSaveSucessListener listener;

    public void setOnSaveSucessListener(OnSaveSucessListener listener) {
        this.listener = listener;
    }

    /**
     * 初始化UI
     */
    protected void initUI() {
        mCurrentNewAction = new NewActionInfo();
        ivPlay = (ImageView) findViewById(R.id.iv_play_music);
        tvMusicTime = (TextView) findViewById(R.id.tv_play_time);
        ivPlay.setOnClickListener(this);
        ivCancelChange = (ImageView) findViewById(R.id.iv_cancel_update);
        ivCancelChange.setOnClickListener(this);

        ivRobot = (ImageView) findViewById(R.id.iv_robot);
        ivHandLeft = (ImageView) findViewById(R.id.iv_hand_left);
        ivHandLeft.setOnClickListener(this);
        ivHandRight = (ImageView) findViewById(R.id.iv_hand_right);
        ivHandRight.setOnClickListener(this);
        ivLegLeft = (ImageView) findViewById(R.id.iv_leg_left);
        ivLegLeft.setOnClickListener(this);
        ivLegRight = (ImageView) findViewById(R.id.iv_leg_right);
        ivLegRight.setOnClickListener(this);
        initRobot();

        recyclerViewFrames = (RecyclerView) findViewById(R.id.rcv_actions);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewFrames.setLayoutManager(layoutManager);
        list_frames = new ArrayList<Map<String, Object>>();
        adapter = new FrameRecycleViewAdapter(mContext, list_frames, density, this);
        recyclerViewFrames.setAdapter(adapter);

        recyclerViewFrames.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 10;
            }
        });
        adapter.setOnItemListener(new FrameRecycleViewAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int pos, Map<String, Object> data) {
                ViseLog.d("getNewPlayerState:" + mHelper.getNewPlayerState());
                if (autoRead) {
                    return;
                }

                if (mHelper.getNewPlayerState() == NewActionPlayer.PlayerState.PLAYING || isOnCourse) {
                    return;
                }

                if (musicTimes != 0) {
                    if (pos == list_frames.size() - 1) {
                        return;
                    }
                }

                adapter.setDefSelect(pos);

                selectPos = pos;
                mCurrentEditItem = data;
                showEditFrameLayout();
                updateAddViewEnable();
            }
        });

        recyclerViewFrames.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                ViseLog.d("firstVisibleItemPosition:" + firstVisibleItemPosition +
                        "-lastVisibleItemPosition:" + lastVisibleItemPosition);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (touch == 1) {
                    return;
                }

                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    if (touch == 2) {
                        ViseLog.d("onScrolled recyclerViewFrames  dx:" + dx);
                        recyclerViewTimes.scrollBy(dx, dy);
                        recyclerViewTimesHide.scrollBy(dx, dy);
                    }

                }

            }
        });

        recyclerViewFrames.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (musicTimes != 0 && list_frames.size() == 1) {
                    return true;
                }
                touch = 2;
                return false;
            }
        });

        ivAddFrame = (ImageView) findViewById(R.id.iv_add_frame);
        ivAddFrame.setEnabled(true);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
        ivAddFrame.setOnClickListener(this);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        ivReset = (ImageView) findViewById(R.id.iv_reset);
        ivReset.setOnClickListener(this);
        ivAutoRead = (ImageView) findViewById(R.id.iv_auto_read);
        ivAutoRead.setOnClickListener(this);
        ivSave = (ImageView) findViewById(R.id.iv_save_action);
        ivSave.setOnClickListener(this);
        ivHelp = (ImageView) findViewById(R.id.iv_help);
        ivHelp.setOnClickListener(this);

        ivActionLib = (ImageView) findViewById(R.id.iv_action_lib);
        ivActionLib.setOnClickListener(this);
        ivActionLibMore = (ImageView) findViewById(R.id.iv_action_lib_more);
        ivActionLibMore.setOnClickListener(this);
        ivActionBgm = (ImageView) findViewById(R.id.iv_action_bgm);
        ivActionBgm.setOnClickListener(this);
        initEditFrameLayout();
//        rlRoot = (RelativeLayout) findViewById(R.id.rl_action_edit);
        recyclerViewTimes = (RecyclerView) findViewById(R.id.rcv_time);
        layoutManagerTime = new LinearLayoutManager(mContext);
        layoutManagerTime.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTimes.setLayoutManager(layoutManagerTime);

        recyclerViewTimesHide = (RecyclerView) findViewById(R.id.rcv_time_hide);
        layoutManagerTimeHide = new LinearLayoutManager(mContext);
        layoutManagerTimeHide.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTimesHide.setLayoutManager(layoutManagerTimeHide);

        recyclerViewTimesHide.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int firstVisibleItemPosition = layoutManagerTimeHide.findFirstVisibleItemPosition();
                ViseLog.d("firstVisibleItemPosition:" + firstVisibleItemPosition + "total:" + timeDatas.size());
                String pro = accuracy(firstVisibleItemPosition, timeDatas.size(), 0);
                ViseLog.d("pro:" + pro);
                if (recyclerViewTimesHide.getVisibility() == View.VISIBLE) {
//                    sbVoice.setProgress(Integer.valueOf(pro)*100);
                }

            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ViseLog.d("onScrolled recyclerViewTimesHide");
                recyclerViewTimes.scrollBy(dx, dy);
                recyclerViewFrames.scrollBy(dx, dy);
            }
        });
        ivZoomPlus = (ImageView) findViewById(R.id.iv_zoom_plus);
        ivZoomMinus = (ImageView) findViewById(R.id.iv_zoom_minus);
        ivZoomPlus.setOnClickListener(this);
        ivZoomMinus.setOnClickListener(this);
        tvZoomPlus = (TextView) findViewById(R.id.tv_zoom_plus);
        tvZoomMinus = (TextView) findViewById(R.id.tv_zoom_minus);
        sbVoice = (SeekBar) findViewById(R.id.sb_voice);

        sbVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ViseLog.d("progress:" + getnum(progress, musicTimes));

                if (timeDatas.size() <= 0) {
                    return;
                }
                float a = Float.valueOf(getnum(progress, musicTimes));
                ViseLog.d("progress a:" + a + Math.round(a));

                timePosition = (musicTimes / 100) * Math.round(a) / 100;
                if (timePosition <= 0) {
                    timePosition = 0;
                } else {
                    timePosition = timePosition - 1;
                }

                ViseLog.d("timePosition:" + timePosition);

//                recyclerViewTimes.smoothScrollToPosition(timePosition);
                ViseLog.d("1progress:" + Math.round(a) + "rate:" + Math.round(a) / 10 + "timePosition:" + timePosition);
                int rate = Math.round(a) / 10;

                if (rate == 10) {
                    ViseLog.d("timePosition:" + timePosition);
                    layoutManagerTime.scrollToPositionWithOffset(timePosition, 0);
                    layoutManagerTimeHide.scrollToPositionWithOffset(timePosition, 0);
                    layoutManagerTimeHide.setStackFromEnd(true);

                } else {
                    if (rate == 0) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 1) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 2) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 1, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 1, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 3) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 2, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 2, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 4) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 3, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 3, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 5) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 4, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 4, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 6) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 5, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 5, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 7) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 6, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 6, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 8) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 7, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 7, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else if (rate == 9) {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 8, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 8, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    } else {
                        layoutManagerTime.scrollToPositionWithOffset(timePosition - rate - 1, 0);
                        layoutManagerTimeHide.scrollToPositionWithOffset(timePosition - rate - 1, 0);
                        layoutManagerTimeHide.setStackFromEnd(true);
                    }


                }

//                layoutManagerTime.setStackFromEnd(true);
                ViseLog.d("recyclerViewFrames smoothScrollToPosition 1:" + scroll);
                recyclerViewFrames.scrollBy((musicTimes) * Math.round(a) / 100 - scroll, 0);
//                recyclerViewTimes.scrollBy((musicTimes)*Math.round(a)/100-scroll+10, 0);
//                recyclerViewTimesHide.scrollBy((musicTimes)*Math.round(a)/100-scroll,0);
                scroll = (musicTimes) * Math.round(a) / 100;
//                recyclerViewFrames.smoothScrollToPosition(timePosition);

//                recyclerViewTimesHide.smoothScrollToPosition(timePosition);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ViseLog.d("22 onStartTrackingTouch:" + currentTimeMillis());
                if (timeDatas.size() <= 0) {
                    return;
                }
                clickTime = currentTimeMillis();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ViseLog.d("22 onStopTrackingTouch:" + currentTimeMillis());

                long dur = System.currentTimeMillis() - clickTime;
                if (System.currentTimeMillis() - clickTime < 100) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(TIME, "100");
                    if (timeDatas.get(timePosition).get(SHOW).equals("1")) {

                        map.put(SHOW, "1");
                    } else {
                        map.put(SHOW, "1");
                    }
                    ViseLog.d("timePosition:" + timePosition + "--dur:" + dur);
                    timeDatas.set(timePosition, map);
                    timeAdapter.notifyDataSetChanged();
                    timeHideAdapter.notifyDataSetChanged();
                } else {

                }
            }
        });

        sbVoice.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViseLog.d("sssss" + event.getX());
//                recyclerViewTimes.scrollBy((int)event.getX(), 0);
                return false;
            }
        });

        sbVoice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ViseLog.d(" sbVoice setOnClickListener");

                if (mediaPlayer != null) {
                    mediaPlayer.getDuration();

                }
            }
        });

        ivResetIndex = (ImageView) findViewById(R.id.iv_reset_index);
        ivResetIndex.setOnClickListener(this);
        ivDeleteMusic = (ImageView) findViewById(R.id.iv_music_icon);
        ivDeleteMusic.setOnClickListener(this);
        rl_delete_music = (RelativeLayout) findViewById(R.id.rl_delete_music);
        tvDeleteMusic = (TextView) findViewById(R.id.tv_del_music);
        iv_del_music = (ImageView) findViewById(R.id.iv_del_music);
        iv_del_music.setOnClickListener(this);
        initMediaPlayer();

        changeSaveAndPlayState();

        rlRecordingState = (RelativeLayout) findViewById(R.id.rl_recording_state);
        tvRecordTime = (TextView) findViewById(R.id.tv_recoding_time);
        tvRecordIndex = (TextView) findViewById(R.id.tv_recoding_index);
    }


    private void initEditFrameLayout() {
        rlEditFrame = (RelativeLayout) findViewById(R.id.lay_frame_data_edit);
        rlEditFrame.setOnClickListener(this);
        ivPreview = (ImageView) findViewById(R.id.iv_preview);
        ivPreview.setOnClickListener(this);
        ivChange = (ImageView) findViewById(R.id.iv_change);
        ivChange.setOnClickListener(this);
        ivCopy = (ImageView) findViewById(R.id.iv_copy);
        ivCopy.setOnClickListener(this);
        ivCut = (ImageView) findViewById(R.id.iv_cut);
        ivCut.setOnClickListener(this);
        tvCut = (TextView) findViewById(R.id.tv_cut);
        ivPaste = (ImageView) findViewById(R.id.iv_paste);
        ivPaste.setOnClickListener(this);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        ivDelete.setOnClickListener(this);

    }

    public void onPlayMusicComplete() {

    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ViseLog.d("播放完毕:" + isFinishFramePlay);
                onPlayMusicComplete();
                playFinish = true;
                mHandler.removeMessages(0);
                if (isFinishFramePlay && mediaPlayer != null) {
                    mediaPlayer.seekTo(0);
                    sbVoice.setProgress(0);
                    tvMusicTime.setText(TimeUtils.getTimeFromMillisecond((long) handleMusicTime(mediaPlayer.getDuration())));
                    ivPlay.setImageResource(R.drawable.icon_play_music);
                    ivAddFrame.setEnabled(true);
                    ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
                    recyclerViewTimesHide.setVisibility(View.GONE);
                    if (isFinishFramePlay) {
                        setEnable(true);
                    }
                    if (list_frames != null && list_frames.size() > 0) {
                        if (isFinishFramePlay) {
                            layoutManager.scrollToPosition(0);
                        }

                    }
                    if (recyclerViewTimes != null) {
                        recyclerViewTimes.smoothScrollToPosition(0);
                    }
                }

            }
        });
    }


    public void play(String mp3) {
        String path = null;
        if (mp3.equals("")) {
            path = mDir + File.separator + "a.mp3";
//            final File file = new File(path);
        } else {
            path = mDir + File.separator + mp3;
        }

        File file = new File(path);
        ViseLog.d("path:" + path);
        if (file.exists()) {

            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ToastUtils.showShort("file is not exit");
        }

    }


    /**
     * 播放预览动作
     */
    private void play() {
        try {
            if (mDir.equals("") || mDir.equals(null)) {
                return;
            }
            playFinish = false;
            mediaPlayer.start();

            //后台线程发送消息进行更新进度条
            final int milliseconds = 100;
            new Thread() {
                @Override
                public void run() {
                    while (true && !playFinish && !((Activity) mContext).isDestroyed()) {
                        try {
                            sleep(milliseconds);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        mHandler.sendEmptyMessage(0);
                    }
                }
            }.start();

//            mediaPlayer.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSaveAction;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_Stop,
                    null);
            if (isSaveAction) {
                return false;
            } else {
                return doBack();
            }
        }
        return false;
    }

    private boolean doBack() {

        if (musicTimes == 0) {
            if (list_frames.size() < 1) {
                doReset();
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    playFinish = true;
                    mediaPlayer = null;
                }
                mHelper.doEnterOrExitActionEdit((byte) 0x04);
                ((Activity) mContext).finish();
                return false;
            }
        } else {
            if (list_frames.size() < 2) {
                doReset();
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    playFinish = true;
                    mediaPlayer = null;
                }
                mHelper.doEnterOrExitActionEdit((byte) 0x04);
                ((Activity) mContext).finish();
                return false;
            }
        }

        if (isSaveSuccess) {
            mHelper.doEnterOrExitActionEdit((byte) 0x04);
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                playFinish = true;
                mediaPlayer = null;
            }
            ((Activity) mContext).finish();
            return true;
        }

        new BaseDialog.Builder(mContext)
                .setMessage(R.string.action_ui_readback_quit_tip).
                setConfirmButtonId(R.string.action_ui_readback_quit_confirm)
                .setCancleButtonID(R.string.action_ui_common_cancel)
                .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == com.ubt.baselib.R.id.button_confirm) {
                            //saveNewAction(1);
                            if (mHelper.getNewPlayerState() == NewActionPlayer.PlayerState.PLAYING) {
                                mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_Stop,
                                        getEditingPreviewActions());
                            }
                            doReset();
                            if (mediaPlayer != null) {
                                mediaPlayer.stop();
                                playFinish = true;
                                mediaPlayer = null;
                            }
                            mHelper.doEnterOrExitActionEdit((byte) 0x04);
                            ((Activity) mContext).finish();
                            dialog.dismiss();
                        } else if (view.getId() == com.ubt.baselib.R.id.button_cancle) {

                            dialog.dismiss();
                        }

                    }
                }).create().show();

        return true;
    }

    public void saveNewAction(int type) {
        if (musicTimes == 0) {
            if (list_frames.size() < 1) {
                new BaseDialog.Builder(mContext)
                        .setMessage(R.string.action_ui_readback_not_null).
                        setConfirmButtonId(R.string.action_ui_common_confirm)
                        .setCancleButtonID(R.string.action_ui_common_cancel)
                        .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == com.ubt.baselib.R.id.button_confirm) {
                                    dialog.dismiss();
                                } else if (view.getId() == com.ubt.baselib.R.id.button_cancle) {
                                    dialog.dismiss();
                                }

                            }
                        }).create().show();
                return;
            }
        } else {
            if (list_frames.size() < 2) {
                new BaseDialog.Builder(mContext)
                        .setMessage(R.string.action_ui_readback_not_null).
                        setConfirmButtonId(R.string.action_ui_common_confirm)
                        .setCancleButtonID(R.string.action_ui_common_cancel)
                        .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == com.ubt.baselib.R.id.button_confirm) {
                                    dialog.dismiss();
                                } else if (view.getId() == com.ubt.baselib.R.id.button_cancle) {
                                    dialog.dismiss();
                                }

                            }
                        }).create().show();
                return;
            }
        }

        Intent inte = new Intent();
        if (type == 1) {
            inte.setClass(mContext, ActionSaveActivity.class);
        } else {
              inte.setClass(mContext, ActionSaveCourseActivity.class);
        }
        inte.putExtra(ActionsEditHelper.New_ActionInfo, PG.convertParcelable(getEditingActions()));
        inte.putExtra(SCHEME_ID, mSchemeId);
        inte.putExtra(SCHEME_NAME, mSchemeName);
        inte.putExtra(FROM_TYPE, type);
        if (mDir != "") {
            inte.putExtra(ActionSaveActivity.MUSIC_DIR, mDir);
        }

        if (listener != null) {
            listener.startSave(inte);
        }

    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stopMusic() {
        if (mediaPlayer != null) {
            ViseLog.d("mediaPlayer stop");
//            tvMusicTime.setText(TimeUtils.getTimeFromMillisecond((long) handleMusicTime(mediaPlayer.getDuration())));
            playFinish = true;
            mHandler.removeMessages(0);
            mediaPlayer.stop();
            ivPlay.setImageResource(R.drawable.icon_play_music);
        }
    }

    public void replayMusic() {
        if (mediaPlayer != null && !mDir.equals("")) {
            ViseLog.d("mediaPlayer replayMusic");
            tvMusicTime.setText(TimeUtils.getTimeFromMillisecond((long) handleMusicTime(mediaPlayer.getDuration())));
            playFinish = true;
            mHandler.removeMessages(0);
            mediaPlayer.seekTo(0);
            ivPlay.setImageResource(R.drawable.icon_play_music);
        }
    }

    @Override
    public void onClick(View v) {
        int i1 = v.getId();
        if (i1 == R.id.iv_back) {
            doBack();

        } else if (i1 == R.id.iv_save_action) {
            saveNewAction(1);

        } else if (i1 == R.id.iv_cancel_update) {
            doCancelChange();

        } else if (i1 == R.id.iv_play_music) {
            startPlayAction();

        } else if (i1 == R.id.iv_del_music) {
            deleteMusic();
            changeSaveAndPlayState();

        } else if (i1 == R.id.iv_music_icon) {
            if (mDir != "" && playFinish) {
                rl_delete_music.setVisibility(View.VISIBLE);
            }

        } else if (i1 == R.id.iv_reset) {
            doReset();
            resetState();
            ivAddFrame.setEnabled(true);
            ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);

        } else if (i1 == R.id.iv_reset_index) {
            ViseLog.d("total size:" + list_frames.size());
            if (mediaPlayer != null && mediaPlayer.isPlaying() && !mDir.equals("")) {
                pause();
            }
            replayMusic();
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_Stop, null);
            sbVoice.setProgress(0);
            recyclerViewFrames.smoothScrollToPosition(0);
            recyclerViewTimes.smoothScrollToPosition(0);
            setEnable(true);

        } else if (i1 == R.id.iv_auto_read) {
            if (ids.size() <= 0) {
                showLostDialog(0, SkinManager.getInstance().getTextById(R.string.ui_create_click_to_cutoff));
            } else {
                DialogMusic dialogMusic = new DialogMusic(mContext, this, 1);
                dialogMusic.show();
            }

        } else if (i1 == R.id.sb_voice) {
        } else if (i1 == R.id.iv_help) {//                String language = ResourceManager.getInstance(mContext).getStandardLocale(ResourceManager.getInstance(mContext).getAppCurrentLanguage());
////                String url = "https://services.ubtrobot.com/actionHelp/actionHelp.html?lang=" + language;  //暂时这样
//                String url = "https://prodapi.ubtrobot.com/alpha1e/activeHelp.html";
//                ViseLog.d( "url:" + url);
//                Intent intent = new Intent();
//                intent.putExtra(WebContentActivity.SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                intent.putExtra(WebContentActivity.WEB_TITLE, "");
//                intent.putExtra(WebContentActivity.WEB_URL, url);
//                intent.setClass(mContext, HelpActivity.class);
//                mContext.startActivity(intent);

        } else if (i1 == R.id.iv_add_frame) {
            addFrameOnClick();

        } else if (i1 == R.id.iv_hand_left) {
            lostLeft();

        } else if (i1 == R.id.iv_hand_right) {
            lostRight();

        } else if (i1 == R.id.iv_leg_left) {
            if (lostLeftLeg) {
                return;
            }
            if (lostRightLeg == false) {
                showLostDialog(1, SkinManager.getInstance().getTextById(R.string.ui_create_holde_robot));
            } else {
                lostLeftLeg();
            }

        } else if (i1 == R.id.iv_leg_right) {
            if (lostRightLeg) {
                return;
            }
            if (lostLeftLeg == false) {
                showLostDialog(2, SkinManager.getInstance().getTextById(R.string.ui_create_holde_robot));
            } else {
                lostRightLeg();
            }

        } else if (i1 == R.id.iv_action_lib) {
            showPrepareActionDialog(1);

        } else if (i1 == R.id.iv_action_lib_more) {
            showPrepareActionDialog(2);

        } else if (i1 == R.id.iv_action_bgm) {
            PermissionUtils.getInstance().request(new PermissionUtils.PermissionLocationCallback() {
                @Override
                public void onSuccessful() {
                    showPrepareMusicDialog();
                }

                @Override
                public void onFailure() {

                }

                @Override
                public void onRationSetting() {

                }

                @Override
                public void onCancelRationSetting() {
                }

            }, PermissionUtils.PermissionEnum.STORAGE, mContext);

        } else if (i1 == R.id.iv_zoom_plus) {
            ivZoomPlus();

        } else if (i1 == R.id.iv_zoom_minus) {
            ivZoomMins();

        } else if (i1 == R.id.lay_frame_data_edit) {//                rlEditFrame.setVisibility(View.GONE);
            doCancelChange();
//                adapter.setDefSelect(-1);

        } else if (i1 == R.id.iv_preview) {
            doPreviewItem();

        } else if (i1 == R.id.iv_change) {
            doChangeItem();

        } else if (i1 == R.id.iv_copy) {
            doCopyItem();

        } else if (i1 == R.id.iv_cut) {
            doCutItem();

        } else if (i1 == R.id.iv_paste) {
            doPasteItem();

        } else if (i1 == R.id.iv_delete) {
            doDeleteItem();
            changeSaveAndPlayState();

        } else {
        }
    }

    /**
     * 预览动作帧
     */
    private void doPreviewItem() {
        mHelper
                .doCtrlAllEng(((FrameActionInfo) mCurrentEditItem
                        .get(ActionsEditHelper.MAP_FRAME)).getData());
        resetState();
        ivAddFrame.setEnabled(true);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
    }

    /**
     * 修改动作帧
     */
    public void doChangeItem() {
        if (ids.size() <= 0) {
            goneEditFrameLayout();
            showLostDialog(0, SkinManager.getInstance().getTextById(R.string.ui_create_click_to_cutoff));
            return;
        }
        change = true;
        ivCancelChange.setVisibility(View.VISIBLE);
        ivAddFrame.setImageResource(R.drawable.ic_confirm);
    }

    /**
     * 去修改动作帧
     */
    private void doCancelChange() {
        ivCancelChange.setVisibility(View.INVISIBLE);
        goneEditFrameLayout();
        change = false;
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
        adapter.setDefSelect(-1);
    }

    /**
     * 复制动作帧
     */
    private void doCopyItem() {
        copy = true;
        ivPaste.setEnabled(true);
        ivPaste.setImageResource(R.drawable.ic_paste);
        mCopyItem = mCurrentEditItem;
    }

    /**
     * 剪切动作帧
     */
    private void doCutItem() {
        cut = true;
        ivPaste.setEnabled(true);
        ivPaste.setImageResource(R.drawable.ic_paste);
        mCutItem = mCurrentEditItem;
    }

    /**
     * 粘贴动作帧
     */
    private void doPasteItem() {
        int index = selectPos;
        ViseLog.d("index:" + index);
        if (copy) {
            list_frames.add(index + 1, copyItem(mCopyItem));
        } else if (cut) {
            ViseLog.d("index:" + index);
            list_frames.add(index + 1, copyItem(mCutItem));
            list_frames.remove(mCutItem);
            mCutItem.clear();
        }
        goneEditFrameLayout();
        adapter.notifyDataSetChanged();
    }

    public Map<String, Object> copyItem(Map<String, Object> item) {
        Map<String, Object> c_item = new HashMap<String, Object>();
        c_item.put(ActionsEditHelper.MAP_FRAME, ((FrameActionInfo) item
                .get(ActionsEditHelper.MAP_FRAME)).doCopy());
        c_item.put(ActionsEditHelper.MAP_FRAME_NAME,
                item.get(ActionsEditHelper.MAP_FRAME_NAME));
        c_item.put(ActionsEditHelper.MAP_FRAME_TIME,
                item.get(ActionsEditHelper.MAP_FRAME_TIME));

        return c_item;
    }

    /**
     * 删除动作帧
     */
    private void doDeleteItem() {
        if (TextUtils.isEmpty(mDir)) {
            if (selectPos == list_frames.size() - 1) {
                currentIndex--;
            }

        } else {
            if (selectPos == list_frames.size() - 2) {
                currentIndex--;
            }
        }
        ViseLog.d("doDeleteItem selectPos:" + selectPos + "list:" + list_frames.size() + "currentIndex:" + currentIndex);
        list_frames.remove(mCurrentEditItem);
        if (TextUtils.isEmpty(mDir)) {
            if (list_frames.size() == 0) {
                currentIndex = 1;
            }

        } else {
            if (list_frames.size() == 1) {
                currentIndex = 1;
            }
        }
        adapter.notifyDataSetChanged();
        adapter.setDefSelect(-1);
        goneEditFrameLayout();
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
        ivCancelChange.setVisibility(View.INVISIBLE);
        ViseLog.d("doDeleteItem currentIndex:" + currentIndex);
    }


    /**
     * 删除音乐
     */
    private void deleteMusic() {
        if (mDir != "") {
            stopMusic();
            mDir = "";
            musicTimes = 0;
            sbVoice.setVisibility(View.INVISIBLE);
            timeDatas.clear();
            timeAdapter.notifyDataSetChanged();
            ToastUtils.showShort(SkinManager.getInstance().getTextById(R.string.ui_create_delete_music_tips));
            rl_delete_music.setVisibility(View.GONE);
            tvMusicTime.setVisibility(View.INVISIBLE);
            if (list_frames.size() > 0) {
                list_frames.remove(list_frames.size() - 1);
                adapter.setMusicTime(0);
                adapter.notifyDataSetChanged();
            }

            if (isFinishFramePlay) {
                setEnable(true);
            }

        }
    }

    public void startPlayAction() {

        recyclerViewTimesHide.setVisibility(View.GONE);
        rl_delete_music.setVisibility(View.GONE);

        //取消修改状态
        goneEditFrameLayout();
        change = false;
        if (list_frames.size() > 0) {
            ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
            adapter.setDefSelect(-1);
        }

        if (mDir.equals("") && list_frames.size() <= 0) {
            return;
        }

        if (mediaPlayer != null && mediaPlayer.isPlaying() && !mDir.equals("")) {
            ivPlay.setImageResource(R.drawable.icon_play_music);
            pause();
            doPlayCurrentFrames();
            ViseLog.d("setEnable true");
            setEnable(true);
            ivAddFrame.setEnabled(true);
            ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
            playFinish = true;
        } else {
            ViseLog.d("doPlayCurrentFrames");
            if (recyclerViewTimes != null) {
                recyclerViewTimes.smoothScrollToPosition(0);
            }
            if (list_frames.size() > 0) {
                ViseLog.d("getNewPlayerState:" + mHelper.getNewPlayerState());
                if (scroll == 0 && mHelper.getNewPlayerState() != NewActionPlayer.PlayerState.PLAYING) {
                    ViseLog.d("recyclerViewFrames smoothScrollToPosition");
                    recyclerViewFrames.smoothScrollToPosition(0);
                }

            }
            ivPlay.setImageResource(R.drawable.ic_pause);
            doPlayCurrentFrames();
            play();

        }
    }

    private void doPlayCurrentFrames() {

        ViseLog.d("state:" + mHelper.getNewPlayerState());
        resetState();
        if (list_frames.size() == 0) {
            return;
        }
        if (mHelper.getNewPlayerState() == NewActionPlayer.PlayerState.PLAYING) {
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_pause_or_continue,
                    getEditingActions());
            ViseLog.d("doPlayCurrentFrames 1");


        } else if (mHelper.getNewPlayerState() == NewActionPlayer.PlayerState.PAUSING) {
            setEnable(false);
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_pause_or_continue,
                    getEditingActions());
            ViseLog.d("doPlayCurrentFrames 2");
        } else {
            setEnable(false);
            if (musicTimes != 0) {
                if (list_frames.size() < 2) {
                    return;
                }
            } else {
                if (list_frames.size() == 0) {
                    return;
                }
            }
//            isFinishFramePlay = false;
            if (mDir != "" && mediaPlayer != null) {
                ViseLog.d("current pos:" + mediaPlayer.getCurrentPosition());
                if (mediaPlayer.getCurrentPosition() == 0) {
                    ViseLog.d("只在音频播完状态下才可以从头开始播:" + mediaPlayer.getCurrentPosition());
                    mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_play,
                            getEditingActions());
                }
            } else {
                mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_play,
                        getEditingActions());
            }
        }
    }

    private NewActionInfo getEditingActions() {

        List<FrameActionInfo> frames = new ArrayList<FrameActionInfo>();
        frames.add(FrameActionInfo.getDefaultFrame());
        if (musicTimes == 0) {
            for (int i = 0; i < list_frames.size(); i++) {
                frames.add(((FrameActionInfo) list_frames.get(i).get(
                        ActionsEditHelper.MAP_FRAME)));
            }
        } else {
            for (int i = 0; i < list_frames.size() - 1; i++) {
                frames.add(((FrameActionInfo) list_frames.get(i).get(
                        ActionsEditHelper.MAP_FRAME)));
            }
        }

        mCurrentNewAction.frameActions = frames;
        ViseLog.d("mCurrentNewAction:" + mCurrentNewAction.frameActions.toString());
        return mCurrentNewAction;
    }

    private NewActionInfo getEditingPreviewActions() {
        List<FrameActionInfo> frames = new ArrayList<FrameActionInfo>();
        frames.add(FrameActionInfo.getDefaultFrame());
        for (int i = 0; i < list_autoFrames.size(); i++) {
            frames.add(((FrameActionInfo) list_autoFrames.get(i).get(
                    ActionsEditHelper.MAP_FRAME)));
        }
        mCurrentNewAction.frameActions = frames;
        ViseLog.d("mCurrentNewAction:" + mCurrentNewAction.frameActions.toString());
        return mCurrentNewAction;
    }


    private void releaseFrameDatas() {

        int frame_size = -1;
        try {
            frame_size = mCurrentNewAction.frameActions.size();
        } catch (Exception e) {
            frame_size = -1;
        }
        if (frame_size == -1) {
            return;
        }
        for (int i = 0; i < frame_size; i++) {

            FrameActionInfo info = mCurrentNewAction.frameActions.get(i);

            Map map = new HashMap<String, Object>();
            map.put(ActionsEditHelper.MAP_FRAME, info);
            /*map.put(ActionsEditHelper.MAP_FRAME_NAME, this.getResources()
                    .getString(R.string.ui_readback_index)
                    + (lst_actions_adapter_data.size() + 1));*/
            map.put(ActionsEditHelper.MAP_FRAME_NAME, (list_frames.size() + 1));
            map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time + "ms");

            list_frames.add(map);
        }

    }


    /**
     * 点击放大事件
     */
    public void ivZoomPlus() {
        ViseLog.d("currentPlus:" + currentPlus);

        if (list_frames.size() <= 0) {
            return;
        }

        if (currentMinus != 1) {
            adapter.scaleItem(1);

            currentPlus = 1;
            currentMinus = 1;
            if (timeDatas.size() > 0) {
                timeAdapter.scaleItem(1);
            }

            tvZoomPlus.setText("");
            tvZoomMinus.setText("");

        } else if (currentPlus == 1) {
            adapter.scaleItem(2);
            if (timeDatas.size() > 0) {
                timeAdapter.scaleItem(2);
            }
            tvZoomPlus.setText("2");
            currentPlus = 2;
        } else if (currentPlus == 2) {
            adapter.scaleItem(3);
            if (timeDatas.size() > 0) {
                timeAdapter.scaleItem(3);
            }
            tvZoomPlus.setText("3");
            currentPlus = 3;
        } else if (currentPlus == 3) {
            adapter.scaleItem(4);
            if (timeDatas.size() > 0) {
                timeAdapter.scaleItem(4);
            }
            tvZoomPlus.setText("4");
            currentPlus = 4;
        }


    }

    /**
     * 点击缩小事件
     */
    public void ivZoomMins() {
        ViseLog.d("currentMinus:" + currentMinus);

        if (list_frames.size() <= 0) {
            return;
        }

        if (currentPlus != 1) {
            adapter.scaleItem(1);
            if (timeDatas.size() > 0) {
                timeAdapter.scaleItem(1);
            }
            currentMinus = 1;
            currentPlus = 1;
            tvZoomPlus.setText("");
            tvZoomMinus.setText("");
        } else if (currentMinus == 1) {
            adapter.scaleItem(-1);
            if (timeDatas.size() > 0) {
                timeAdapter.scaleItem(-1);
            }
            tvZoomMinus.setText("2");
            currentMinus = -1;
        } else if (currentMinus == -1) {
            adapter.scaleItem(-2);
            if (timeDatas.size() > 0) {
                timeAdapter.scaleItem(-2);
            }
            tvZoomMinus.setText("3");
            currentMinus = -2;
        } else if (currentMinus == -2) {
            adapter.scaleItem(-3);
            if (timeDatas.size() > 0) {
                timeAdapter.scaleItem(-3);
            }
            tvZoomMinus.setText("4");
            currentMinus = -4;
        }
    }

    /**
     * 左侧基础动作跟高级动作
     *
     * @param type 1基础动作 2高级动作
     */
    public void showPrepareActionDialog(int type) {
        if (null == mPrepareActionUtil) {
            mPrepareActionUtil = new PrepareActionUtil(mContext);
        }
        mPrepareActionUtil.showActionDialog(type, this);
    }


    public void showPrepareMusicDialog() {
        if (null == mPrepareMusicUtil) {
            mPrepareMusicUtil = new PrepareMusicUtil(mContext);
        }
        mPrepareMusicUtil.showMusicDialog(this);
    }

    /**
     * 初始化Robot图片宽高
     */
    private void initRobot() {
        // 获取屏幕密度（方法2）
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        float density = dm.density;
        ViseLog.d("density:" + density);

        ivRobot.setLayoutParams(ActionConstant.getIvRobotParams(density, ivRobot));
        ViseLog.d("ivRobot:" + ivRobot.getWidth() + "/" + ivRobot.getHeight());
        ivHandLeft.setLayoutParams(ActionConstant.getIvRobotParams(density, ivHandLeft));
        ViseLog.d("ivHandLeft:" + ivHandLeft.getWidth() + "/" + ivHandLeft.getHeight());
        ivHandRight.setLayoutParams(ActionConstant.getIvRobotParams(density, ivHandRight));
        ViseLog.d("ivHandRight:" + ivHandRight.getWidth() + "/" + ivHandRight.getHeight());
        ivLegLeft.setLayoutParams(ActionConstant.getIvRobotParams(density, ivLegLeft));
        ViseLog.d("ivLegLeft:" + ivLegLeft.getWidth() + "/" + ivLegLeft.getHeight());
        ivLegRight.setLayoutParams(ActionConstant.getIvRobotParams(density, ivLegRight));
        ViseLog.d("ivLegRight:" + ivLegRight.getWidth() + "/" + ivLegRight.getHeight());


    }

    /**
     * 基本动作和高级动作回调添加
     *
     * @param prepareDataModel
     */
    @Override
    public void onActionConfirm(PrepareDataModel prepareDataModel) {
        if (prepareDataModel == null) {
            return;
        }
        List<ActionDataModel> list = prepareDataModel.getList();
        for (int i = 0; i < list.size(); i++) {
            String time = list.get(i).getXmlRunTime();
            String angles = list.get(i).getXmldata();

            FrameActionInfo info = new FrameActionInfo();
            info.eng_angles = angles;
            info.eng_time = Integer.valueOf(time);      //暂时用100ms,用实际的有点问题
            info.totle_time = Integer.valueOf(time);

            Map addMap = new HashMap<String, Object>();
            addMap.put(ActionsEditHelper.MAP_FRAME, info);
            String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
            addMap.put(ActionsEditHelper.MAP_FRAME_NAME, (currentIndex) + "");
            addMap.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);

            ViseLog.d("list_frames size:" + list_frames.size());
            if (musicTimes == 0) {
                list_frames.add(addMap);
                currentIndex++;
            } else {
                handleFrameAndTime(addMap);
            }

        }
        adapter.notifyDataSetChanged();
        changeSaveAndPlayState();
    }

    public static final int MSG_AUTO_READ = 1000;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_AUTO_READ) {
                needAdd = true;
                ViseLog.d("adddddd:" + autoRead);

                if (autoRead) {
                    readEngOneByOne();
                }

            } else if (msg.what == 0) {

                //更新进度
                if (playFinish) {
                    return;
                }
                int position = mediaPlayer.getCurrentPosition();
                ViseLog.d("msg what 0 position:" + position);
                if (position != 0) {
                    tvMusicTime.setText(TimeUtils.getTimeFromMillisecond((long) position));
                }

                int time = mediaPlayer.getDuration();
                int max = sbVoice.getMax();

                sbVoice.setProgress(mediaPlayer.getCurrentPosition());
            } else if (msg.what == 1) {
                if (playFinish) {
                    ivPlay.setImageResource(R.drawable.icon_play_music);
                }
            }
        }
    };

    public void addFrameOnClick() {
        ViseLog.d("ivAddFrame");

        if (autoRead) {
            mHandler.removeMessages(MSG_AUTO_READ);
            autoRead = false;
            ivAutoRead.setImageResource(R.drawable.actions_edit_auto_selector);
            tvRecordIndex.setText("");
            tvRecordTime.setText("");
            rlRecordingState.setVisibility(View.INVISIBLE);
            needAdd = false;
            autoAng = "";
            ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
            setButtonEnable(true);
            DialogPreview dialogPreview = new DialogPreview(mContext, list_autoFrames, this);
            dialogPreview.show();
            ViseLog.d("list_autoFrames:" + list_autoFrames.toString());
        } else if (cut) {
            adapter.notifyDataSetChanged();
            adapter.setDefSelect(-1);
            ivCancelChange.setVisibility(View.INVISIBLE);
            cut = false;
        } else {
            if (ids.size() <= 0) {
                showLostDialog(0, SkinManager.getInstance().getTextById(R.string.ui_create_click_to_cutoff));

                goneEditFrameLayout();
                adapter.setDefSelect(-1);
                return;
            }
            if (mHelper.getNewPlayerState() == NewActionPlayer.PlayerState.PLAYING) {
                return;
            }
            needAdd = true;
            readCount = ids.size();
            ViseLog.d("ivAddFrame:" + readCount);
            readEngOneByOne();
            goneEditFrameLayout();
        }
    }


    public void showEditFrameLayout() {
        rlEditFrame.setVisibility(View.VISIBLE);

    }

    private void goneEditFrameLayout() {
        rlEditFrame.setVisibility(View.GONE);
        copy = false;
//        cut = false;
        ivPaste.setEnabled(false);
        ivPaste.setImageResource(R.drawable.ic_paste_disable);
        adapter.setDefSelect(-1);
        ivAddFrame.setEnabled(true);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);


    }

    /**
     * 音乐回调
     */
    @Override
    public void onMusicConfirm(PrepareMusicModel prepareMusicModel) {
        if (prepareMusicModel == null) {
            return;
        }
        String name = prepareMusicModel.getMusicName();
        int songType = prepareMusicModel.getMusicType();
        ViseLog.d("name:" + name + "songType:" + songType);
        setPlayFile(name + ".mp3", songType);
        changeSaveAndPlayState();
    }

    @Override
    public void onMusicDelete(PrepareMusicModel prepareMusicModel) {
        ViseLog.d("onMusicDelete:" + prepareMusicModel.toString() + "__mDir:" + mDir);
        if (prepareMusicModel.getMusicType() == 1) {
            if (!TextUtils.isEmpty(mDir)) {
                ViseLog.d("path:" + FileTools.tmp_file_cache + "/" + prepareMusicModel.getMusicName());
                if ((FileTools.tmp_file_cache + "/" + prepareMusicModel.getMusicName() + ".mp3").equals(mDir)) {
                    deleteMusic();
                    changeSaveAndPlayState();
                }
            }
        }
    }

    String mCurrentSourcePath;

    private void setPlayFile(String fileName, int type) {

        mCurrentSourcePath = FileTools.tmp_file_cache + "/" + fileName;
        boolean isFileCreateSuccess = false;
        if (type == 0) {
            isFileCreateSuccess = FileTools.writeAssetsToSd("music/" + fileName, mContext, mCurrentSourcePath);
        } else if (type == 1) {
            isFileCreateSuccess = FileTools.copyFile(FileTools.record + File.separator + fileName, mCurrentSourcePath, true);
        }

        ViseLog.d("isFileCreateSuccess:" + isFileCreateSuccess);
        if (isFileCreateSuccess) {

            ViseLog.d("mDir:" + mDir);

            if (mDir.equals("")) {
                try {
//                    mDir = mCurrentSourcePath;
//                    sbVoice.setVisibility(View.VISIBLE);
                    setMusic();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                DialogMusic dialogMusic = new DialogMusic(mContext, this, 0);
                dialogMusic.show();
            }


        }

    }

    @Override
    public void setMusic() {
        try {

            ViseLog.d("setMusic");
            //先清除之前的标记
            mDir = "";
            sbVoice.setVisibility(View.INVISIBLE);
            timeDatas.clear();
            if (timeAdapter != null) {
                timeAdapter.notifyDataSetChanged();
            }
            tvMusicTime.setText("00:00");

            if (list_frames.size() > 0) {
                if (musicTimes != 0) {
                    list_frames.remove(list_frames.size() - 1);
                    adapter.setMusicTime(0);
                    adapter.notifyDataSetChanged();
                }

            }
            musicTimes = 0;

            mDir = mCurrentSourcePath;
            sbVoice.setVisibility(View.VISIBLE);

            mediaPlayer.reset();
            mediaPlayer.setDataSource(mDir);
            mediaPlayer.prepareAsync();//数据缓冲
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
//                    mp.start();
                    mp.seekTo(0);
                    sbVoice.setProgress(0);
                    musicTimes = handleMusicTime(mediaPlayer.getDuration());
                    ViseLog.d("play musicTimes:" + musicTimes);
                    long time = musicTimes;
                    tvMusicTime.setVisibility(View.VISIBLE);
                    tvMusicTime.setText(TimeUtils.getTimeFromMillisecond(time));
                    initTimeFrame();
                    sbVoice.setMax(mediaPlayer.getDuration());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void startAutoRead() {
        setButtonEnable(false);

        autoRead = true;
        ivAutoRead.setImageResource(R.drawable.ic_manual_disable);
        ivAddFrame.setImageResource(R.drawable.ic_stop);
        mHandler.sendEmptyMessage(MSG_AUTO_READ);
        rlRecordingState.setVisibility(View.VISIBLE);
    }

    private void initTimeFrame() {

//        timeDatas = new ArrayList<Map<String, Object>>();
        Map<String, Object> timeMap = new HashMap<String, Object>();
        for (int i = 0; i < musicTimes / 100; i++) {
            timeMap.put(TIME, "100");
            timeMap.put(SHOW, "0");
            timeDatas.add(timeMap);
        }
        ViseLog.d("size:" + timeDatas.size());

        timeHideAdapter = new TimesHideRecycleViewAdapter(mContext, timeDatas);
        recyclerViewTimesHide.setAdapter(timeHideAdapter);
        timeHideAdapter.setOnItemListener(new TimesHideRecycleViewAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int pos, Map<String, Object> data) {
                ViseLog.d("onItemClick pos:" + pos);
                data.put(SHOW, "0");
                data.put(TIME, "100");
                timeDatas.set(pos, data);
                timeHideAdapter.notifyDataSetChanged();
                timeAdapter.notifyDataSetChanged();
            }
        });

        timeAdapter = new TimesRecycleViewAdapter(mContext, timeDatas);
        recyclerViewTimes.setAdapter(timeAdapter);
        timeAdapter.setOnItemListener(new TimesRecycleViewAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, int pos, Map<String, Object> data) {
                ViseLog.d("timeAdapter onItemClick:" + pos);

            }
        });
        recyclerViewTimes.smoothScrollToPosition(0);
        recyclerViewTimes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                firstVisibleItemPosition = layoutManagerTime.findFirstVisibleItemPosition();
                lastVisibleItemPosition = layoutManagerTime.findLastVisibleItemPosition();

                ViseLog.d("firstVisibleItemPosition:" + firstVisibleItemPosition +
                        "-lastVisibleItemPosition:" + lastVisibleItemPosition);
//                sbVoice.setProgress((firstVisibleItemPosition*100/5000)*100);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (touch == 2) {
                    return;

                }
                ViseLog.d("ivZhen scrollBy dx:" + dx + "-dy:" + dy);
                //                recyclerViewFrames.scrollBy(dx, dy);
            }
        });

        recyclerViewTimes.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch = 1;
                return false;
            }
        });

        //根据添加的音乐自动补全动作
        if (list_frames.size() == 0) {
            FrameActionInfo info = new FrameActionInfo();
            info.eng_angles = "";

            info.eng_time = musicTimes;
            info.totle_time = musicTimes;

            Map map = new HashMap<String, Object>();
            map.put(ActionsEditHelper.MAP_FRAME, info);
            String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
            item_name = item_name.replace("#", (list_frames.size() + 1) + "");
            //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
            map.put(ActionsEditHelper.MAP_FRAME_NAME, (list_frames.size() + 1) + "");
            map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);
            list_frames.add(list_frames.size(), map);
            adapter.setMusicTime(musicTimes);
            adapter.notifyDataSetChanged();


        } else {

            //根据当前已添加的动作帧时间计算补全帧时长
            handleAddFrame();

        }

    }

    private void handleAddFrame() {

        int time = 0;

        for (int i = 0; i < list_frames.size(); i++) {
            time += (int) list_frames.get(i).get(ActionsEditHelper.MAP_FRAME_TIME);
        }

        ViseLog.d("handleAddFrame time:" + time);

        int backupTime = musicTimes - time;
        ViseLog.d("handleAddFrame backupTime:" + backupTime);
        if (backupTime <= 0) {
            return;
        }

        FrameActionInfo info = new FrameActionInfo();
        info.eng_angles = "";

        info.eng_time = backupTime;
        info.totle_time = backupTime;

        Map map = new HashMap<String, Object>();
        map.put(ActionsEditHelper.MAP_FRAME, info);
        String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
        item_name = item_name.replace("#", (list_frames.size() + 1) + "");
        //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
        map.put(ActionsEditHelper.MAP_FRAME_NAME, (list_frames.size() + 1) + "");
        map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);
        list_frames.add(list_frames.size(), map);

        adapter.setMusicTime(backupTime);
        adapter.notifyDataSetChanged();


    }

    private int handleMusicTime(int time) {
        int handleTime = 0;
        int yushu = time % 100;
        handleTime = time + (100 - yushu);
        ViseLog.d("handleTime:" + handleTime);
        return handleTime;

    }

    @Override
    public void playAction(PrepareDataModel prepareDataModel) {
        ViseLog.d("previewAction:" + prepareDataModel.toString());
        for (int i = 0; i < prepareDataModel.getList().size(); i++) {
            String time = prepareDataModel.getList().get(i).getXmlRunTime();
            String angles = prepareDataModel.getList().get(i).getXmldata();

            FrameActionInfo info = new FrameActionInfo();
            info.eng_angles = angles;
            info.eng_time = Integer.valueOf(time);
            info.totle_time = Integer.valueOf(time);

            Map addMap = new HashMap<String, Object>();
            addMap.put(ActionsEditHelper.MAP_FRAME, info);
            String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
            item_name = item_name.replace("#", (list_frames.size() + 1) + "");
            //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
            addMap.put(ActionsEditHelper.MAP_FRAME_NAME, (list_frames.size() + 1) + "");
            addMap.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);

            ViseLog.d("list_frames size:" + list_frames.size());
            previewList.add(addMap);
        }
        doPlayPreviewFrames();
        previewList.clear();
    }


    /**
     * 左手掉电
     */
    public void lostLeft() {
        ivHandLeft.setSelected(true);
        if (lostLeftHand) {
            return;
        }
        mHelper.doLostLeftHandAndRead();
        lostLeftHand = true;
        ids.add(1);
        ids.add(2);
        ids.add(3);
        updateAddViewEnable();
        if (!lostRightLeg && !lostLeftLeg && !lostRightHand) {
            showLostDialog(0, SkinManager.getInstance().getTextById(R.string.action_ui_create_click_robot));
        }
    }

    /**
     * 右手掉电
     */
    public void lostRight() {
        ivHandRight.setSelected(true);
        if (lostRightHand) {
            return;
        }
        lostRightHand = true;
        ids.add(4);
        ids.add(5);
        ids.add(6);
        updateAddViewEnable();
        mHelper.doLostRightHandAndRead();
        if (!lostRightLeg && !lostLeftLeg && !lostLeftHand) {

            showLostDialog(0, SkinManager.getInstance().getTextById(R.string.action_ui_create_click_robot));
        }
    }


    @Override
    public void lostLeftLeg() {
        ivLegLeft.setSelected(true);
        lostLeftLeg = true;
        mHelper.doLostLeftFootAndRead();
        ids.add(7);
        ids.add(8);
        ids.add(9);
        ids.add(10);
        ids.add(11);
        updateAddViewEnable();
        if (!lostRightLeg && !lostRightHand && !lostLeftHand) {
            showLostDialog(0, SkinManager.getInstance().getTextById(R.string.action_ui_create_click_robot));

        }
    }

    @Override
    public void lostRightLeg() {
        ivLegRight.setSelected(true);
        lostRightLeg = true;
        mHelper.doLostRightFootAndRead();
        ids.add(12);
        ids.add(13);
        ids.add(14);
        ids.add(15);
        ids.add(16);
        updateAddViewEnable();
        if (!lostLeftLeg && !lostRightHand && !lostLeftHand) {
            showLostDialog(0, SkinManager.getInstance().getTextById(R.string.action_ui_create_click_robot));
        }
    }


    private void updateAddViewEnable() {
        ivAddFrame.setEnabled(true);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
    }


    /**
     * 掉电提示
     *
     * @param type
     * @param content
     */
    public void showLostDialog(final int type, String content) {
//        View contentView = LayoutInflater.from(mContext).inflate(R.layout.action_dialog_tips, null);
//        ViewHolder viewHolder = new ViewHolder(contentView);
//        TextView tvContent = contentView.findViewById(R.id.tv_content);
//        tvContent.setText(content);
//        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//        Display display = windowManager.getDefaultDisplay();
//        int width = (int) ((display.getWidth()) * 0.6); //设置宽度
//        DialogPlus.newDialog(mContext)
//                .setContentHolder(viewHolder)
//                .setContentBackgroundResource(R.drawable.action_dialog_filter_rect)
//                .setGravity(Gravity.CENTER)
//                .setContentWidth(width)
//                .setOnClickListener(new com.orhanobut.dialogplus.OnClickListener() {
//                    @Override
//                    public void onClick(DialogPlus dialog, View view) {
//                        dialog.dismiss();
//                        if (type == 1) {
//                            lostLeftLeg();
//                        } else if (type == 2) {
//                            lostRightLeg();
//                        }
//                    }
//                })
//                .setCancelable(true)
//                .create().show();

        new BaseDialog.Builder(mContext)
                .setMessage(content).
                setConfirmButtonId(com.ubt.baselib.R.string.base_confirm)
                .setConfirmButtonColor(R.color.base_tv_large_black)
                .setButtonOnClickListener(new BaseDialog.ButtonOnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        if (view.getId() == com.ubt.baselib.R.id.button_confirm) {
                            if (type == 1) {
                                lostLeftLeg();
                            } else if (type == 2) {
                                lostRightLeg();
                            }
                            dialog.dismiss();
                        }

                    }
                }).create().show();


    }

    public void doReset() {
        ViseLog.d("doReset");

        String angles = "93#20#66#86#156#127#90#74#95#104#89#89#104#81#76#90";

//        String angles = "90#90#90#90#90#90#90#60#76#110#90#90#120#104#70#90";
        FrameActionInfo info = new FrameActionInfo();
        info.eng_angles = angles;

        info.eng_time = 600;
        info.totle_time = 600;

        Map map = new HashMap<String, Object>();
        map.put(ActionsEditHelper.MAP_FRAME, info);
        String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
        item_name = item_name.replace("#", 1 + "");
        //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
        map.put(ActionsEditHelper.MAP_FRAME_NAME, 1 + "");
        map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);

        mHelper
                .doCtrlAllEng(((FrameActionInfo) map
                        .get(ActionsEditHelper.MAP_FRAME)).getData());

        ids.clear();


    }

    public void doStopPlay() {
        ViseLog.d("doStopPlay");
        if (!isFinishFramePlay) {
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_Stop,
                    getEditingActions());
            doReset();
        }
        if (!playFinish) {
            if (mediaPlayer != null && mediaPlayer.isPlaying() && !mDir.equals("")) {
                pause();
            }
            replayMusic();
            sbVoice.setProgress(0);
            recyclerViewFrames.smoothScrollToPosition(0);
            recyclerViewTimes.smoothScrollToPosition(0);
            setEnable(true);
        }
    }

    private void resetState() {
        lostLeftHand = false;
        lostRightHand = false;
        lostLeftLeg = false;
        lostRightLeg = false;
        ivHandLeft.setSelected(false);
        ivHandRight.setSelected(false);
        ivLegLeft.setSelected(false);
        ivLegRight.setSelected(false);
        ivAddFrame.setEnabled(false);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
        ids.clear();
    }

    public void setButtonEnable(boolean enable) {
        ivReset.setEnabled(enable);
        ivAutoRead.setEnabled(enable);
        ViseLog.d("ivSave setButtonEnable");
        ivSave.setEnabled(enable);
        ivActionLib.setEnabled(enable);
        ivActionLibMore.setEnabled(enable);
        ivActionBgm.setEnabled(enable);
        ivPlay.setEnabled(enable);
        ivHelp.setEnabled(enable);
    }

    public void setEnable(boolean enable) {
        ivReset.setEnabled(enable);
        ivAutoRead.setEnabled(enable);
        if (enable) {
            if (TextUtils.isEmpty(mDir)) {
                if (list_frames.size() == 0) {
                    ivSave.setEnabled(false);
                } else {
                    ivSave.setEnabled(true);
                }

            } else {
                if (list_frames.size() > 1) {
                    ivSave.setEnabled(true);
                } else {
                    ivSave.setEnabled(false);
                }
            }
        } else {
            ivSave.setEnabled(enable);
        }

        ivActionLib.setEnabled(enable);
        ivActionLibMore.setEnabled(enable);
        ivActionBgm.setEnabled(enable);
        ivHelp.setEnabled(enable);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    private int i = 0;

    public void readEngOneByOne() {
        if (ids.size() > 0) {
            ViseLog.d("readEngOneByOne:" + i + "  ids.size==" + ids.size());
            if (i == ids.size()) {
                i = 0;
                addFrame();
            } else {
                if (i <= ids.size()) { //偶现i的值大于ids.size 加此判断
                    mHelper.doLostOnePower(ids.get(i));
                } else {
                    ViseLog.e("i>ids.size:" + i + "  ids.size==" + ids.size());
                }

            }

        } else {
            ViseLog.d("laizhelile");
            change = false;
            adapter.setDefSelect(-1);
            ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
            ivCancelChange.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void changeCurrentItemTime(int time) {
        ((FrameActionInfo) mCurrentEditItem
                .get(ActionsEditHelper.MAP_FRAME)).totle_time = time;
    }

    public void addFrame() {
        String angles = "";
        for (int i = 0; i < init.length; i++) {
            angles += init[i] + "#";
        }

        ViseLog.d("angles:" + angles);

        if (change) {
            change = false;
            ((FrameActionInfo) mCurrentEditItem
                    .get(ActionsEditHelper.MAP_FRAME)).eng_angles = angles;
            ViseLog.d("addFrame time:" + adapter.getTime());
            int time = adapter.getTime();
            if (time != 10) {
                ((FrameActionInfo) mCurrentEditItem
                        .get(ActionsEditHelper.MAP_FRAME)).totle_time = adapter.getTime();
            }

            ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
            ivCancelChange.setVisibility(View.INVISIBLE);
            adapter.setDefSelect(-1);
            adapter.setTime();
            adapter.notifyDataSetChanged();
        } else {

            FrameActionInfo info = new FrameActionInfo();
            info.eng_angles = angles;
            if (autoRead) {
                info.eng_time = 200;
                info.totle_time = 200;
            } else {
                info.eng_time = 470;
                info.totle_time = 470;
            }

            Map map = new HashMap<String, Object>();
            map.put(ActionsEditHelper.MAP_FRAME, info);
            String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
            item_name = item_name.replace("#", (list_frames.size() + 1) + "");
            map.put(ActionsEditHelper.MAP_FRAME_NAME, currentIndex + "");

            map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);
            if (autoRead) {
                ViseLog.d("autoAng:" + autoAng + "angles:" + angles);

                if (autoAng.equals(angles)) {
                    mHandler.sendEmptyMessage(MSG_AUTO_READ);
                    return;
                } else {
                    ViseLog.d("autoAng:" + autoAng + "angles:" + angles);
                    if (autoAng.equals("")) {
                        autoAng = angles;
                        //课程关卡判断
                        if (!isCourseReading) {
                            ViseLog.d("isCourseReading:第一步");
                            list_autoFrames.add(map);
                        }
                    } else {
                        String[] auto = autoAng.split("#");
                        String[] ang = angles.split("#");
                        boolean isNeedAdd = false;
                        for (int i = 0; i < auto.length; i++) {

                            int abs = Integer.valueOf(auto[i]) - Integer.valueOf(ang[i]);
                            if (Math.abs(abs) > 5) {
                                autoAng = angles;
                                isNeedAdd = true;
                                break;
                            }
                        }
                        if (!isNeedAdd) {
                            ViseLog.d("no need");
                            mHandler.sendEmptyMessage(MSG_AUTO_READ);
                            return;
                        } else {
                            if (isCourseReading) {
                                onReacHandData();
                                return;
                            }
                            ViseLog.d("need added");
                            list_autoFrames.add(map);
                            tvRecordIndex.setText(list_autoFrames.size() + "");
                            if (list_autoFrames.size() < 5) {
                                tvRecordTime.setText("00:01");
                            } else {
                                tvRecordTime.setText("" + TimeUtils.getTimeFromMillisecond((long) list_autoFrames.size() * 200));
                            }
                        }
                    }

                }

            }
            if (isCourseReading) {
                if (autoRead) {
                    mHandler.sendEmptyMessage(MSG_AUTO_READ);
                }
                return;
            }
            ViseLog.d("list_frames add");
            if (musicTimes == 0) {
                ViseLog.d("isCourseReading:第二步");
                list_frames.add(map);
                currentIndex++;
            } else {
                // 添加位置和最后补全的时间计算
                ViseLog.d("isCourseReading:第三步");
                handleFrameAndTime(map);
            }

            adapter.notifyDataSetChanged();
            if (musicTimes == 0) {
                if (list_frames.size() > 0) {
                    recyclerViewFrames.smoothScrollToPosition(list_frames.size() - 1);
                }

            } else {
                if (list_frames.size() > 1) {
                    recyclerViewFrames.smoothScrollToPosition(list_frames.size() - 2);
                }

            }


        }
        needAdd = false;
        ViseLog.d("continue read！");
        if (autoRead) {
            mHandler.sendEmptyMessage(MSG_AUTO_READ);
        } else {
            changeSaveAndPlayState();
        }


    }

    /**
     * 机器人角度发生变化
     */
    public void onReacHandData() {

    }

    private void handleFrameAndTime(Map<String, Object> map) {
        if (list_frames.size() == 0) {
            ViseLog.d("list_frames size 0:" + map.get(ActionsEditHelper.MAP_FRAME_TIME));
            list_frames.add(list_frames.size(), map);
            currentIndex++;
            adapter.notifyDataSetChanged();
        } else {
            ViseLog.d("list_frames size :" + list_frames.size());
            list_frames.add(list_frames.size() - 1, map);
            currentIndex++;
            handleFrameTime();


        }
    }

    private boolean overMusicTime = false;

    private void handleFrameTime() {

        int time = 0;

        for (int i = 0; i < list_frames.size() - 1; i++) {
            time += (int) list_frames.get(i).get(ActionsEditHelper.MAP_FRAME_TIME);
        }

        ViseLog.d("handleFrameTime time:" + time + "---musicTimes:" + musicTimes);

        int backupTime = musicTimes - time;
        ViseLog.d("handleFrameTime backupTime:" + backupTime);
        if (backupTime <= 0) {
            if (!overMusicTime) {
                ViseLog.d("1220 ss overMusicTime:" + overMusicTime);
                list_frames.remove(list_frames.size() - 1);
                adapter.notifyDataSetChanged();
                overMusicTime = true;
            }
            return;
        } else {
            ViseLog.d("1220 overMusicTime:" + overMusicTime + "backupTime:" + backupTime);
            overMusicTime = false;
            FrameActionInfo info = new FrameActionInfo();
            info.eng_angles = "";

            info.eng_time = backupTime;
            info.totle_time = backupTime;
            ViseLog.d("backupTime:" + backupTime);

            Map map = new HashMap<String, Object>();
            map.put(ActionsEditHelper.MAP_FRAME, info);
            String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
            item_name = item_name.replace("#", (list_frames.size() + 1) + "");
            //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
            map.put(ActionsEditHelper.MAP_FRAME_NAME, (list_frames.size() + 1) + "");
            map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);
            list_frames.set(list_frames.size() - 1, map);

            adapter.setMusicTime(backupTime);
            adapter.notifyDataSetChanged();
            layoutManager.scrollToPosition(list_frames.size() - 2);


        }

//        layoutManager.scrollToPositionWithOffset(list_frames.size()-2, 0);
//        recyclerViewFrames.smoothScrollToPosition(list_frames.size()-2);

    }

    private List<Map<String, Object>> previewList = new ArrayList<Map<String, Object>>();


    private Date lastTime_play = null;

    private void doPlayPreviewFrames() {

        // 防止过快点击-----------start
        Date curDate = new Date(System.currentTimeMillis());
        float time_difference = 500;
        if (lastTime_play != null) {
            time_difference = curDate.getTime()
                    - lastTime_play.getTime();
        }
        lastTime_play = curDate;
        if (time_difference < 500) {
            return;
        }

        resetState();
        ivAddFrame.setEnabled(true);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);

        doPlayPreview = true;
        if (mHelper.getNewPlayerState() == NewActionPlayer.PlayerState.PLAYING) {
            ViseLog.d("doPlayPreviewFrames Do_Stop");
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_Stop,
                    getPreviewActions());


        } else {
            ViseLog.d("doPlayPreviewFrames Do_play doPlayPreview:" + doPlayPreview);
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_play,
                    getPreviewActions());
            doPlayPreview = true;


        }
    }


    private NewActionInfo getPreviewActions() {
        List<FrameActionInfo> frames = new ArrayList<FrameActionInfo>();
        frames.add(FrameActionInfo.getDefaultFrame());
        for (int i = 0; i < previewList.size(); i++) {
            frames.add(((FrameActionInfo) previewList.get(i).get(
                    ActionsEditHelper.MAP_FRAME)));
        }
        mCurrentNewAction.frameActions = frames;
        ViseLog.d("mCurrentNewAction:" + mCurrentNewAction.frameActions.toString());
        return mCurrentNewAction;
    }


    public String getnum(int num1, int num2) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format((float) num1 / (float) num2 * 100);
        return result;
    }


    public static String accuracy(double num, double total, int scale) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(scale);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total * 100;
        return df.format(accuracy_num);
    }


    public void onPlaying() {
        ViseLog.d("onPlaying");
        isFinishFramePlay = false;
    }


    public void onPausePlay() {
        ViseLog.d("onPausePlay");
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                ivPlay.setImageResource(R.drawable.icon_play_music);
                setEnable(true);
                ivAddFrame.setEnabled(true);
                ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
            }
        });
    }


    public void onFinishPlay() {
        ViseLog.d("onFinishPlay");
        if (doPlayPreview) {
            doPlayPreview = false;
        }
        isFinishFramePlay = true;
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                if (playFinish) {
                    if (mediaPlayer != null && !TextUtils.isEmpty(mDir)) {
                        mediaPlayer.seekTo(0);
                        sbVoice.setProgress(0);
                    }

                    ivPlay.setImageResource(R.drawable.icon_play_music);
                    setEnable(true);
                    layoutManager.scrollToPosition(0);
                    if (recyclerViewTimes != null) {
                        recyclerViewTimes.smoothScrollToPosition(0);
                    }
                }
                ivAddFrame.setEnabled(true);
                ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);
                adapter.setPlayIndex(-1);

            }
        });


    }


    public void onFrameDo(final int index) {
        ViseLog.d("onFrameDo:" + index + "--doPlayPreview:" + doPlayPreview);
        if (doPlayPreview) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (index == 0) {

                } else {
                    if (musicTimes == 0) {
                        layoutManager.scrollToPositionWithOffset(index - 1, 0);
                    } else {
                        if (playFinish) {
                            layoutManager.scrollToPositionWithOffset(index - 1, 0);
                        }
                    }

//                    layoutManager.setStackFromEnd(true);
//                    recyclerViewFrames.smoothScrollToPosition(index-1);
                    adapter.setPlayIndex(index - 1);

                }
            }
        });


    }


    public void onReadEng(byte[] eng_angle) {

        ViseLog.d("onReadEng:" + needAdd);

        if (needAdd) {
            ViseLog.d("onReadEng:" + readCount + "--" + eng_angle[0] + "--" + eng_angle[1]);
            init[ByteHexHelper.byteToInt(eng_angle[0]) - 1] = String.valueOf(ByteHexHelper.byteToInt(eng_angle[1]));
            i++;
            readEngOneByOne();


        }
    }


    @Override
    public void doPlayAutoRead() {

        ViseLog.d("doPlayAutoRead");

        if (mHelper.getNewPlayerState() == NewActionPlayer.PlayerState.PLAYING) {
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_Stop,
                    getEditingPreviewActions());


        } else {
            doPlayPreview = true;
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_play,
                    getEditingPreviewActions());

        }

        resetState();
        ivAddFrame.setEnabled(true);
        ivAddFrame.setImageResource(R.drawable.ic_addaction_enable);

    }


    @Override
    public void cancelAutoData() {
        int count = list_autoFrames.size();
        currentIndex = currentIndex - count;
        ViseLog.d("count:" + count);
        for (int i = 0; i < count; i++) {
            list_frames.remove(list_frames.size() - 1);
        }
        adapter.notifyDataSetChanged();
        changeSaveAndPlayState();
        if (musicTimes != 0) {
            int time = 0;
            for (int i = 0; i < list_frames.size() - 1; i++) {
                time += (int) list_frames.get(i).get(ActionsEditHelper.MAP_FRAME_TIME);
            }

            ViseLog.d("handleFrameTime time:" + time + "---musicTimes:" + musicTimes);

            int backupTime = musicTimes - time;
            if (backupTime < 0 && overMusicTime) {

            } else {
                if (overMusicTime) {
                    FrameActionInfo info = new FrameActionInfo();
                    info.eng_angles = "";

                    info.eng_time = backupTime;
                    info.totle_time = backupTime;
                    ViseLog.d("backupTime:" + backupTime);

                    Map map = new HashMap<String, Object>();
                    map.put(ActionsEditHelper.MAP_FRAME, info);
                    String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
                    item_name = item_name.replace("#", (list_frames.size() + 1) + "");
                    //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
                    map.put(ActionsEditHelper.MAP_FRAME_NAME, (list_frames.size() + 1) + "");
                    map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);
                    list_frames.add(list_frames.size(), map);

                    adapter.setMusicTime(backupTime);
                    adapter.notifyDataSetChanged();
                    overMusicTime = false;
                }
            }
        }

    }

    public void onDisConnect() {
        ((Activity) mContext).finish();
    }

    public interface OnSaveSucessListener {
        void startSave(Intent intent);
    }

    private void changeSaveAndPlayState() {
        ViseLog.d("mDir:" + mDir + "--size:" + list_frames.size());

        if (TextUtils.isEmpty(mDir) && list_frames.size() == 0) {
            ivPlay.setEnabled(false);
        } else {
            ivPlay.setEnabled(true);
        }

        if (TextUtils.isEmpty(mDir)) {
            if (list_frames.size() == 0) {
                ivSave.setEnabled(false);
            } else {
                ivSave.setEnabled(true);
            }

        } else {
            if (list_frames.size() > 1) {
                ivSave.setEnabled(true);
            } else {
                ivSave.setEnabled(false);
            }
        }


    }


    private void handleLastFrame() {
        //根据添加的音乐自动补全动作
        if (list_frames.size() == 0) {
            FrameActionInfo info = new FrameActionInfo();
            info.eng_angles = "";

            info.eng_time = musicTimes;
            info.totle_time = musicTimes;

            Map map = new HashMap<String, Object>();
            map.put(ActionsEditHelper.MAP_FRAME, info);
            String item_name = SkinManager.getInstance().getTextById(R.string.ui_readback_index);
            item_name = item_name.replace("#", (list_frames.size() + 1) + "");
            //map.put(ActionsEditHelper.MAP_FRAME_NAME, item_name);
            map.put(ActionsEditHelper.MAP_FRAME_NAME, (list_frames.size() + 1) + "");
            map.put(ActionsEditHelper.MAP_FRAME_TIME, info.totle_time);
            list_frames.add(list_frames.size(), map);
            adapter.setMusicTime(musicTimes);
            adapter.notifyDataSetChanged();


        } else {

            //根据当前已添加的动作帧时间计算补全帧时长
            handleAddFrame();

        }
    }

    public void doFinish() {
        if (mHelper.getNewPlayerState() == NewActionPlayer.PlayerState.PLAYING) {
            mHelper.doActionCommand(ActionsEditHelper.Command_type.Do_Stop,
                    getEditingPreviewActions());


        }
        doReset();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            playFinish = true;
            mediaPlayer = null;
        }
        mHelper.doEnterOrExitActionEdit((byte) 0x04);
        ((Activity) mContext).finish();
    }


}
