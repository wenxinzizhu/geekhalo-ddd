### ddd-lite-codegen
> 基于 ddd lite 和 ddd lite spring 体系构建，用于基于领域模型对象中自动生成其他非核心代码。


### 0. 运行原理 
> ddd lite codegen 构建于 apt 技术之上。

框架提供若干注解和注解处理器，在编译阶段，自动生成所需的 Base 类。这些 Base 类随着领域对象的重构而变化，从而大大减少样板代码。
如有特殊需求，可以通过子类进行扩展，而无需修改 Base 类的内容（每次编译，Base 类都会自动生成）。

### 1. 配置
该框架采用两步处理，第一步由 maven 的 apt plugin 完成，第二步由编译器调用 apt 组件完成。

对于 maven 项目，需要添加相关依赖和插件，具体如下：
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.geekhalo</groupId>
    <artifactId>gh-ddd-lite-demo</artifactId>
    <version>1.0.0-SNAPSHOT</version>

    <parent>
        <groupId>com.geekhalo</groupId>
        <artifactId>gh-base-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

    <properties>
        <service.name>demo</service.name>
        <server.name>gh-${service.name}-service</server.name>
        <server.version>v1</server.version>
        <server.description>${service.name} Api</server.description>
        <servlet.basePath>/${service.name}-api</servlet.basePath>
    </properties>

    <dependencies>
        <!-- 添加 ddd 相关支持-->
        <dependency>
            <groupId>com.geekhalo</groupId>
            <artifactId>gh-ddd-lite</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>com.geekhalo</groupId>
            <artifactId>gh-ddd-lite-spring</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>

        <!-- 添加 code gen 依赖，将自动启用 EndpointCodeGenProcessor 处理器-->
        <!--编译时有效即可，运行时，不需要引用-->
        <dependency>
            <groupId>com.geekhalo</groupId>
            <artifactId>gh-ddd-lite-codegen</artifactId>
            <version>1.0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-apt</artifactId>
            <scope>provided</scope>
        </dependency>

        <!-- 持久化主要由 Spring Data 提供支持-->
        <dependency>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>

        <!-- 添加测试支持-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- 添加 Swagger 支持-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>com.mysema.maven</groupId>
                <artifactId>apt-maven-plugin</artifactId>
                <version>1.1.3</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>process</goal>
                        </goals>
                        <configuration>
                            <outputDirectory>target/generated-sources/java</outputDirectory>
                            <processors>
                                <!--添加 Querydsl 处理器-->
                                <processor>com.querydsl.apt.QuerydslAnnotationProcessor</processor>
                                <!--添加 DDD 处理器-->
                                <processor>com.geekhalo.ddd.lite.codegen.DDDCodeGenProcessor</processor>
                            </processors>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>

```
 
### 2. creator
> 领域对象为内部核心对象，绝对不应该暴露给外面。因此，在创建一个新的领域对象时，需要一种机制将所需数据传递到模型中。

常用的机制就是将创建时所需数据封装成一个 dto 对象，通过这个 dto 对象来传递数据，领域对象从 dto 中提取所需数据，完成对象创建工作。

**creator** 就是这种特殊的 Dto，在封装创建对象所需的属性的同时，提供数据到领域对象的绑定操作。

#### 2.1 常规做法

假设，现在有一个 Person 类：
```
@Data
public class Person {
    private String name;
    private Date birthday;
    private Boolean enable;
}
```

我们需要创建新的 Person 对象，比较正统的方式便是，创建一个 PersonCreator，用于封装所需数据：
```
@Data
public class PersonCreator {
    private String name;
    private Date birthday;
    private Boolean enable;
}
```

然后，在 Person 中添加创建方法，如：
```
public static Person create(PersonCreator creator){
    Person person = new Person();
    person.setName(creator.getName());
    person.setBirthday(creator.getBirthday());
    person.setEnable(creator.getEnable());
    return person;
}
```

大家有没有发现问题：
1. Person 和 PersonCreator 包含的属性基本相同
2. 如果在 Person 中添加、移除、修改属性，会同时调整三处（Person、PersonCreator、create 方法），遗漏任何一处，都会导致逻辑错误

对于这种机械而且有规律的场景，是否可以自动搞定？

#### 2.2 @GenCreator 

##### 2.2.1 启用 GenCreator 功能
新建 Person 类，在类上添加 @GenCreator 注解。

```
@GenCreator
@Data
public class Person extends JpaAggregate{
    private String name;
    private Date birthday;
    private Boolean enable;
}

