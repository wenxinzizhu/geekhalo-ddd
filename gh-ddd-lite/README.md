### DDD 战术组件封装

所涉及的战术组件包括:
1. 实体
2. 值对象
3. 领域服务
4. 领域事件
5. 聚合 
6. 仓库
7. 应用服务


#### 1. 实体
> 一个实体是一个唯一的东西，并且可以在相当长的一段时间内持续变化。

  
一个实体模型就是一个独立的事物。每个实体都拥有一个唯一标识符，可以以他的个体性与所有其他类型相同或不同的实体区分开。大多时候，实体是可变的。也就是说，他的状态随着时间发生变化。

首先，先看下，为支持实体所提供的类：

![image](http://litao851025.gitee.io/books-image/gh-ddd-lite/entity.png)

其中，包括:
1. EntityId 唯一标识支持
2. Entity 支持
3. Validator 验证体系支持

##### 1.1. 唯一标识支持
> 实体的唯一标识，在实体创建时产生，并在整个生命周期中保存不变。

实体的唯一标识，优先使用值对象，以保证其不变性。

通常情况下，实体唯一标识的生成，主要有以下几种方式：
1. 程序生成
2. 持久化机制生成

###### 程序生成
> 该策略属于及早生成方案，在实体创建完成后，已经分配了唯一标识。

为此，框架提供 EntityId 接口，以规范实体的唯一标识（主要用于程序生成方案）。


![image](http://litao851025.gitee.io/books-image/gh-ddd-lite/entityId.png)

唯一标识继承在 ValueObject，是一种特殊的值对象。
1. MongoEntityId 为 MongoEntity 提供的唯一标识父类 
2. JpaEntityId 为 JpaEntity 提供的唯一标识父类

###### 持久化机制生成
> 该策略属于延迟生成方案，只有当实体成功持久化后，才能获得唯一标识。

最常见的是 MySQL 的自增主键，只有在成功保存后，才能拿到主键值。
从设计上来看，这种方案存在些问题，因此框架对此没有提供特殊支持。

将标识生成延迟到实例持久化会有些问题：
1. 事件创建时，需要知道持久化实体的 ID；
2. 如果将实体放入 Set 中，会因为没有 ID，从而导致逻辑错误。

#### 1.2. 委派标识
> 有些 ORM 框架，需要通过自己的方式来处理对象的身份标识。
  
为了解决这个问题，我们需要使用两种标识，一种为领域使用，一种为 ORM 使用。在 ORM 中使用的，我们称为委派标识。
委派标识和领域中的实体标识没有任何关系，委派标识只是为了迎合 ORM 而创建的。
对于外界来说，我们最好将委派标识隐藏起来，因为委派标识并不是领域模型的一部分，将委派标识暴露给外界可能造成持久化漏洞。  
领域标识不需要作为数据库的主键，但大多数情况下，需要设置为唯一键。

框架通过 Entity 体系对委派标识提供支持。

##### 1.3 Entity 支持
> 通过通用属性、基类等对实体提供支持。

###### 通用属性
> 由 Entity 接口提供方法支持，由 AbstractEntity 提供属性支持。

```
public interface Entity<ID> extends Validator{
    /**
     * 获取实体唯一主键
     * @return
     */
    ID getId();

    /**
     * 获取实体当前版本号
     * @return
     */
    int getVersion();

    /**
     * 获取实体创建时间
     * @return
     */
    Date getCreateTime();


    /**
     * 获取实体更新时间
     * @return
     */
    Date getUpdateTime();
}
```
其中，getVersion 主要用于基于乐观锁的并发控制。

AbstractEntity 具体如下：
```
@Getter(AccessLevel.PUBLIC)
@MappedSuperclass
public abstract class AbstractEntity<ID> implements Entity<ID> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntity.class);

    @Version
    @Setter(AccessLevel.PRIVATE)
    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "create_time", nullable = false, updatable = false)
    @Setter(AccessLevel.PROTECTED)
    private Date createTime;

    @Column(name = "update_time", nullable = false)
    @Setter(AccessLevel.PROTECTED)
    private Date updateTime;

    public void prePersist(){
        Date now = new Date();
        this.setCreateTime(now);
        this.setUpdateTime(now);
    }

    public void preUpdate(){
        this.setUpdateTime(new Date());
    }


    @QueryTransient
    public Long getCreateTimeAsMS(){
        return  toMs(getCreateTime());
    }

    @QueryTransient
    public Long getUpdateTimeAsMS(){
        return toMs(getUpdateTime());
    }

    protected Long toMs(Date date){
        return date == null ? null : date.getTime();
    }

    @Override
    public void validate(ValidationHandler handler) {
        for (Field field : FieldUtils.getAllFieldsList(getClass())){
            try {
                Object value = FieldUtils.readField(field, this, true);
                if (value instanceof Validator){
                    ((Validator) value).validate(handler);
                }
                if (value instanceof Collections){
                    ((Collection) value).forEach(v->{
                        if (v instanceof Validator){
                            ((Validator) v).validate(handler);
                        }
                    });
                }
            } catch (IllegalAccessException e) {
                LOGGER.error("failed to get value of {} from {}.", field, this);
            }
        }
    }
}
```
AbstractEntity 主要对通用属性进行封装。
1. prePersist 回调接口，持久化前回调，用于更新创建和更新时间
2. preUpdate 回调接口，更新前回调，用于更新实体的更新时间
3. validate 验证接口，默认实现，遍历所有的属性并对 Validator 接口的对象进行验证

###### 基类支持
> 框架提供了几个通用基类，以用于不同的场景。

基类 | 用途
---|---
JpaEntity | 基于 Jpa 的实体实现，ID 为 Long 类型，使用持久化生成
IdentitiedJpaEntity | 基于 Jpa 的委派标识实体，ID 为 JpaEntityId 子类型，由应用程序维护；使用自增 _id 为数据库主键，为框架服务。
MongoEntity | 基于 MongoDB 的实体实现，ID 为 BigInteger 类型，使用驱动生成
IdentitiedMongoEntity | 基于 MongoDB 的委派标识实体，ID 为 MongoEntityId 子类型，由应用程序维护；使用BigInteger _id 为数据库主键，为框架服务。

##### 1.4 Validator 验证支持
> 验证的主要目的在于检查模型的正确性，检查对象可以是某个属性，也可以是整个对象，甚至是多个对象的组合。即便领域对象的各个属性都是合法的，也不能表示该对象作为一个整体是合法的；同样，单个对象合法也并不能保证对象组合是合法的。
  
###### 属性合法性验证
> 可以使用自封装来验证属性。
  
自封装性要求无论以哪种方式访问数据，即使从对象内部访问数据，都必须通过 getter 和 setter 方法。
一般情况下，我们可以在 setter 方法中，对属性进行合法性验证，比如是否为空、字符长度是否符合要求、邮箱格式是否正确等。
  
###### 验证整个对象
> 要验证整个实体，我们需要访问整个对象的状态----所有对象属性。

延迟验证，就是一种到最后一刻才进行验证的方法。验证过程应该收集所有的验证结果，而不是在一开始遇到非法状态就抛出异常。验证类可以实现规范模式或策略模式。当发现非法状态时，验证类将通知客户方或记录下验证结果以便稍后使用。

有时候，验证逻辑比领域对象本身的变化还快，将验证逻辑嵌入在领域对象中会使领域对象承担太多的职责。此时，我们可以创建一个单独的组件来完成模型验证。在 Java 中设计单独的验证类时，我们可以将该类放在和实体同样的包中，将属性的 getter 方法生命为包级别，这样验证类便能访问这些属性了。

为此，框架提供 Validator 体系处理验证。

![image](http://litao851025.gitee.io/books-image/gh-ddd-lite/validator.png)

**_BusinessException_**
> 业务异常基类。

继承自 RuntimeException，添加 code、msg 等属性，其他的业务异常统一继承自该类。

```
@Data
public class BusinessException extends RuntimeException{
    private final Integer code;
    private final String msg;


    public BusinessException(Integer code, String message) {
        this.code = code;
        this.msg = message;
    }

    public BusinessException(Integer code, String message, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.msg = message;
    }

}
```

**_ValidationException_**
> BusinessException 子类，表示业务验证异常。

```
public class ValidationException extends BusinessException{
    private final List<String> errorMsg = Lists.newArrayList();

    public ValidationException(List<String> msgs) {
        super(404, msgs.stream().collect(Collectors.joining(",")));
        this.errorMsg.addAll(msgs);
    }
}
```

**_Validator_**
> 业务验证。标记某类为提供业务验证逻辑。

```
public interface Validator {
    void validate(ValidationHandler handler);
}
```
使用 ValidationHandler 反馈验证结果。


**_ValidationHandler_**
> 验证处理器，用于处理具有的验证结果。

```
public interface ValidationHandler {
    void handleError(String msg);
}
```
该类主要处理验证结果的收集，应该在所有验证结果收集完成后，才做具体操作。

**_ValidationChecker_**
> 用于检测验证结果，针对验证结果做出不同的反馈，经常与 ValidationHandler 一起使用。

```
public interface ValidationChecker {
    void check();
}
``` 

**_ExceptionBasedValidationHandler_**
> 验证处理器，收集验证结果，并在检测时，以异常的方式暴露验证结果。

```
public class ExceptionBasedValidationHandler extends AbstractValidationHandler implements ValidationChecker {

    @Override
    public void check(){
        if (!CollectionUtils.isEmpty(getErrorMsg())){
            throw new ValidationException(getErrorMsg());
        }
    }
}
```

AbstractValidationHandler 用于收集验证结果，具体如下：
```
public abstract class AbstractValidationHandler implements ValidationHandler {
    private final List<String> errorMsg = Lists.newArrayList();

    @Override
    public void handleError(String msg) {
        this.errorMsg.add(msg);
    }

    public List<String> getErrorMsg(){
        return Collections.unmodifiableList(this.errorMsg);
    }
}
```
  
###### 验证对象组合
> 相比之下，验证对象组合会复杂很多，最后的方式是把这种验证过程创建成一个领域服务。



#### 2. 值对象
#### 3. 领域服务
#### 4. 领域事件
#### 5. 聚合 
#### 6. 仓库
#### 7. 应用服务