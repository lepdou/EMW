# EMW
EMW(Embedded Middleware) 内嵌单机版中间件服务。在很多场景下，不希望依赖中间件服务端，应用自身内嵌中间件的服务能力。
例如云厂商提供的 SaaS 服务，如果依赖于中间件，则需要顺带中间件一起输出，交付成本，硬件成本，运维成本成本非常高。如果内嵌中间件服务，则可以做到独立输出的能力。

## EMW-Config
封装 Apollo 的客户端，基于应用自身的数据库实现配置中心的能力。使用方式非常简单，只需要在应用的数据库创建一张 emw-config 表即可。
```
        String jdbcUrl = "jdbc:mysql://localhost:3306/emw";
        String username = "root";
        String password = "admin";

        EMWConfigStarter.run(EMWConfigParams.builder().jdbcUrl(jdbcUrl).username(username).password(password).build());

        Config config = ConfigService.getAppConfig();
        
        String key = "someKey";
        config.getProperty(key, "someDefaultValue");
```
