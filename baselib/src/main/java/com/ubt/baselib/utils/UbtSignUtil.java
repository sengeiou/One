package com.ubt.baselib.utils;

import com.ubt.baselib.globalConst.Constant1E;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class UbtSignUtil {


        private static final String SIGN_PART_SEPARATOR = " ";
        private final static String mAppKey = Constant1E.APPKEY;

        public static String sign() {
            long now = System.currentTimeMillis() / 1000;
            return MD5Util.encodeByMD5(now + mAppKey) + SIGN_PART_SEPARATOR + now;
        }
}

