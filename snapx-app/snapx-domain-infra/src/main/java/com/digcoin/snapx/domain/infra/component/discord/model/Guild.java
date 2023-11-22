package com.digcoin.snapx.domain.infra.component.discord.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class Guild {

    private String id;
    private String name;
    private String icon;
    private boolean owner;
    @JsonProperty("permissions_new")
    private Long permissions;
    private List<String> features;

    public List<Permission> getPermissionList() {
        List<Permission> permissionList = new LinkedList();
        Permission[] permissions = Permission.values();
        int len = permissions.length;

        for(int i = 0; i < len; ++i) {
            Permission permission = permissions[i];
            if (permission.isIn(this.permissions)) {
                permissionList.add(permission);
            }
        }

        return permissionList;
    }
}
