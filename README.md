# Snapx Server

## API文档地址

- 后台管理端：http://localhost:9080/swagger-ui.html
- 前台应用端：http://localhost:9081/swagger-ui.html

## 项目目录说明
### snapx-bom
项目依赖版本管理

### snapx-core
项目业务无关的公共代码

### snapx-app
项目业务相关的代码，和可启动服务

## 模块说明 snapx-app
### 公共业务 snapx-server-base
snapx-server-admin和snapx-server-app依赖的jar包，包含两个服务共享的业务逻辑

### 后台管理服务 snapx-server-admin
后台管理端服务，可单独启动的单体应用

### 前台应用服务 snapx-server-app
前台应用端服务，可单独启动的单体应用

### 领域业务模块 snapx-domain-xxxx
snapx-domain开头的是领域业务模块，被snapx-server-admin和snapx-server-app依赖。
领域业务模块之间不能相互依赖，需要协同多个领域业务完成的逻辑在snapx-server-admin和snapx-server-app中实现。

- snapx-domain-system 管理系统领域业务，包括后台管理段管理员维护，登录登出，角色权限控制
- snapx-domain-infra 基础设施领域业务，包括图片上传，第三方对接
- snapx-domain-member 会员体系领域业务
- snapx-domain-camera 相机拍照领域业务
- snapx-domain-restaurant 餐馆评价领域业务

## 数据库规范
表名需要加上领域业务前缀，实体上可以不加。
```java
@Data
@TableName("sys_admin_user") // sys_是领域前缀，admin_user是实体名字
public class AdminUser {
    private Long id;
}
```

前缀定义如下

- sys 管理系统领域业务
- inf 基础设施领域业务
- mem 会员相关领域业务
- cmr 相机相关领域业务
- rst 餐馆相关领域业务

## API定义规范

API接口路径由三部分组成，/{领域业务}/{模块}/{方法名}，单词用中划线分隔。

API接口只支持GET / POST 方法请求

```
# /system 是管理系统领域业务，/admin-user 是后台管理员模块，/find-admin-user是方法名 
GET /system/admin-user/find-admin-user/{id}
```
接口路径领域命名

- /system 管理系统领域业务
- /infra 基础设施领域业务
- /member 会员相关领域业务
- /camera 相机拍照领域业务
- /restaurant 餐馆拍照领域业务


接口方法名规范

- GET /find-xxx/{id} 表示通过id获取单个实体
- GET /page-xxx 表示分页查找实体列表
- GET /list-xxx 表示不分页获取实体列表
- GET /count-xxx 实体记录数
- POST /create-xxx 表示创建实体
- POST /update-xxx/{id} 表示更新实体
- POST /delete-xxx/{id} 表示删除实体
- POST /update-xxx-{状态字段}/{id} 表示更新实体某一个状态值

## Drone CI/CD 部署方法

Drone CI/CD 访问地址：http://drone.catfoodworks.com

### dev环境部署

代码合并到dev分支推送远端，自动触发部署app服务，admin服务。

登录Drone查看打包部署进度

```bash
git checkout dev
git push
```

### release环境部署

代码合并到release分支，使用git tag命令创建一个tag, 使用git push --tag命令推送远端，自动触发部署app服务，admin服务。

tag命名规范，日期+第几次部署，例如 230226001，230226002

登录Drone查看打包部署进度。

```bash
git checkout release
git tag 230226001
git push --tag
```