```
##### 2.2.2 编译代码，生成 Base 类

执行 mvn clean compile 命令，在 target/generated-sources/java 对应包下，会出现一个 BasePersonCreator 类，如下：
```
@Data
public abstract class BasePersonCreator<T extends BasePersonCreator> {
  @Setter(AccessLevel.PUBLIC)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "",
      name = "birthday"
  )
  private Date birthday;

  @Setter(AccessLevel.PUBLIC)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "",
      name = "enable"
  )
  private Boolean enable;

  @Setter(AccessLevel.PUBLIC)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "",
      name = "name"
  )
  private String name;

  public void accept(Person target) {
    target.setBirthday(getBirthday());
    target.setEnable(getEnable());
    target.setName(getName());
  }
}
```

该类含有与 Person 一样的属性，并提供了 accept 方法，对 person 对象执行对于属性的 set 操作。

##### 2.2.3 构建 PersonCreator
基于 BasePersonCreator 创建 PersonCreator 类。
```
public class PersonCreator extends BasePersonCreator<PersonCreator>{
}
```

##### 2.2.4 添加静态 create 方法 
基于 BasePersonCreator 类，构建 PersonCreator，如：
```
@GenCreator
@Data
public class Person extends JpaAggregate{
    private String name;
    private Date birthday;
    private Boolean enable;

    public static Person create(PersonCreator creator){
        Person person = new Person();
        creator.accept(person);
        return person;
    }
}
```
此后，Person 属性的变化，将自动应用于 BasePersonCreator 中。

#### 2.3 运行原理

@GenCreator 运行原理如下：
1. 自动读取当前类的 setter 方法；
2. 筛选 **public** 和 **protected** 访问级别的 setter 方法，将其作为属性添加到 BaseXXXCreator 类中；
3. 创建 accept 方法，读取 BaseXXXXCreator 的属性，并通过 setter 方法写回业务对象。  

对于不需要添加到 Creator 的 setter 方法，可以使用 @GenCreatorIgnore 忽略该方法。

细心的同学可能注意到，在 BaseXXXXCreator 类的属性上存在 @ApiModelProperty 注解，该注解为 swagger 注解，用于生成 swagger 文档。
我们可以使用 @Description 注解，标注字段描述信息，这些信息会自动添加的 swagger 文档中。

### 3. updater
> updater 和 creator 非常相似，主要应用在对象修改的场景。

对于对象修改场景有点特殊，及 null 的处理，当用户传递 null 进来，不知道是属性不修改还是属性设置为 null。针对这种场景，常用方案是将其包装在一个 Optional 中，如果 Optional 对应的属性为 null，表示对该属性不做处理；如果 Optional 中包含的 value 为null，表示将属性值设置为 null。

#### 3.1 启用 GenUpdater
在 Person 类上添加 @GenUpdater 注解。
```
@GenUpdater
@GenCreator
@Data
public class Person extends JpaAggregate{
    @Description("名称")
    private String name;
    private Date birthday;
    private Boolean enable;

