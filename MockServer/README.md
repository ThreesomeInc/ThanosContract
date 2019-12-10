# Mock Server

### Assumpsion

* 最理想是每对Consumer-Provider之间所有契约都用同一个port，对使用者最友好，但这样就需要Mock Server里面逐个去匹配incoming的请求应该是那个schema的？除非msg内部能够提供schema信息?
* 暂时退而求其次，每对Consumer-Provider之间的同一个schema共用一个socket port，所有基于这个schema的contract都活在里面

### Phase1 [Done]

#### 启动mock server 
* 获取所有静态已有契约和schema
* 为每份契约yml起一个独立线程和端口处理

#### 每个port对应的Thread：
* 收到request，根据对应的schema去拆解，并根据regex验证field格式
* 匹配所有同一个schema下，这对consumer-provider下的契约列表，找出第一个匹配的
* 根据契约生成response并返回(不做校验)


### Long Term [TODO]

#### Contract Service
##### Schema domain
* id, schema
* CRUD
* 還有契約時候不能刪除schema
* 刪除schema時候應該銷毀對應mock

##### 契約 domain
* Id, 消費者，生產者，schema id, 契約
* CRUD
* 檢驗是否符合schema
* 把修改消息通知mockserver handler

#### Mock service
##### Base
* 收到契約更改， 重新啟動對應mock thread, 更新對應port
* Id, 消費者，生產者，schema id, port
* 線程銷毀
##### Request service
* 從port收到request, 跟關聯的契約list比對，匹配則按契約返回
