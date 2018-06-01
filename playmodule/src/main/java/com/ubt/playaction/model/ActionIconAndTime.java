package com.ubt.playaction.model;

import java.util.HashMap;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class ActionIconAndTime {

    private static String TAG="ActionIconAndTime";
    private static  String[] mActionName = {
            "A secret life",
            "Get up",
            "Rotan-give them",
            "Sirius",
            "Sweet and sour",
            "We are taking off",
            "Happy birthday",
            "We wish you merry christmas",
            "Jingle bells",
            "Eternity and more",
            "Push up",
            "Stretch out",
            "Laugh",
            "Kick your leg",
            "Fly like a bird",
            "High pat on horse",
            "Needle at sea bottom",
            "Crane spreads wings",
            "Hold the lute",
            "Kick with right heel",
            "Grasp bird's tail",
            "Squat",
            "Stretch forward",
            "Arm stretch",
            "Right side bow with kick",
            "Left side bow with kick",
            "Right kick",
            "Left kick",
            "Right waist stretch",
            "Left waist stretch",
            "The deer and the lion",
            "The deer in the cowshed",
            "The donkey and the wolf",
            "The farmer girl",
            "The fisherman and the fish",
            "The fox without a tail",
            "The mice and the cat",
            "The wolf and the lamb",
            "Trojan horse"


    };

    private static String[] mActionTime = {
            "01:18",
            "02:33",
            "01:59",
            "01:00",
            "02:31",
            "02:32",
            "01:30",
            "01:10",
            "01:10",
            "01:00",
            "00:19",
            "00:40",
            "00:08",
            "00:12",
            "00:15",
            "00:11",
            "00:12",
            "00:07",
            "00:08",
            "00:12",
            "00:23",
            "00:10",
            "00:17",
            "00:10",
            "00:17",
            "00:17",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "00:12",
            "01:30",



};
    private static String[] mActionLogo={
            "dance",
            "dance",
            "dance",
            "dance",
            "dance",
            "dance",
            "dance",
            "dance",
            "dance",
            "dance",
            "action",
            "action",
            "action",
            "action",
            "action",
            "taiji",
            "taiji",
            "taiji",
            "taiji",
            "taiji",
            "taiji",
            "yoga",
            "yoga",
            "yoga",
            "yoga",
            "yoga",
            "yoga",
            "yoga",
            "yoga",
            "yoga",
            "story",
            "story",
            "story",
            "story",
            "story",
            "story",
            "story",
            "story",
            "story"
    };
    static HashMap<String, String > mActionIconInfo=new HashMap<>();
    static  HashMap<String,String> mActionTimeInfo=new HashMap<>();
    public static void init(){
        for(int i=0;i<mActionName.length;i++){
            mActionIconInfo.put(mActionName[i],mActionLogo[i]);
            mActionTimeInfo.put(mActionName[i],mActionTime[i]);
        }

    }

    public static String  getImageIcon(String actionName){
        //UbtLog.d(TAG,"actionName"+actionName+"getImageIcon        "+mActionIconInfo.get(actionName));
        return mActionIconInfo.get(actionName);
    };
    public static String getTime(String actionName){
        // UbtLog.d(TAG,"actionName"+actionName+"getTime        "+mActionTimeInfo.get(actionName));
        return mActionTimeInfo.get(actionName);
    }



}
