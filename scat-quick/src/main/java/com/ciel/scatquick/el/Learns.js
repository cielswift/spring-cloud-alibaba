// 只要命中50%的分词就返回 GET /test_index/test/_search
let a = {
  "query": {
    "match": {
      "content": { //查询字段
        "query": "java 程序员 书 推荐",
        "minimum_should_match": "50%"
      }
    }
  }
}

//
a = {
  "query": {
    "match": {
      "name": "夏培鑫"
    }
  }
}