package com.ubt.blockly.main.bean;

import java.util.List;

/**
 * @author admin
 * @className
 * @description
 * @date
 * @update
 */


public class BlocklyProjectRequest extends BaseRequest {

    private List<BlocklySaveMode> programParams;

    public List<BlocklySaveMode> getList() {
        return programParams;
    }

    public void setList(List<BlocklySaveMode> programParams) {
        this.programParams = programParams;
    }

    @Override
    public String toString() {
        return "BlocklyProjectRequest{" +
                "programParams=" + programParams +
                '}';
    }
}
