package com.ciel.scatquick.aoptxspi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 日志对象
 */

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class LogInfo implements Serializable {

    protected Long id;

    protected String param;

    protected String method;

    protected String path;

    protected String ip;

    protected String resp;

    protected String userId;

    protected String userName;

    protected Long time;

    protected String date;
}