    public static Person create(PersonCreator creator){
        Person person = new Person();
        creator.accept(person);
        return person;
    }
}
```
#### 3.2 编译代码，生成 Base 类
执行 mvn clean compile， 生成 BasePersonUpdater。
```
@Data
public abstract class BasePersonUpdater<T extends BasePersonUpdater> {
  @Setter(AccessLevel.PRIVATE)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "",
      name = "birthday"
  )
  private DataOptional<Date> birthday;

  @Setter(AccessLevel.PRIVATE)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "",
      name = "enable"
  )
  private DataOptional<Boolean> enable;

  @Setter(AccessLevel.PRIVATE)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "名称",
      name = "name"
  )
  private DataOptional<String> name;

  public T birthday(Date birthday) {
    this.birthday = DataOptional.of(birthday);
    return (T) this;
  }

  public T acceptBirthday(Consumer<Date> consumer) {
    if(this.birthday != null){ 
    	consumer.accept(this.birthday.getValue());
    }
    return (T) this;
  }

  public T enable(Boolean enable) {
    this.enable = DataOptional.of(enable);
    return (T) this;
  }

  public T acceptEnable(Consumer<Boolean> consumer) {
    if(this.enable != null){ 
    	consumer.accept(this.enable.getValue());
    }
    return (T) this;
  }

  public T name(String name) {
    this.name = DataOptional.of(name);
    return (T) this;
  }

  public T acceptName(Consumer<String> consumer) {
    if(this.name != null){ 
    	consumer.accept(this.name.getValue());
    }
    return (T) this;
  }

  public void accept(Person target) {
    this.acceptBirthday(target::setBirthday);
    this.acceptEnable(target::setEnable);
    this.acceptName(target::setName);
  }
}
```
与 BasePersonCreator 很大差别：
1. 属性使用 DataOptional<T> 进行包装；
2. 每个属性提供 T fieldName(Date fieldName) 方法，用于设置对应的属性值；
3. 每个属性提供 T acceptFieldName(Consumer<FieldType> consumer) 方法，在 DataOptional 属性不为空的时候，进行业务处理；
4. 提供 void accept(Target target) 方法，将 BaseXXXXUpdater 中的数据写回到 Target 对象中。

与 BaseXXXCreator 类似，BaseXXXUpdater 也提供 @GenUpdaterIgnore 注解，对方法进行忽略；也可使用  @Description 注解生成 Swagger 文档描述。
与 BaseXXXCreator 的最大差别在于，Updater 机制，只会应用于 public 的 setter 方法。

> 对于不需要更新的属性，建议使用 protected 访问级别，这样只会在 creator 中存在。


#### 3.3 创建 PersonUpdater 类
创建 PersonUpdater 类继承 BasePersonUpdater
```
public class PersonUpdater extends BasePersonUpdater<PersonUpdater>{
}
```
#### 3.4 创建 update 方法
为 Person 类 添加 update 方法
```
public void update(PersonUpdater updater){
    updater.accept(this);
}
```

### 4. dto 
> dto 是大家最熟悉的模式，但这里的 dto，只针对返回数据。请求数据，统一使用 Creator 和 Updater 完成。

#### 4.1 启用 GenDto
为 Person 类添加 @GenDto 注解。
```
@GenUpdater
@GenCreator
@GenDto
@Data
public class Person extends JpaAggregate {
    @Description("名称")
    private String name;
    @Setter(AccessLevel.PROTECTED)
    private Date birthday;
    private Boolean enable;

    public static Person create(PersonCreator creator){
        Person person = new Person();
        creator.accept(person);
        return person;
    }

    public void update(PersonUpdater updater){
        updater.accept(this);
    }
}
```
#### 4.2 编译代码，生成 Base 类
执行 mvn clean compile 生成 BasePersonDto，如下：
```
@Data
public abstract class BasePersonDto extends AbstractAggregateDto implements Serializable {
  @Setter(AccessLevel.PACKAGE)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "",
      name = "birthday"
  )
  private Date birthday;

  @Setter(AccessLevel.PACKAGE)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "",
      name = "enable"
  )
  private Boolean enable;

  @Setter(AccessLevel.PACKAGE)
  @Getter(AccessLevel.PUBLIC)
  @ApiModelProperty(
      value = "名称",
      name = "name"
  )
  private String name;

  protected BasePersonDto(Person source) {
    super(source);
    this.setBirthday(source.getBirthday());
    this.setEnable(source.getEnable());
    this.setName(source.getName());
  }
}

```
#### 4.3 新建 PersonDto
新建 PersonDto 继承自 PersonDto
```
public class PersonDto extends BasePersonDto{
    public PersonDto(Person source) {
        super(source);
    }
}
```

#### 4.3 @GenDto 生成策略
@GenDto 生成策略如下：
1. 查找类所有的 public getter 方法；
2. 为每个 getter 方法添加属性；
3. 新建构造函数，在构造函数中完成目标对象到 BaseXXXDto 的属性赋值。

### 5. converter
> converter 主要针对使用 Jpa 作为存储的场景。

#### 5.1 设计背景
Jpa 对 enum 类型提供了两种存储方式：
1. 存储 enum 的名称；
2. 存储 enum 的定义顺序。
这两个在使用上都存在一定的问题，通常情况下，需要存储自定义 code，因此，需要实现枚举类型的 Converter。

#### 5.2 @GenCodeBasedEnumConverter

##### 5.2.1 启用 GenCodeBasedEnumConverter
新建 PersonStatus 枚举，实现 CodeBasedEnum 接口，添加 @GenCodeBasedEnumConverter 注解：
```
@GenCodeBasedEnumConverter
public enum PersonStatus implements CodeBasedEnum<PersonStatus> {
    ENABLE(1), DISABLE(0);

    private final int code;

