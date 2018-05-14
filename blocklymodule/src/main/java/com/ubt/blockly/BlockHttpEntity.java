package com.ubt.blockly;

import static com.ubt.baselib.globalConst.BaseHttpEntity.BASIC_UBX_SYS;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class BlockHttpEntity {

    /**
     * 保存blockly编程项目
     */
    public static final String SAVE_USER_PROGRAM = BASIC_UBX_SYS + "alpha1e/program/add";

    /**
     * 获取用户的Blockly编程项目
     */
    public static final String GET_USER_PROGRAM = BASIC_UBX_SYS + "alpha1e/program/list";

    /**
     * 获取Blockly编程项目详情
     */
    public static final String GET_USER_PROGRAM_DETAIL = BASIC_UBX_SYS + "alpha1e/program/get";

    /**
     * 删除Blockly编程项目
     */
    public static final String DEL_USER_PROGRAM = BASIC_UBX_SYS + "alpha1e/program/delete";

    /**
     * 修改blockly编程项目
     */
    public static final String UPDATE_USER_PROGRAM = BASIC_UBX_SYS + "alpha1e/program/update";


    /**
     * 获取逻辑编程课程列表
     */
    public static final String BLOCKLY_COURSE_LIST = BASIC_UBX_SYS + "alpha1e/graph/list";

    /**
     * 更新Blockly 课程当前进度
     */
    public static final String UPDATE_BLOCKLY_COURSE = BASIC_UBX_SYS + "alpha1e/graph/update";



}
