package com.ubt.en.alpha1e.action.util;

import android.content.Context;

import com.ubt.baselib.customView.popup.HorizontalGravity;
import com.ubt.baselib.customView.popup.VerticalGravity;
import com.ubt.baselib.model1E.LocalActionRecord;
import com.ubt.baselib.skin.SkinManager;
import com.ubt.en.alpha1e.action.R;
import com.ubt.en.alpha1e.action.model.CourseActionModel;
import com.ubt.en.alpha1e.action.model.CourseOne1Content;
import com.vise.log.ViseLog;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：liuhai
 * @date：2018/5/2 19:35
 * @modifier：ubt
 * @modify_date：2018/5/2 19:35
 * [A brief description]
 * version
 */

public class ActionCourseDataManager {

    /**
     * 动作课程播放路径
     */
    public final static String COURSE_ACTION_PATH = "action/course/motion/";

    /**
     * 获取关卡一第一个课时数据
     *
     * @param context
     * @return
     */
    public static List<CourseOne1Content> getCardOneList(Context context) {
        List<CourseOne1Content> one1ContentList = new ArrayList<>();
        CourseOne1Content one1Content1 = new CourseOne1Content();
        one1Content1.setIndex(0);
        one1Content1.setContent(SkinManager.getInstance().getTextById(R.string.actions_lesson_action_axis));
        one1Content1.setId(R.id.ll_frame);
        one1Content1.setVoiceName("{\"filename\":\"id_elephant.wav\",\"playcount\":1}");
        one1Content1.setActionPath(COURSE_ACTION_PATH + "AE_action editor2.hts");
        one1Content1.setTitle(SkinManager.getInstance().getTextById(R.string.actions_lesson_action_frame));
        one1Content1.setDirection(0);
        one1Content1.setX(0);
        one1Content1.setY(-50);
        one1Content1.setVertGravity(VerticalGravity.CENTER);
        one1Content1.setHorizGravity(HorizontalGravity.RIGHT);
        one1ContentList.add(one1Content1);

        CourseOne1Content one1Content3 = new CourseOne1Content();
        one1Content3.setIndex(2);
        one1Content3.setContent(SkinManager.getInstance().getTextById(R.string.actions_lesson_music_axis));
        one1Content3.setId(R.id.rl_musicz_zpne);
        one1Content3.setActionPath(COURSE_ACTION_PATH + "AE_action editor3.hts");
        one1Content3.setTitle(SkinManager.getInstance().getTextById(R.string.actions_lesson_music_axis));
        one1Content3.setX(0);
        one1Content3.setY(-50);
        one1Content3.setVertGravity(VerticalGravity.CENTER);
        one1Content3.setHorizGravity(HorizontalGravity.RIGHT);
        one1Content3.setDirection(0);
        one1ContentList.add(one1Content3);

        CourseOne1Content one1Content4 = new CourseOne1Content();
        one1Content4.setIndex(3);
        one1Content4.setContent(SkinManager.getInstance().getTextById(R.string.actions_lesson_action_frame));
        one1Content4.setId(R.id.iv_reset_index);
        one1Content4.setVoiceName("{\"filename\":\"id_elephant.wav\",\"playcount\":1}");
        one1Content4.setActionPath(COURSE_ACTION_PATH + "AE_action editor4.hts");
        one1Content4.setTitle(SkinManager.getInstance().getTextById(R.string.actions_lesson_action_frame));
        one1Content4.setDirection(0);
        one1Content4.setX(80);
        one1Content4.setY(30);
        one1Content4.setVertGravity(VerticalGravity.ALIGN_BOTTOM);
        one1Content4.setHorizGravity(HorizontalGravity.RIGHT);
        one1ContentList.add(one1Content4);

        CourseOne1Content one1Content5 = new CourseOne1Content();
        one1Content5.setIndex(4);
        one1Content5.setContent(SkinManager.getInstance().getTextById(R.string.actions_lesson_1_add_action));
        one1Content5.setId(R.id.iv_add_frame);
        one1Content5.setVoiceName("{\"filename\":\"id_elephant.wav\",\"playcount\":1}");
        one1Content5.setActionPath(COURSE_ACTION_PATH + "AE_action editor5.hts");
        one1Content5.setTitle(SkinManager.getInstance().getTextById(R.string.actions_lesson_zoom));
        one1Content5.setDirection(1);
        one1Content5.setX(20);
        one1Content5.setY(0);
        one1Content5.setVertGravity(VerticalGravity.CENTER);
        one1Content5.setHorizGravity(HorizontalGravity.LEFT);
        one1ContentList.add(one1Content5);

        return one1ContentList;
    }