    PersonStatus(int code) {
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
```
##### 5.2.2 编译代码，生成 CodeBasedPersonStatusConverter 
执行 mvn clean compile 命令，自动生成 CodeBasedPersonStatusConverter 类
```
public final class CodeBasedPersonStatusConverter implements AttributeConverter<PersonStatus, Integer> {
  public Integer convertToDatabaseColumn(PersonStatus i) {
    return i == null ? null : i.getCode();
  }

  public PersonStatus convertToEntityAttribute(Integer i) {
    if (i == null) return null;
    for (PersonStatus value : PersonStatus.values()){
    	if (value.getCode() == i){
    		return value; 
    	}
    }
    return null;
  }
}
```
##### 5.2.3 应用 Converter 
在 Person 中使用 CodeBasedPersonStatusConverter 转化器：
```
public class Person extends JpaAggregate {
    @Description("名称")
    private String name;
    @Setter(AccessLevel.PROTECTED)
    private Date birthday;
    private Boolean enable;

    @Convert(converter = CodeBasedPersonStatusConverter.class)
    private PersonStatus status;
}
```

### 6. repository
> Repository 是领域驱动设计中很重要的一个组件，一个聚合根对于一个 Repository。

Repository 与基础设施关联紧密，框架通过 @GenSpringDataRepository 提供了 Spring Data Repository 的支持。

#### 6.1 启用 GenSpringDataRepository
在 Person 上添加 @GenSpringDataRepository 注解：
```
@GenSpringDataRepository
@Data
public class Person extends JpaAggregate {
    @Description("名称")
    private String name;
    @Setter(AccessLevel.PROTECTED)
    private Date birthday;
    private Boolean enable;
    @Convert(converter = CodeBasedPersonStatusConverter.class)
    private PersonStatus status;
}

```

#### 6.2 编译代码，生成 Base 类 
执行 mvn clean compile 生成 BasePersonRepository 类
```
interface BasePersonRepository extends AggregateRepository<Long, Person>, Repository<Person, Long>, QuerydslPredicateExecutor<Person> {
}
```
该接口实现了 AggregateRepository<Long, Person>、Repository<Person, Long>、QuerydslPredicateExecutor<Person> 三个接口，其中 AggregateRepository 为 ddd-lite 框架接口，另外两个为 spring data 接口。

#### 6.3 创建 PersonRepository
```
public interface PersonRepository extends BasePersonRepository{
}
```
#### 6.4 使用 PersonRepository
PersonRepository 为 Spring Data 标准定义接口，Spring Data 会为其自动创建代理类，无需我们实现便可以直接注入使用。


#### 6.5 Index 支持
一般情况下，PersonRepository 中的方法能够满足我们大多数需求，如果存在关联关系，可以使用 @Index 处理。
1. 在 Person 中，添加 @Index({"name", "status"}) @QueryEntity 注解
```
@GenSpringDataRepository
@Index({"name", "status"})
@QueryEntity

@Data
public class Person extends JpaAggregate {
    @Description("名称")
    private String name;
    @Setter(AccessLevel.PROTECTED)
    private Date birthday;
    private Boolean enable;
    @Convert(converter = CodeBasedPersonStatusConverter.class)
    private PersonStatus status;
}

``` 

2. 执行 mvn clean compile，查看生成的 PersonRepository
```
interface BasePersonRepository extends AggregateRepository<Long, Person>, Repository<Person, Long>, QuerydslPredicateExecutor<Person> {
  Long countByName(String name);

  default Long countByName(String name, Predicate predicate) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QPerson.person.name.eq(name));;
    booleanBuilder.and(predicate);
    return this.count(booleanBuilder.getValue());
  }

  Long countByNameAndStatus(String name, PersonStatus status);

