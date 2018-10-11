package com.liul.trc_study_task.window;

public abstract class PermissionInfo {
    private String permission="";
    private String describe="";

    public PermissionInfo(String permission, String describe) {
        this.permission = permission;
        this.describe = describe;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
