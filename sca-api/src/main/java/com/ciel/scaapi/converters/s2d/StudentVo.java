package com.ciel.scaapi.converters.s2d;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentVo {
    // 身份ID
    private Long id;
    // 姓名
    private String name;
    // 年龄
    private Integer age;
    // 电话
    private String mobile;
}