  default Long countByNameAndStatus(String name, PersonStatus status, Predicate predicate) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QPerson.person.name.eq(name));;
    booleanBuilder.and(QPerson.person.status.eq(status));;
    booleanBuilder.and(predicate);
    return this.count(booleanBuilder.getValue());
  }

  List<Person> getByName(String name);

  List<Person> getByName(String name, Sort sort);

  default List<Person> getByName(String name, Predicate predicate) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QPerson.person.name.eq(name));;
    booleanBuilder.and(predicate);
    return Lists.newArrayList(findAll(booleanBuilder.getValue()));
  }

  default List<Person> getByName(String name, Predicate predicate, Sort sort) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QPerson.person.name.eq(name));;
    booleanBuilder.and(predicate);
    return Lists.newArrayList(findAll(booleanBuilder.getValue(), sort));
  }

  List<Person> getByNameAndStatus(String name, PersonStatus status);

  List<Person> getByNameAndStatus(String name, PersonStatus status, Sort sort);

  default List<Person> getByNameAndStatus(String name, PersonStatus status, Predicate predicate) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QPerson.person.name.eq(name));;
    booleanBuilder.and(QPerson.person.status.eq(status));;
    booleanBuilder.and(predicate);
    return Lists.newArrayList(findAll(booleanBuilder.getValue()));
  }

  default List<Person> getByNameAndStatus(String name, PersonStatus status, Predicate predicate,
      Sort sort) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QPerson.person.name.eq(name));;
    booleanBuilder.and(QPerson.person.status.eq(status));;
    booleanBuilder.and(predicate);
    return Lists.newArrayList(findAll(booleanBuilder.getValue(), sort));
  }

  Page<Person> findByName(String name, Pageable pageable);

  default Page<Person> findByName(String name, Predicate predicate, Pageable pageable) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QPerson.person.name.eq(name));;
    booleanBuilder.and(predicate);
    return findAll(booleanBuilder.getValue(), pageable);
  }

  Page<Person> findByNameAndStatus(String name, PersonStatus status, Pageable pageable);

  default Page<Person> findByNameAndStatus(String name, PersonStatus status, Predicate predicate,
      Pageable pageable) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    booleanBuilder.and(QPerson.person.name.eq(name));;
    booleanBuilder.and(QPerson.person.status.eq(status));;
    booleanBuilder.and(predicate);
    return findAll(booleanBuilder.getValue(), pageable);
  }
}

```
根据索引信息，BasePersonRepository 自动生成了各种查询，这些查询也不用实现，直接注入使用。

### 7. application
> application 是领域模型最直接的使用者。

主要涵盖聚合根中的 Command 命令、Repository 中的查询命令、DomainService 的流程命令等。其中有以聚合根中的 Command 和 Repository 中的查询为主。框架为此提供了自动生成 BaseXXXApplication 的支持。

框架提供 @GenApplication 注解，作为自动生成的入口。

#### 7.1 聚合根的 Command
> 将 @GenApplication 添加到聚合根上，框架自动识别 Command 的方法，并将其添加到 BaseApplication 中。

##### 7.1.1 启用 GenApplication
在 Person 中添加 @GenApplication 注解，为了更好的演示 Command 方法，新增 enable 和 disable 两个方法：
```
@GenApplication
@Data
public class Person extends JpaAggregate {
    @Description("名称")
    private String name;
    @Setter(AccessLevel.PROTECTED)
    private Date birthday;
    private Boolean enable;

    @Convert(converter = CodeBasedPersonStatusConverter.class)
    private PersonStatus status;

    public static Person create(PersonCreator creator){
        Person person = new Person();
        creator.accept(person);
        return person;
    }

    public void update(PersonUpdater updater){
        updater.accept(this);
    }

    public void enable(){
        setStatus(PersonStatus.ENABLE);
    }

    public void disable(){
        setStatus(PersonStatus.DISABLE);
    }
}
```
##### 7.1.2 编译代码，生成 Base 类 
执行 mvn clean compile，生成 BasePersonApplication 接口和 BasePersonApplicationSupport 实现类。
BasePersonApplication 如下：
```
public interface BasePersonApplication {
  Long create(PersonCreator creator);

  void disable(@Description("主键") Long id);

  void update(@Description("主键") Long id, PersonUpdater updater);

  void enable(@Description("主键") Long id);
}
```

BasePersonApplication 主要做了如下工作：
1. 对于 Person 的 create 静态工厂方法，将自动创建 create 方法
2. 对于 Person 的返回为 void 方法（非 setter 方法），将自创建为 command 方法，为其增加一个主键参数

BasePersonApplicationSupport 如下：
```
abstract class BasePersonApplicationSupport extends AbstractApplication implements BasePersonApplication {
  @Autowired
  private DomainEventBus domainEventBus;

  @Autowired
  private PersonRepository personRepository;

  protected BasePersonApplicationSupport(Logger logger) {
    super(logger);
  }

  protected BasePersonApplicationSupport() {
  }

  protected PersonRepository getPersonRepository() {
    return this.personRepository;
  }

  protected DomainEventBus getDomainEventBus() {
    return this.domainEventBus;
  }

  @Transactional
  public Long create(PersonCreator creator) {
    	Person result = creatorFor(this.getPersonRepository())
                .publishBy(getDomainEventBus())
                .instance(() -> Person.create(creator))
                .call(); 
    logger().info("success to create {} using parm {}",result.getId(), creator);
    return result.getId();
  }

