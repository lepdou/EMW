# EMW
EMW(Embedded Middleware) 内嵌单机版中间件服务。在很多场景下，不希望依赖中间件服务端，应用自身内嵌中间件的服务能力。
例如云厂商提供的 SaaS 服务，如果依赖于中间件，则需要顺带中间件一起输出，交付成本，硬件成本，运维成本非常高。如果内嵌中间件服务，则可以做到独立输出的能力。

## EMW-Config
在有些场景下，不希望依赖公司的统一配置中心作为配置管理中间件，因为这额外增加了外部依赖。例如：

1. 你的应用是一个开源软件
2. 你的应用作为一个云厂商提供的服务
3. 公司没有统一的配置中心服务，但是又需要动态配置管理的能力

如果依赖外部的配置中心，那么意味着输出的时候需要带上配置中心，这显然会增加输出成本。但是几乎任何应用都依赖配置动态更新的能力，那如何实现呢？一种实现方式就是通过自身应用的数据库实现配置中心的能力。

[emw-config 使用文档](https://github.com/lepdou/EMW/wiki/emw-config-%E4%BD%BF%E7%94%A8%E6%96%87%E6%A1%A3)

## EMW-MQ (未实现)
应用内部经常有这样的场景 1. 集群内广播事件 2. 集群内异步执行，这两个场景可以通过内嵌的消息队列实现。

## EMW-Lock (未实现)
通过应用自身的数据库实现分布式锁的能力。

## EMW-Election （未实现）
通过应用自身的数据库实现分布式选主能力，相比于 raft 之类的协议，通过数据库实现更加简单满足绝大部分场景。

## EMW-Discovery （未实现）
通过应用自身的数据库实现简易的服务注册发现能力

## EMW-Scheduler (未实现)
通过应用自身的数据库实现简易的集群定时任务能力