    /**
     * 关卡课时及状态
     *
     * @return
     */
    public static List<CourseActionModel> getCourseActionModel(int card, int currentCourse) {
        List<CourseActionModel> list = new ArrayList<>();
        CourseActionModel model1 = null;
        CourseActionModel model2 = null;
        CourseActionModel model3 = null;
        CourseActionModel model4 = null;
        if (card == 1) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_1_items1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_1_items2), 0);
            model3 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_1_items3), 0);
            list.add(model1);
            list.add(model2);
            list.add(model3);
        } else if (card == 2) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_2_items1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_2_items2), 0);
            model3 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_2_items3), 0);
            list.add(model1);
            list.add(model2);
            list.add(model3);
        } else if (card == 3) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_3_item1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_3_item2), 0);
            list.add(model1);
            list.add(model2);
        } else if (card == 4) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_4_item1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_4_item2), 0);
            model3 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_4_item3), 0);
            list.add(model1);
            list.add(model2);
            list.add(model3);
        } else if (card == 5) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_5_item1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_5_item2), 0);
            model3 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_5_item3), 0);
            list.add(model1);
            list.add(model2);
            list.add(model3);
        } else if (card == 6) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_6_item1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_6_item2), 0);
            model3 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_6_item3), 0);
            model4 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_6_item4), 0);
            list.add(model1);
            list.add(model2);
            list.add(model3);
            list.add(model4);
        } else if (card == 7) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_7_item1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_7_item2), 0);
            list.add(model1);
            list.add(model2);
        } else if (card == 8) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_8_item1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_8_item2), 0);
            list.add(model1);
            list.add(model2);
        } else if (card == 9) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_9_item1), 0);
            model2 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_9_item2), 0);
            list.add(model1);
            list.add(model2);
        } else if (card == 10) {
            model1 = new CourseActionModel(SkinManager.getInstance().getTextById(R.string.actions_lesson_10_item), 0);
            list.add(model1);

        }
        if (currentCourse == 1) {
            model1.setStatu(2);
        } else if (currentCourse == 2) {
            model1.setStatu(1);
            model2.setStatu(2);
        } else if (currentCourse == 3) {
            model1.setStatu(1);
            model2.setStatu(1);
            model3.setStatu(2);
        } else if (currentCourse == 4) {
            model1.setStatu(1);
            model2.setStatu(1);
            model3.setStatu(1);
            model4.setStatu(2);
        }

        return list;
    }


    /**
     * 关卡列表页获取数据
     *
     * @param position
     * @param size
     * @return
     */
    public static List<CourseActionModel> getCourseDataList(int position, int size) {
        int level = 1;// 当前第几个课时
        int currentCourse = position + 1;
        LocalActionRecord record = DataSupport.findFirst(LocalActionRecord.class);
        if (null != record) {
            ViseLog.d("getDataList", "record===" + record.toString());
            int course = record.getCourseLevel();
            int recordlevel = record.getPeriodLevel();
            if (course == currentCourse) {//只有当最新记录跟position+1相等时才需要获取到课时
//                if (recordlevel < size) {
//                    level = ++recordlevel;
//                } else if (recordlevel == size) {
//                    level = 1;
//                }
                level = recordlevel;
            }
        }
        return getCourseActionModel(currentCourse, 1);
    }
}
