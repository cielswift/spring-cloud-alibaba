package com.ciel.scaproducer2.config.vali;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class People implements Serializable {

    @NotSex(groups = Big.class)
    private String act;

    @NotNull(groups = {Big.class,Small.class})
    private Integer age;

    public interface Big{}
    public interface Small{}
}