  @Transactional
  public void disable(@Description("主键") Long id) {
    	Person result = updaterFor(this.getPersonRepository())
                .publishBy(getDomainEventBus())
                .id(id)
                .update(agg -> agg.disable())
                .call(); 
    logger().info("success to disable for {} using parm ", id);
  }

  @Transactional
  public void update(@Description("主键") Long id, PersonUpdater updater) {
    	Person result = updaterFor(this.getPersonRepository())
                .publishBy(getDomainEventBus())
                .id(id)
                .update(agg -> agg.update(updater))
                .call(); 
    logger().info("success to update for {} using parm {}", id, updater);
  }

  @Transactional
  public void enable(@Description("主键") Long id) {
    	Person result = updaterFor(this.getPersonRepository())
                .publishBy(getDomainEventBus())
                .id(id)
                .update(agg -> agg.enable())
                .call(); 
    logger().info("success to enable for {} using parm ", id);
  }
}
```
BasePersonApplicationSupport 主要完成工作如下：
1. 自动注入 DomainEventBus 和 PersonRepository 等相关资源；
2. 实现聚合根中的 Command 方法，并为其开启事务支持。

##### 7.1.3 创建 PersonApplication 和 PersonApplicationImpl
创建 PersonApplication 和 PersonApplicationImpl 类，具体如下：

PersonApplication：
```
public interface PersonApplication extends BasePersonApplication{
}
```

PersonApplicationImpl：
```
@Service
public class PersonApplicationImpl extends BasePersonApplicationSupport 
        implements PersonApplication {
}
```

#### 7.2 Repository 中的 Query 
> 将 @GenApplication 添加到 Repository 上，框架将当前接口中的方法作为 Query 方法，自动添加到 Application 中。

##### 7.2.1 启用 GenApplication
在 PersonRepository 添加 @GenApplication 注解；
```
@GenApplication
public interface PersonRepository extends BasePersonRepository{
    
}
```

##### 7.2.2 添加查询方法 
在 PersonRepository 中添加要暴露的方法:
```
@GenApplication
public interface PersonRepository extends BasePersonRepository{
    @Override
    Page<Person> findByName(String name, Pageable pageable);

    @Override
    Page<Person> findByNameAndStatus(String name, PersonStatus status, Pageable pageable);
}

```

##### 7.2.3 编译代码，生成 Base 类 
执行 mvn clean compile 查看现在的 BasePersonApplication 和 BasePersonApplicationSupport。

BasePersonApplication：
```
public interface BasePersonApplication {
  Long create(PersonCreator creator);

  void update(@Description("主键") Long id, PersonUpdater updater);

  void enable(@Description("主键") Long id);

  void disable(@Description("主键") Long id);

  Page<PersonDto> findByName(String name, Pageable pageable);

  Page<PersonDto> findByNameAndStatus(String name, PersonStatus status, Pageable pageable);
}

```
可见，查询方法已经添加到 BasePersonApplication 中。

BasePersonApplicationSupport：
```
abstract class BasePersonApplicationSupport extends AbstractApplication implements BasePersonApplication {
  @Autowired
  private DomainEventBus domainEventBus;

  @Autowired
  private PersonRepository personRepository;

  protected BasePersonApplicationSupport(Logger logger) {
    super(logger);
  }

  protected BasePersonApplicationSupport() {
  }

  protected PersonRepository getPersonRepository() {
    return this.personRepository;
  }

  protected DomainEventBus getDomainEventBus() {
    return this.domainEventBus;
  }

  @Transactional
  public Long create(PersonCreator creator) {
    	Person result = creatorFor(this.getPersonRepository())
                .publishBy(getDomainEventBus())
                .instance(() -> Person.create(creator))
                .call(); 
    logger().info("success to create {} using parm {}",result.getId(), creator);
    return result.getId();
  }

  @Transactional
  public void update(@Description("主键") Long id, PersonUpdater updater) {
    	Person result = updaterFor(this.getPersonRepository())
                .publishBy(getDomainEventBus())
                .id(id)
                .update(agg -> agg.update(updater))
                .call(); 
    logger().info("success to update for {} using parm {}", id, updater);
  }

  @Transactional
  public void enable(@Description("主键") Long id) {
    	Person result = updaterFor(this.getPersonRepository())
                .publishBy(getDomainEventBus())
                .id(id)
                .update(agg -> agg.enable())
                .call(); 
    logger().info("success to enable for {} using parm ", id);
  }

