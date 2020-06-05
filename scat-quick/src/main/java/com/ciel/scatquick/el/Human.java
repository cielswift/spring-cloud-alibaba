package com.ciel.scatquick.el;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)


/**
 *索引库（indices）--------------------------------Databases 数据库
 *类型（type）-----------------------------Table 数据表
 *文档（Document）----------------Row 行
 *字段（Field）-------------------Columns 列
 */

/**
 * @Document 作用在类，标记实体类为文档对象，一般有两个属性
 * indexName：对应索引库名称
 * type：对应在索引库中的类型
 * shards：分片数量，默认5
 * replicas：副本数量，默认1
 */
@Document(indexName = "cielswift",type = "human", shards = 1, replicas = 0)
public class Human {

    @Id
    protected Long id;

    /**
     * @Field 作用在成员变量，标记为文档的字段，并指定字段映射属性：
     * type：字段类型，是枚举：FieldType，可以是text、long、short、date、integer、object等
     *      text：存储数据时候，会自动分词，并生成索引
     *      keyword：存储数据时候，不会分词建立索引
     *      Numerical：数值类型，分两类
     *          基本数据类型：long、interger、short、byte、double、float、half_float
     *          浮点数的高精度类型：scaled_float
     *          需要指定一个精度因子，比如10或100。elasticsearch会把真实值乘以这个因子后存储，取出时再还原。
     *      Date：日期类型
     *          elasticsearch可以对日期格式化为字符串存储，但是建议我们存储为毫秒值，存储为long，节省空间。
     * index：是否索引，布尔类型，默认是true
     * store：是否存储，布尔类型，默认是false
     * analyzer：分词器名称，这里的ik_max_word即使用ik分词器
     */

    @Field(type = FieldType.Integer)
    protected Integer age;

    @Field(type = FieldType.Text)
    protected String address;

    @Field(type = FieldType.Text)
    protected String name;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    protected String content;

    @Field(type = FieldType.Date)
    protected Date birthday;
}
