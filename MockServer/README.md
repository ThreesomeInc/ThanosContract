# Mock Server

### Assumpsion

* 最理想是每对Consumer-Provider之间所有契约都用同一个port，对使用者最友好，但这样就需要Mock Server里面逐个去匹配incoming的请求应该是那个schema的？除非msg内部能够提供schema信息?
* 暂时退而求其次，每对Consumer-Provider之间的同一个schema共用一个socket port，所有基于这个schema的contract都活在里面

### Action

#### 启动mock server [TODO]
* [TODO] 扫描resource/contracts下面所有文件夹
* [TODO] 每个yml契约起一个thread处理socket request/response,该yml内所有契约load到List<Contract>, List<Schema>

#### 每个port对应的Thread：

* [DONE] 收到request，根据对应的schema去拆解，并根据regex验证field格式
* [DONE] 匹配所有同一个schema下，这对consumer-provider下的契约列表，找出第一个匹配的
* [DONE] 根据契约生成response并返回(不做校验)