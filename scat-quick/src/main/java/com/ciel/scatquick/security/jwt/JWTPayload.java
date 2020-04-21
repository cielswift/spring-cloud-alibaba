package com.ciel.scatquick.security.jwt;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class JWTPayload implements Serializable {

    private Long id;

    private String userName;

    private String realName;

    private List<String> authority;

    private String ip;
}
