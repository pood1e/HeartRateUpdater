# Github实时心率上传

刚好手头有块PixelWatch，配合Github-Graphql开放接口文档，实时上报自己的心率

## 前置条件

* wear os手表
* 带GMS的手机，能与手表正常配对使用

## 使用

1. 将申请到的Personal Access Token(需要有user权限)填入
   * `gradle.properties`的`token`
   * `SensorReceiver.kt`的`token`
2. 执行`gradle :mobile:downloadServiceApolloSchemaFromIntrospection`获取schema
3. 安装wear包到手表上, 手动修改应用权限-`传感器-始终允许`, 手动启动一次
4. 安装mobile包到手机上, 仅仅是个服务(消息触发)
