package com.ciel.scaapi.util;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class JWTPayload implements Serializable {

    public static final long serialVersionUID = 1L;

    private Long id;

    private String userName;

    private String realName;

    private List<String> authority;

    private String ip;
}
