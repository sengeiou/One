package com.ubt.en.alpha1e.remote.model;

/**
 * @author：liuhai
 * @date：2018/5/4 16:23
 * @modifier：ubt
 * @modify_date：2018/5/4 16:23
 * [A brief description]
 * version
 */

public class RemoteRoleInfo {
    public int roleid;
    public String roleName;
    public String roleIntroduction;
    public int roleIcon;
    public int bz;

    public RemoteRoleInfo(int roleid, String roleName, String roleIntroduction, int roleIcon, int bz) {
        this.roleid = roleid;
        this.roleName = roleName;
        this.roleIntroduction = roleIntroduction;
        this.roleIcon = roleIcon;
        this.bz = bz;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleIntroduction() {
        return roleIntroduction;
    }

    public void setRoleIntroduction(String roleIntroduction) {
        this.roleIntroduction = roleIntroduction;
    }

    public int getRoleIcon() {
        return roleIcon;
    }

    public void setRoleIcon(int roleIcon) {
        this.roleIcon = roleIcon;
    }

    public int getBz() {
        return bz;
    }

    public void setBz(int bz) {
        this.bz = bz;
    }

    @Override
    public String toString() {
        return "RemoteRoleInfo{" +
                "roleid=" + roleid +
                ", roleName='" + roleName + '\'' +
                ", roleIntroduction='" + roleIntroduction + '\'' +
                ", roleIcon=" + roleIcon +
                ", bz=" + bz +
                '}';
    }
}