  @Transactional
  public void disable(@Description("主键") Long id) {
    	Person result = updaterFor(this.getPersonRepository())
                .publishBy(getDomainEventBus())
                .id(id)
                .update(agg -> agg.disable())
                .call(); 
    logger().info("success to disable for {} using parm ", id);
  }

  protected <T> List<T> convertPersonList(List<Person> src, Function<Person, T> converter) {
    if (CollectionUtils.isEmpty(src)) return Collections.emptyList();
    return src.stream().map(converter).collect(Collectors.toList());
  }

  protected <T> Page<T> convvertPersonPage(Page<Person> src, Function<Person, T> converter) {
    return src.map(converter);
  }

  protected abstract PersonDto convertPerson(Person src);

  protected List<PersonDto> convertPersonList(List<Person> src) {
    return convertPersonList(src, this::convertPerson);
  }

  protected Page<PersonDto> convvertPersonPage(Page<Person> src) {
    return convvertPersonPage(src, this::convertPerson);
  }

  @Transactional(
      readOnly = true
  )
  public <T> Page<T> findByName(String name, Pageable pageable, Function<Person, T> converter) {
    Page<Person> result = this.getPersonRepository().findByName(name, pageable);
    return convvertPersonPage(result, converter);
  }

  @Transactional(
      readOnly = true
  )
  public Page<PersonDto> findByName(String name, Pageable pageable) {
    Page<Person> result = this.getPersonRepository().findByName(name, pageable);
    return convvertPersonPage(result);
  }

  @Transactional(
      readOnly = true
  )
  public <T> Page<T> findByNameAndStatus(String name, PersonStatus status, Pageable pageable,
      Function<Person, T> converter) {
    Page<Person> result = this.getPersonRepository().findByNameAndStatus(name, status, pageable);
    return convvertPersonPage(result, converter);
  }

  @Transactional(
      readOnly = true
  )
  public Page<PersonDto> findByNameAndStatus(String name, PersonStatus status, Pageable pageable) {
    Page<Person> result = this.getPersonRepository().findByNameAndStatus(name, status, pageable);
    return convvertPersonPage(result);
  }
}
```
与上个版本相比，新增以下逻辑:
1. 添加 convertPersonList、convertPersonPage等转化方法；
2. 添加 convertPerson 抽象方法，用于完成 Person 到 PersonDto 的转化；
3. 添加 findByNameAndStatus 和 findByName 相关查询方法，并将其标准为只读。

##### 7.2.4 调整 PersonApplicationImpl 
为 PersonApplicationImpl 添加 convertPerson 实现。
```
@Service
public class PersonApplicationImpl extends BasePersonApplicationSupport
        implements PersonApplication {
    @Override
    protected PersonDto convertPerson(Person src) {
        return new PersonDto(src);
    }
}
```

至此，对领域的支持就介绍完了，我们看下我们的 Person 类。
```

@GenUpdater
@GenCreator
@GenDto
@GenSpringDataRepository
@Index({"name", "status"})
@QueryEntity

@GenApplication

@Data
public class Person extends JpaAggregate {
    @Description("名称")
    private String name;
    @Setter(AccessLevel.PROTECTED)
    private Date birthday;
    private Boolean enable;

    @Convert(converter = CodeBasedPersonStatusConverter.class)
    private PersonStatus status;

    public static Person create(PersonCreator creator){
        Person person = new Person();
        creator.accept(person);
        return person;
    }

    public void update(PersonUpdater updater){
        updater.accept(this);
    }

    public void enable(){
        setStatus(PersonStatus.ENABLE);
    }

