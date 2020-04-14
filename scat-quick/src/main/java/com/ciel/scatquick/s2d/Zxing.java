package com.ciel.scatquick.s2d;

import com.github.dozermapper.core.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Zxing {

    @Autowired
    private Mapper dozerMapper;

    public void testDefault(){

        StudentDomain studentDomain =
                new StudentDomain(1024L, "tan日拱一兵", 18, "13996996996");

        long st =System.currentTimeMillis();
        StudentVo studentVo = dozerMapper.map(studentDomain, StudentVo.class); //同名field 的双向映射，即隐式映射

        System.out.println(System.currentTimeMillis()-st);
        //同名字段的数据转换

        long st2 =System.currentTimeMillis();

        StudentVo vo = new StudentVo();
        BeanUtils.copyProperties(studentDomain,vo);

        System.out.println(System.currentTimeMillis()-st2);


    }
}
