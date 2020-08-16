package com.ciel.scatquick.document.db;

import org.aspectj.weaver.ast.Not;

public class MysqlCon {

    static{

       // 必须把字段定义为NOT NULL并设默认值

        //explain :重点关注id，type，possible_keys，key，Extra
    //        select_type表示查询类型，主要用来区别普通查询，子查询，联合查询等。
    //           SIMPLE： 简单的 select 查询,查询中不包含子查询或者UNION；
    //           PRIMARY： 查询中若包含任何复杂的子部分，最外层查询则被标记为；
    //           SUBQUERY： 在SELECT或WHERE列表中包含了子查询；
    //           DERIVED： 在FROM列表中包含的子查询被标记为DERIVED(衍生)，MySQL会递归执行这些子查询, 把结果放在临时表里；
    //          UNION： 若第二个SELECT出现在UNION之后，则被标记为UNION；若UNION包含在FROM子句的子查询中,外层SELECT将被标记为：DERIVED
    //          UNION RESULT： 从UNION表获取结果的SELECT

    //    type表示访问类型，从好到坏依次是：system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL
    //        system：表只有一行记录（等于系统表），这是const类型的特列；
    //        const：表示通过索引一次就找到了；const用于比较主键或者唯一索引。因为只匹配一行数据，所以很快，如将主键置于where列表中，MySQL就能将该查询转换为一个常量；
    //        eq_ref：表示只有一行数据与之匹配，通常是使用了主键索引或者唯一索引；
    //        ref：表示使用了非唯一索引；
    //        range：表示使用了索引来做范围查询
    //        index：表示扫描了全部的索引，一般是用了索引来排序；
    //        ALL：表示全表扫描；

     //   possible_keys表示这次查询可以用到的索引有哪些。

//        Extra 含不适合在其他列中显示但十分重要的额外信息。
//
//        Using filesort：表示mysql会对数据使用一个外部的索引排序，而不是按照表内的索引顺序进行读取。MySQL中无法利用索引完成的排序操作称为“文件排序”。
//        Using temporary：表示使用了临时表，常见于排序 order by 和分组查询 group by。
//        Not exists：MYSQL优化了LEFT JOIN，一旦它找到了匹配LEFT JOIN标准的行， 就不再搜索了。
//        Using index：表示使用了覆盖索引。MySQL直接从索引中过滤不需要的记录并返回命中的结果。这是MySQL服务层完成的，但无需再回表查询记录。
//        Using index condition：这是MySQL 5.6出来的新特性，叫做“索引条件推送”。简单说一点就是MySQL原来在索引上是不能执行如like这样的操作的，但是现在可以了，这样减少了不必要的IO操作，但是只能用在二级索引上。
//        Using where：使用了WHERE从句来限制哪些行将与下一张表匹配或者是返回给用户。
//        注意：Extra列出现Using where表示MySQL服务器将存储引擎返回服务层以后再应用WHERE条件过滤。
//        Using join buffer：表示使用了连接缓存。
//        Impossible where：表示where子句的值总是false，不能用来获取任何元组，比如在一个NOT NULL列上执行is null的查询
    }

