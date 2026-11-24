package com.spring.teamwork.model.VO;

import lombok.Data;

@Data
public class WitnessInviteVO {
    private String inviteCode;
    private String inviteUrl;
    private Integer expireHours;
}
