​				

# mybatis模块

![image-20201029110752971](/Users/zyn/Library/Application Support/typora-user-images/image-20201029110752971.png)

##基础支持层

* 反射模块

  ​	该模块对Java 原生的反射进行了良好的封装，提供了更加简洁易用的API ，方便上层使调用，并且对反射操作进行了一系列优化，	例如缓存了类的元数据，提高了反射操作的性能。

* 类型转换模块

  ​	别名机制， 该机制是类型转换模块的主要功能之一。

  ​	实现JDBC 类型与Java 类型之间的转换，该功能在为SQL 语句绑定实参以及映射查询结果集时都会涉及。

* 日志模块

  ​	提供详细的日志输出信息;

  ​	能够集成多种日志框架，其日志模块的一个主要功能就是集成第三方日志框架。

* 资源加载模块

  ​	对类加载器进行封装，确定类加载器的使用顺序，并提供了加载类文件以及其他资源文件的功能。

* 解析器模块

  ​    对XPath 进行封装，为MyBatis 初始化时解析mybatis-config.xml 配置文件以及映射配置文件提供支持;

  ​	为处理动态SQL 语句中的占位符提供支持。

* 数据源模块

  ​    身提供了相应的数据源实现;

  ​	提供了与第三方数据源集成的接口.

* 事务模块

  ​    MyBatis 对数据库中的事务进行了抽象,其自身提供了相应的事务接口和简单实现。

* 缓存模块

   	My Batis 中提供了一级缓存和二级缓存，而这两级缓存都是依赖于基础支持层中的缓存模块实现的。

      	MyBatis 中自带的这两级缓存与MyBatis 以及整个应用是运行在同一个JVM中的，共享同一块堆内存。

      	如果这两级缓存中的数据量较大， 则可能影响系统中其他功能的运行，所以当需要缓存大量数据时，优先考虑
   使用Redis 、Memcache 等缓存产品。

* Binding模块

  ​	MyBatis 通过Binding 模块将用户自定义的Mapper 接口与映射配置文件关联起来，系统可以通过调用自定义Mapper 接口中的方法执行相应的SQL 语句完成数据库操作，尽早发现拼写错误。

  开发人员无须编写自定义Mapper 接口的实现， MyBatis 会自动为其创建动态代理对象。

## 核心处理层

* 配置解析

  ​	在MyBatis 初始化过程中，会加载mybatis-config.xml 配置文件、映射配置文件以及Mapper 接口中的注解信息，解析后的配置信息会形成相应的对象并保存到Configuration 对象中。之后，利用该Configuration 对象创建Sq!SessionFactory 对象。

  待MyBatis 初始化之后，开发人员可以通过初始化得到Sq!SessionFactory 创建SqlSession 对象并完成数据库操作。

* SQL解析与scripting模块

   	MyBatis 实现动态SQL 语句的功能，提供了多种动态SQL 语句对应的节点，例如，＜ where＞节点、＜ if>节点、＜ foreach＞节点等。通过这些节点的组合使用， 开发人员可以写出几乎满足所有需求的动态SQL 语句。

  My Batis 中的scripting 模块会根据用户传入的实参，解析映射文件中定义的动态SQL节点，并形成数据库可执行的SQL 语句。之后会处理SQL 语句中的占位符，绑定用户传入的实参。

* SQL执行

  

* 插件

  ​	SQL 语句的执行涉及多个组件，其中比较重要的是Executor 、StatementHandler 、ParameterHandler 和ResultSetHandler。

  Executor 主要负责维护一级缓存和二级缓存，并提供事务管理的相关操作，它会将数据库相关操作委托给StatementHandler 完成。

  StatementHandler 首先通过ParameterHandler 完成SQL 语句的实参绑定；

  然后通过java.sql.Statement 对象执行SQL 语句并得到结果集；

  最后通过ResultSetHandler 完成结果集的映射，得到结果对象并返回。

## 接口层

* SqlSession

  ​	核心是SqlSession 接口，该接口中定义了MyBatis 暴露给应用程序调用的API ，也就是上层应用与MyBatis 交互的桥梁。接口层在接收到调用请求时，会调用核心处理层的相应模块来完成具体的数据库操作。



# mybatis 核心功能

* 将包含标签的复杂数据库操作语句解析为纯粹的SQL语句
* 将数据操作节点和映射接口中的抽象方法进行绑定，在抽象方法被调用时执行数据库操作
* 将输入参数对象转化为数据库操作语句中的参数
* 将数据库操作语句的返回结果转化为对象

# mybatis操作流程

​	mybatis的操作主要分为两大阶段

## mybatis 初始化阶段

​	该阶段用来完成mybatis运行环境的准备工作，只在mybatis启动时运行一次

* 加载驱动

	* 根据配置文件的位置，获取它的输入流InputStream
	* 从配置文件的根节点开始，逐层解析配置文件，也包括相关的映射文件。解析过程中不断将解析结果放入Configuration对象
	* 以配置好的Configuration对象为参数，获取一个SqlSessionFactory对象

## 数据读写阶段

	* 建立连接数据的SqlSession
	* 映射接口文件与映射文件的绑定
	* 映射接口的代理
	* SQL语句的查找
	* 尝试从缓存中查找操作结果，如果找到则返回；如果找不到则继续从数据库中查找
	* 数据库查询
	* 处理结果集
	* 在缓存中记录查询结果
	* 返回查询结果

# mybatis源码结构

​	按照包的功能可以将所有的包分层三大类:

 * 基础功能包

   这些包用来为其他包提供一些外围基础功能，如文件读取功能、反射操作功能等。这些包的特点是功能相对独立，与业务逻辑耦合小。