    public void disable(){
        setStatus(PersonStatus.DISABLE);
    }
}
```
在 Person 上有一堆的 @GenXXXX，感觉有点泛滥，对此，框架提供了两个符合注解，针对聚合和实体进行优化。

### 8. EnableGenForEntity
> 统一开启实体相关的代码生成器。

@EnableGenForEntity 等同于同时开启如下注解：
注解 | 含义
---|---
@GenCreator | 自动生成 BaseXXXCreator
@GenDto | 自动生成 BaseXXXXDto
@GenUpdater | 自动生成 BaseXXXXUpdater

### 9. EnableGenForAggregate
> 统一开启聚合相关的代码生成器。

@EnableGenForAggregate 等同于同时开启如下注解：
注解 | 含义
---|---
@GenCreator | 自动生成 BaseXXXCreator
@GenDto | 自动生成 BaseXXXXDto
@GenUpdater | 自动生成 BaseXXXXUpdater
GenSpringDataRepository | 自动生成基于 Spring Data 的 BaseXXXRepository
@GenApplication | 自动生成 BaseXXXXApplication 以及实现类 BaseXXXXXApplicationSupport

> 对于领域对象的支持，已经非常完成，那对于 Application 的使用者呢？

框架提供了对于 Controller 的支持。
### 10. controller
> 将 @GenController 添加到 XXXXApplication 接口上，将启用对 Controller 的支持。

#### 10.1  启用 GenController
在 PersonApplication 启用 Controller 支持。在 PersonApplication 接口上添加 @GenController("com.geekhalo.ddd.lite.demo.controller.BasePersonController") 
```
@GenController("com.geekhalo.ddd.lite.demo.controller.BasePersonController")
public interface PersonApplication extends BasePersonApplication{
}
```

#### 10.2 编译代码，生成 Base 类 
执行 mvn clean compile，查看生成的 BasePersonController 类:
```

abstract class BasePersonController {
  @Autowired
  private PersonApplication application;

  protected PersonApplication getApplication() {
    return this.application;
  }

  @ResponseBody
  @ApiOperation(
      value = "",
      nickname = "create"
  )
  @RequestMapping(
      value = "/_create",
      method = RequestMethod.POST
  )
  public ResultVo<Long> create(@RequestBody PersonCreator creator) {
    return ResultVo.success(this.getApplication().create(creator));
  }

  @ResponseBody
  @ApiOperation(
      value = "",
      nickname = "disable"
  )
  @RequestMapping(
      value = "{id}/_disable",
      method = RequestMethod.POST
  )
  public ResultVo<Void> disable(@PathVariable("id") Long id) {
    this.getApplication().disable(id);
    return ResultVo.success(null);
  }

  @ResponseBody
  @ApiOperation(
      value = "",
      nickname = "update"
  )
  @RequestMapping(
      value = "{id}/_update",
      method = RequestMethod.POST
  )
  public ResultVo<Void> update(@PathVariable("id") Long id, @RequestBody PersonUpdater updater) {
    this.getApplication().update(id, updater);
    return ResultVo.success(null);
  }

  @ResponseBody
  @ApiOperation(
      value = "",
      nickname = "enable"
  )
  @RequestMapping(
      value = "{id}/_enable",
      method = RequestMethod.POST
  )
  public ResultVo<Void> enable(@PathVariable("id") Long id) {
    this.getApplication().enable(id);
    return ResultVo.success(null);
  }

  @ResponseBody
  @ApiOperation(
      value = "",
      nickname = "findByNameAndStatus"
  )
  @RequestMapping(
      value = "/_find_by_name_and_status",
      method = RequestMethod.POST
  )
  public ResultVo<PageVo<PersonDto>> findByNameAndStatus(@RequestBody FindByNameAndStatusReq req) {
    return ResultVo.success(PageVo.apply(this.getApplication().findByNameAndStatus(req.getName(), req.getStatus(), req.getPageable())));
  }

  @ResponseBody
  @ApiOperation(
      value = "",
      nickname = "findByName"
  )
  @RequestMapping(
      value = "/_find_by_name",
      method = RequestMethod.POST
  )
  public ResultVo<PageVo<PersonDto>> findByName(@RequestBody FindByNameReq req) {
    return ResultVo.success(PageVo.apply(this.getApplication().findByName(req.getName(), req.getPageable())));
  }
}
```

该类主要完成：
1. 自动注入 PersonApplication；
2. 为 Command 中的 create 方法创建对于方法，并返回创建后的主键；
3. 为 Command 中的 update 方法创建对于方法，在 path 中添加主键参数，并返回 Void；
4. 为 Query 方法创建对于的方法；
5. 统一使用 ResultVo 作为返回值；
6. 对 Spring Data 中的 Pageable 和 Page 进行封装；
7. 对于多参数方法，创建封装类，使用封装类收集数据；
8. 添加 Swagger 相关注解。

#### 10.3 新建 PersonController
新建 PersonController 实现 BasePersonController。并添加 RequestMapping，设置 base path。
```
@RequestMapping("person")
@RestController
public class PersonController extends BasePersonController{
}
```
#### 10.4 启动，查看 Swagger 文档
至此，Controller 就开发好了，启动项目，输入 http://127.0.0.1:8080/swagger-ui.html 便可以看到相关接口。

![image](http://litao851025.gitee.io/books-image/gh-ddd-lite/swagger-person.png)
 



