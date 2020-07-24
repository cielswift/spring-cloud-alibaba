PUT /b2b/good/6
{
  "id" : 6,
    "name" : "莫尔森",
    "price" : 25.5
}

GET /b2b/good/6
GET /b2b/good/_search
GET /b2b/good/_search
{
  "query": {
  "bool": {
    "must": [ //must多个条件 and 关系
      {"match": {
          "name": "盘尼"
        }},
      {
        "match": {
          "price": 95.5
        }
      }
    ],
    "should": [ //should多个条件 or 关系
      {"match": {
          "name": "盘尼"
        }},
      {
        "match": {
          "price": 55.5
        }
      }
    ],
    "must_not": [  //must_not 不等于
      {"match": {
          "name": "盘尼"
        }}
    ],
    "filter": { //数据过滤
      "range": {
        "price": { //字段
          "gte": 10,
          "lte": 100
        }
      }
    },

    {  //组合查询  where name = "西林" and (price = 55.5 or price = 25.5)
      "query": {
      "bool": {
        "must": [
          {"match": {
              "name": "西林"
            }},{
            "bool": {"should": [
                {"term": {
                    "price": 55.5
                  }},
                {
                  "term": {
                    "price": 25.5
                  }
                }
              ]}
          }
        ]
      }
    }
    },




  },
  "match": { //简单条件 分词查询
    "name": "盘尼 阿莫" //多个条件 空格隔开
  },
  "term": { //简单条件 精确查询
    "price": "55.5"
  },
  "terms": { //带s 多个值
    "price": [55.5,25.5]
  },
  "range": { //范围查询
    "price": {
      "gte": 10,
      "lte": 100
    }
  },
  "range" : {
    "timestamp" : {
      "gt" : "now-1h" //过去一个小时内的所有文档
    }
  },
  "exists": {  //字段存在的不为null的
    "field": "name"
  }
},
  "_source": ["name","id","price"], //过滤字段
    "sort": [
  {
    "price": { //排序
      "order": "desc"
    }
  }
],
    "from": 0, //分页
    "size": 20, //分页

  {
    "query":{
    "multi_match":
    {
      "query":"夏培鑫",
        "fields":["name","detail"] //多字段匹配
    }
  }
  },


}

GET _analyze  //查看分词器效果
{
  "analyzer": "keyword",
   "text": ["生病要吃药盘尼西林"]
}

POST /b2b/good/2/_update
{
  "doc":{ //更新 必须是doc
  "name" :  "阿莫西林僵尸片"
  }
}

DELETE  /b2b/good/6

