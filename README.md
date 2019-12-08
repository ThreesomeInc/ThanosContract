# ThanosContract

### Task

#### 定义契约DSL

**假设:**
* 不同consumer/provider的test分在不同folder
* 同一个consumer/provider对的同一个schema的test写在同一个yaml，以"---"分隔
* MVP应该支持schema里面是fix value和正则，远期可以考虑支持自定义function

**Task:**
* [Done] Schema:CSV
* [Done] Test Data: yaml
* regex||fix value
* func(validator & generator)
	 
	
### DemoServer
* [Done] 有一个Demo的Socket request/response

### Service Register

因为socket没办法像Restful那样使用url区分，只能占用port，这样就牵涉到需要大量的port和如何释放的问题。
暂时考虑每个consumer/provider对之间的所有test case会共用一个port。
有请求到这个服务注册发现时候，需要启动对应的端口，用完即毁或设置某个时间段自动销毁

* [TODO] pending

### 基于DSL生成mock server
* [TODO] 基于input和契约都是全量且固定值
    * Parser: read schema.csv
    * Parser: read test.yaml
    * Match input request with contracts, if match, build associated response, else throw exception.	
* [TODO]基于契约有部分正则
* [TODO] 新契约的时候要不要重启？
* [TODO] Long term - 注册接收IP/Port
	
### 基于DSL生成测试
* 测试案例的代码模版  
* 单测？接口测试？怎么集成？
* 注册接收IP/Port
* 异常处理

	
### 测试数据管理
* [TODO] Pending

