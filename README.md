DDD 战术模式详见: https://www.geekhalo.com/categories/%E9%A2%86%E5%9F%9F%E9%A9%B1%E5%8A%A8%E8%AE%BE%E8%AE%A1/%E6%88%98%E6%9C%AF%E6%A8%A1%E5%BC%8F/

### geekhalo-ddd
> geekhalo ddd 项目，主要对 DDD 相关理念进行抽象，并提供多种基础服务。

项目文档以及项目主地址见：https://gitee.com/litao851025/geekhalo-ddd

整个项目的设计理念遵循：
1. 梳理 DDD 相关理念，推进 DDD 的学习；
2. 寻求 DDD 最佳实践，分解、抽象并对其进行封装，最大化其重用性；
3. 使用技术手段，提供对最佳实践的支持，最大程度的降低代码量，使程序员回归业务本质。

### 介绍
针对 DDD 不同层次，对项目进行整体规划。

#### 1. DDD Lite
> DDD Lite 部分，聚焦于战术层，对实体、值对象、领域服务、领域事件、模块、聚合、工厂、仓库等，提供落地支撑。

ddd lite 致力于将程序员从繁重的重复性技术工作中解放出来，让其回归业务本质。因此，在 ddd lite 中大量使用代码生成技术，从而减轻开发压力。

ddd lite 的代码生成理念，来源于领域驱动设计的战术部分，主要围绕 DDD 的核心组件完成。一句话解释，就是开发人员主要负责领域对象建模，其余部分由框架完成。


#### 1.1 **gh-ddd-lite**
> 聚焦于战术模型中最佳实战经验，完成对 DDD 战术概念进行封装。

详见: https://gitee.com/litao851025/geekhalo-ddd/tree/master/gh-ddd-lite

#### 1.2. **gh-ddd-lite-spring**
> 为 gh-ddd-lite 提供 spring 的支持。

#### 1.3. **gh-ddd-lite-codegen**
> 基于 gh-ddd-lite 项目，提供样板代码自动生成功能。

详见：https://gitee.com/litao851025/geekhalo-ddd/tree/master/gh-ddd-lite-codegen

#### 1.4. **gh-ddd-lite-demo**
> 展示项目的正确使用方式。


