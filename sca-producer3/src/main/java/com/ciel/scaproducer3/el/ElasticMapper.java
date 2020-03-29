package com.ciel.scaproducer3.el;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data 的强大之处，就在于你不用写任何DAO处理，自动根据方法名或类的信息进行CRUD操作
 * 只要你定义一个接口，然后继承Repository提供的一些子接口，就能具备各种基本的CRUD功能。
 *
 * findByTitle
 *
 * Keyword	Sample
 * And	findByNameAndPrice
 * Or	findByNameOrPrice
 * Is	findByName
 * Not	findByNameNot
 * Between	findByPriceBetween
 * LessThanEqual	findByPriceLessThan
 * GreaterThanEqual	findByPriceGreaterThan
 * Before	findByPriceBefore
 * After	findByPriceAfter
 * Like	findByNameLike
 * StartingWith	findByNameStartingWith
 * EndingWith	findByNameEndingWith
 * Contains/Containing	findByNameContaining
 * In	findByNameIn(Collection<String>names)
 * NotIn	findByNameNotIn(Collection<String>names)
 * Near	findByStoreNear
 * True	findByAvailableTrue
 * False	findByAvailableFalse
 * OrderBy	findByAvailableTrueOrderByNameDesc
 */
@Repository
public interface ElasticMapper extends ElasticsearchRepository<Human, Long> {

    //默认的注释
    //@Query("{\"bool\" : {\"must\" : {\"field\" : {\"content\" : \"?\"}}}}")
    Page<Human> findByContent(String content, Pageable pageable);

    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"firstCode.keyword\" : \"?\"}}}}")
    Page<Human> findByFirstCode(String firstCode, Pageable pageable);

    @Query("{\"bool\" : {\"must\" : {\"field\" : {\"secordCode.keyword\" : \"?\"}}}}")
    Page<Human> findBySecordCode(String secordCode, Pageable pageable);
}