    /*
    innodb存储引擎，要求必须有主键，会根据主键建立一个默认索引，
    叫做聚簇索引，innodb的数据文件本身同时也是个索引文件，索引存储结构大致如下：
    15，data：0x07，完整的一行数据，（15,张三,22）
    22，data：完整的一行数据，（22,李四,30）

    就是因为这个原因，innodb表是要求必须有主键的。另外一个是，innodb存储引擎下，如果对某个非主键的字段创建个索引，
    那么最后那个叶子节点的值就是主键的值，因为可以用主键的值到聚簇索引里根据主键值再次查找到数据，即所谓的回表，例如：

    select * from table where name = ‘张三’
    先到name的索引里去找，找到张三对应的叶子节点，叶子节点的data就是那一行的主键，id=15，
    然后再根据id=15，到数据文件里面的聚簇索引（根据主键组织的索引）根据id=15去定位出来id=15这一行的完整的数据


    select * from table where a=1 and b=2 and c=3，你知道不知道，你要怎么建立索引，才可以确保这个SQL使用索引来查询
    你如果要对一个商品表按照店铺、商品、创建时间三个维度来查询，那么就可以创建一个联合索引：shop_id、product_id、gmt_create
    一般来说，你有一个表（product）：shop_id、product_id、gmt_create，你的SQL语句要根据这3个字段来查询，所以你一般来说不是就建立3个索引，一般来说会针对平时要查询的几个字段，建立一个联合索引
    后面在java系统里写的SQL，都必须符合最左前缀匹配原则，确保你所有的sql都可以使用上这个联合索引，通过索引来查询
     create index (shop_id,product_id,gmt_create)
    （1）全列匹配
    这个就是说，你的一个sql里，正好where条件里就用了这3个字段，那么就一定可以用到这个联合索引的：
    select * from product where shop_id=1 and product_id=1 and gmt_create=’2018-01-01 10:00:00’
    （2）最左前缀匹配
    这个就是说，如果你的sql里，正好就用到了联合索引最左边的一个或者几个列表，那么也可以用上这个索引，在索引里查找的时候就用最左边的几个列就行了：
    select * from product where shop_id=1 and product_id=1，这个是没问题的，可以用上这个索引的
    （3）最左前缀匹配了，但是中间某个值没匹配
    这个是说，如果你的sql里，就用了联合索引的第一个列和第三个列，那么会按照第一个列值在索引里找，找完以后对结果集扫描一遍根据第三个列来过滤，第三个列是不走索引去搜索的，就是有一个额外的过滤的工作，但是还能用到索引，所以也还好，例如：
    select * from product where shop_id=1 and gmt_create=’2018-01-01 10:00:00’
    就是先根据shop_id=1在索引里找，找到比如100行记录，然后对这100行记录再次扫描一遍，过滤出来gmt_create=’2018-01-01 10:00:00’的行
    这个我们在线上系统经常遇到这种情况，就是根据联合索引的前一两个列按索引查，然后后面跟一堆复杂的条件，还有函数啥的，但是只要对索引查找结果过滤就好了，根据线上实践，单表几百万数据量的时候，性能也还不错的，简单SQL也就几ms，复杂SQL也就几百ms。可以接受的。
    （4）没有最左前缀匹配
    那就不行了，那就在搞笑了，一定不会用索引，所以这个错误千万别犯
    select * from product where product_id=1，这个肯定不行
    （5）前缀匹配
    这个就是说，如果你不是等值的，比如=，>=，<=的操作，而是like操作，那么必须要是like ‘XX%’这种才可以用上索引，比如说
    select * from product where shop_id=1 and product_id=1 and gmt_create like ‘2018%’
    （6）范围列匹配
    如果你是范围查询，比如>=，<=，between操作，你只能是符合最左前缀的规则才可以范围，范围之后的列就不用索引了
    select * from product where shop_id>=1 and product_id=1
    这里就在联合索引中根据shop_id来查询了
    （7）包含函数
    如果你对某个列用了函数，比如substring之类的东西，那么那一列不用索引
    select * from product where shop_id=1 and 函数(product_id) = 2
    上面就根据shop_id在联合索引中查询

    3.5 索引的缺点以及使用注意
    索引是有缺点的，比如常见的就是会增加磁盘消耗，因为要占用磁盘文件，同时高并发的时候频繁插入和修改索引，会导致性能损耗的。

    我们给的建议，尽量创建少的索引，比如说一个表一两个索引，两三个索引，十来个，20个索引，高并发场景下还可以。

    字段，status，100行，status就2个值，0和1
    你觉得你建立索引还有意义吗？几乎跟全表扫描都差不多了
    select * from table where status=1，相当于是把100行里的50行都扫一遍
     */
}
