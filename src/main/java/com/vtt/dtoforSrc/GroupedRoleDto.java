package com.vtt.dtoforSrc;


import lombok.Data;

import java.util.List;

@Data
public class GroupedRoleDto {
    private String roleId;
    private List<String> downlinkedRoleIds;
}