* 配置解析包

  这些包用来完成配置解析、存储等工作。这些包中的方法主要在系统初始化阶段运行。

* 核心操作包

  这些包用来完成数据库操作。这些包可能会依赖基础功能包提供的基础功能和配置解析包提供的配置信息。

具体划分如下

![image-20201030105125313](/Users/zyn/Library/Application Support/typora-user-images/image-20201030105125313.png)

* 基础功能包
  * exceptions
  * reflection
  * annotations
  * lang
  * type
  * io
  * logging
  * parsing

* 配置解析包
  * bingding
  * builder
  * mapping
  * scripting
  * datasource

* 核心操作包
  * jdbc
  * cache
  * transaction
  * cursor
  * executor
  * session
  * plugin

# exceptions包

​	exceptions中主要是异常相关的类

![image-20201030133035310](/Users/zyn/Library/Application Support/typora-user-images/image-20201030133035310.png)

​	从上图可以看到很多异常并没有在exceptions包中，这就涉及项目规划时分包问题。通常情况下在规划一个项目的包结构时，可以按照以下两种方式进行分包。

 * 按照类型方式分包

   例如将所有的接口放入一个包，将所有的Controller类放入一个包。这种方式从类型上看更为你清晰，但是会将完成同一功能的不同类分散到不同包中，不便于模块化开发。

* 按照功能方式划分

  例如将所有与加/解密有关的类放入一个包，将所有与http请求有关的类放入一个包。这种方式，同一个功能的类内聚性高，便于模块化开发，但会导致同一个包内类的类型混乱。

在项目设计和开发中，推荐优先将功能耦合度高的类放入按照按照功能划分的包中，而将功能耦合度低或供多个功能使用的类放入按照类型划分的包中。

# reflection包

## Type接口及其子类

![image-20201101110847466](/Users/zyn/Library/Application Support/typora-user-images/image-20201101110847466.png)



* Class类

  它代表运行的java程序中的类和接口，枚举类型、注解也都是Class类的子类

* WildcardType接口

  它代表通配符表达式

* TypeVariable接口

  它是类型变量的父接口。例如Map<K, V>中的K，V就是类型变量

* ParameterizedType接口

  它代表参数化的类型。例如Collection<String>

* GenericArrayType接口

  它代表包含ParameterizedType或TypeVariable元素的列表

## 对象工厂子包

​	reflection包下的factory子包是用来基于反射生产出各种对象

![image-20201101112325900](/Users/zyn/Library/Application Support/typora-user-images/image-20201101112325900.png)

## 执行器子包

​	reflection包下的invoker子包能够基于反射实现对象方法的调用和对象属性的读写，invoker是对JDK功能的简单封装

![image-20201101113131825](/Users/zyn/Library/Application Support/typora-user-images/image-20201101113131825.png)

## 属性子包

​	reflection包下的property子包，用来完成与对象属性相关的操作

​	PropertyCopier：通反射的方式复制对象属性

​	PropertyNamer：通过get、set方法名称找出对应的属性

​	PropertyTokenizer：可以处理多层嵌套表达式

## 对象包装器子包

![image-20201102083455501](/Users/zyn/Library/Application Support/typora-user-images/image-20201102083455501.png)

​	wrapper包装了bean、map、collection的对象信息、类型信息，并提供了更多易用的方法

## 反射核心类

![image-20201102092205092](/Users/zyn/Library/Application Support/typora-user-images/image-20201102092205092.png)

​	Reflector类将一个类反射解析后,会将该类的属性、方法等一一归类到以上的各个属性中。

```java
  /**
   * 对应的 Class 类型
   */
  private final Class<?> type;

  /**
   * 可读属性的名称集合，可读属性就是存在相应 getter 方法的属性，初始值为空数纽
   */
  private final String[] readablePropertyNames;

  /**
   * 可写属性的名称集合，可写属性就是存在相应 setter 方法的属性，初始值为空数纽
   */
  private final String[] writablePropertyNames;

  /**
   * 记录了属性相应 的 setter 方法 ， key 是属性名称， value是 Invoker 对象，
   * 它是对 setter 方法对应Method 对象的封装
   */
  private final Map<String, Invoker> setMethods = new HashMap<>();

  /**
   * 属性相应 的 getter 方法集合 ， key 是属 性名称， value 也是 Invoker 对象
   */
  private final Map<String, Invoker> getMethods = new HashMap<>();

  /**
   * 记录了属性相应的 setter 方法 的参数值类型， key 是属性名称， value 是 setter 方法的参数类型
   */
  private final Map<String, Class<?>> setTypes = new HashMap<>();

  /**
   * 记录 了属性相应的 getter 方法的返回位类型， key是属性名称， value 是 getter 方法的返回位类型
   */
  private final Map<String, Class<?>> getTypes = new HashMap<>();

  /**
   *  记录了默认构造方法
   */
  private Constructor<?> defaultConstructor;

  /**
   * 记录了所有属性名称的集合
   */
  private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

```



## 参数名解析器

​	ParamNameResolver用来按顺序列出方法中的虚参，并在获取命名参数是对应响应的实参

## 泛型解析器

​	TypeParameterResolver 主要有三个功能：

 * 解析字段类型

   ```java
   Type resolveFieldType(Field field, Type srcType)
   ```

 * 解析返回值类型

   ```java
   Type resolveReturnType(Method method, Type srcType)
   ```

 * 解析方法参数列表中各个参数的类型

   ```java
   Type[] resolveParamTypes(Method method, Type srcType)
   ```

# annotations与lang包

​	