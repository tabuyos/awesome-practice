### 使用

HttpClient和OkHttp一般用于调用其它服务，一般服务暴露出来的接口都为http，http常用请求类型就为GET、PUT、POST和DELETE，因此主要介绍这些请求类型的调用

##### HttpClient使用介绍

使用HttpClient发送请求主要分为一下几步骤：

- 创建 CloseableHttpClient对象或CloseableHttpAsyncClient对象，前者同步，后者为异步
- 创建Http请求对象
- 调用execute方法执行请求，如果是异步请求在执行之前需调用start方法

**创建连接：**

```java
CloseableHttpClient httpClient = HttpClientBuilder.create().build();
```

该连接为同步连接

**GET请求：**

```java
@Test
public void testGet() throws IOException {
    String api = "/api/files/1";
    String url = String.format("%s%s", BASE_URL, api);
    HttpGet httpGet = new HttpGet(url);
    CloseableHttpResponse response = httpClient.execute(httpGet);
    System.out.println(EntityUtils.toString(response.getEntity()));
}
```

使用HttpGet表示该连接为GET请求，HttpClient调用execute方法发送GET请求

**PUT请求：**

```java
@Test
public void testPut() throws IOException {
    String api = "/api/user";
    String url = String.format("%s%s", BASE_URL, api);
    HttpPut httpPut = new HttpPut(url);
    UserVO userVO = UserVO.builder().name("h2t").id(16L).build();
    httpPut.setHeader("Content-Type", "application/json;charset=utf8");
    httpPut.setEntity(new StringEntity(JSONObject.toJSONString(userVO), "UTF-8"));
    CloseableHttpResponse response = httpClient.execute(httpPut);
    System.out.println(EntityUtils.toString(response.getEntity()));
}
```

**POST请求：**

- 添加对象

  ```java
  @Test
  public void testPost() throws IOException {
      String api = "/api/user";
      String url = String.format("%s%s", BASE_URL, api);
      HttpPost httpPost = new HttpPost(url);
      UserVO userVO = UserVO.builder().name("h2t2").build();
      httpPost.setHeader("Content-Type", "application/json;charset=utf8");
      httpPost.setEntity(new StringEntity(JSONObject.toJSONString(userVO), "UTF-8"));
      CloseableHttpResponse response = httpClient.execute(httpPost);
      System.out.println(EntityUtils.toString(response.getEntity()));
  }
  ```

  该请求是一个创建对象的请求，需要传入一个json字符串

- 上传文件

  ```java
  @Test
  public void testUpload1() throws IOException {
      String api = "/api/files/1";
      String url = String.format("%s%s", BASE_URL, api);
      HttpPost httpPost = new HttpPost(url);
      File file = new File("C:/Users/hetiantian/Desktop/学习/docker_practice.pdf");
      FileBody fileBody = new FileBody(file);
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      builder.addPart("file", fileBody);  //addPart上传文件
      HttpEntity entity = builder.build();
      httpPost.setEntity(entity);
      CloseableHttpResponse response = httpClient.execute(httpPost);
      System.out.println(EntityUtils.toString(response.getEntity()));
  }
  ```

  通过addPart上传文件

**DELETE请求：**

```java
@Test
public void testDelete() throws IOException {
    String api = "/api/user/12";
    String url = String.format("%s%s", BASE_URL, api);
    HttpDelete httpDelete = new HttpDelete(url);
    CloseableHttpResponse response = httpClient.execute(httpDelete);
    System.out.println(EntityUtils.toString(response.getEntity()));
}
```

**请求的取消：**

```java
@Test
public void testCancel() throws IOException {
    String api = "/api/files/1";
    String url = String.format("%s%s", BASE_URL, api);
    HttpGet httpGet = new HttpGet(url);
    httpGet.setConfig(requestConfig);  //设置超时时间
    //测试连接的取消

    long begin = System.currentTimeMillis();
    CloseableHttpResponse response = httpClient.execute(httpGet);
    while (true) {
        if (System.currentTimeMillis() - begin > 1000) {
          httpGet.abort();
          System.out.println("task canceled");
          break;
      }
    }

    System.out.println(EntityUtils.toString(response.getEntity()));
}
```

调用abort方法取消请求 执行结果：

```java
task canceled
cost 8098 msc
Disconnected from the target VM, address: '127.0.0.1:60549', transport: 'socket'

java.net.SocketException: socket closed...【省略】
```

##### OkHttp使用

使用OkHttp发送请求主要分为一下几步骤：

- 创建OkHttpClient对象
- 创建Request对象
- 将Request 对象封装为Call
- 通过Call 来执行同步或异步请求，调用execute方法同步执行，调用enqueue方法异步执行

**创建连接：**

```java
private OkHttpClient client = new OkHttpClient();
```

**GET请求：**

```java
@Test
public void testGet() throws IOException {
    String api = "/api/files/1";
    String url = String.format("%s%s", BASE_URL, api);
    Request request = new Request.Builder()
            .url(url)
            .get() 
            .build();
    final Call call = client.newCall(request);
    Response response = call.execute();
    System.out.println(response.body().string());
}
```

**PUT请求：**

```java
@Test
public void testPut() throws IOException {
    String api = "/api/user";
    String url = String.format("%s%s", BASE_URL, api);
    //请求参数
    UserVO userVO = UserVO.builder().name("h2t").id(11L).build();
    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),
    JSONObject.toJSONString(userVO));
    Request request = new Request.Builder()
            .url(url)
            .put(requestBody)
            .build();
    final Call call = client.newCall(request);
    Response response = call.execute();
    System.out.println(response.body().string());
}
```

**POST请求：**

- 添加对象

  ```java
  @Test
  public void testPost() throws IOException {
      String api = "/api/user";
      String url = String.format("%s%s", BASE_URL, api);
      //请求参数
      JSONObject json = new JSONObject();
      json.put("name", "hetiantian");
      RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),     String.valueOf(json));
      Request request = new Request.Builder()
              .url(url)
              .post(requestBody) //post请求
             .build();
      final Call call = client.newCall(request);
      Response response = call.execute();
      System.out.println(response.body().string());
  }
  ```

- 上传文件

  ```java
  @Test
  public void testUpload() throws IOException {
      String api = "/api/files/1";
      String url = String.format("%s%s", BASE_URL, api);
      RequestBody requestBody = new MultipartBody.Builder()
              .setType(MultipartBody.FORM)
              .addFormDataPart("file", "docker_practice.pdf",
                      RequestBody.create(MediaType.parse("multipart/form-data"),
                              new File("C:/Users/hetiantian/Desktop/学习/docker_practice.pdf")))
              .build();
      Request request = new Request.Builder()
              .url(url)
              .post(requestBody)  //默认为GET请求，可以不写
              .build();
      final Call call = client.newCall(request);
      Response response = call.execute();
      System.out.println(response.body().string());
  }
  ```

  通过addFormDataPart方法模拟表单方式上传文件

**DELETE请求：**

```java
@Test
public void testDelete() throws IOException {
  String url = String.format("%s%s", BASE_URL, api);
  //请求参数
  Request request = new Request.Builder()
          .url(url)
          .delete()
          .build();
  final Call call = client.newCall(request);
  Response response = call.execute();
  System.out.println(response.body().string());
}
```

**请求的取消：**

```java
@Test
public void testCancelSysnc() throws IOException {
    String api = "/api/files/1";
    String url = String.format("%s%s", BASE_URL, api);
    Request request = new Request.Builder()
            .url(url)
            .get()  
            .build();
    final Call call = client.newCall(request);
    Response response = call.execute();
    long start = System.currentTimeMillis();
    //测试连接的取消
    while (true) {
         //1分钟获取不到结果就取消请求
        if (System.currentTimeMillis() - start > 1000) {
            call.cancel();
            System.out.println("task canceled");
            break;
        }
    }

    System.out.println(response.body().string());
}
```

调用cancel方法进行取消 测试结果：

```java
task canceled
cost 9110 msc

java.net.SocketException: socket closed...【省略】
```

##### 小结

- OkHttp使用build模式创建对象来的更简洁一些，并且使用.post/.delete/.put/.get方法表示请求类型，不需要像HttpClient创建HttpGet、HttpPost等这些方法来创建请求类型

- 依赖包上，如果HttpClient需要发送异步请求、实现文件上传，需要额外的引入异步请求依赖

  ```java
   <!---文件上传-->
   <dependency>
       <groupId>org.apache.httpcomponents</groupId>
       <artifactId>httpmime</artifactId>
       <version>4.5.3</version>
   </dependency>
   <!--异步请求-->
   <dependency>
       <groupId>org.apache.httpcomponents</groupId>
       <artifactId>httpasyncclient</artifactId>
       <version>4.5.3</version>
   </dependency>
  ```

- 请求的取消，HttpClient使用abort方法，OkHttp使用cancel方法，都挺简单的，如果使用的是异步client，则在抛出异常时调用取消请求的方法即可

### 超时设置

**HttpClient超时设置：**
 在HttpClient4.3+版本以上，超时设置通过RequestConfig进行设置

```java
private CloseableHttpClient httpClient = HttpClientBuilder.create().build();
private RequestConfig requestConfig =  RequestConfig.custom()
        .setSocketTimeout(60 * 1000)
        .setConnectTimeout(60 * 1000).build();
String api = "/api/files/1";
String url = String.format("%s%s", BASE_URL, api);
HttpGet httpGet = new HttpGet(url);
httpGet.setConfig(requestConfig);  //设置超时时间
```

超时时间是设置在请求类型HttpGet上，而不是HttpClient上

**OkHttp超时设置：**
 直接在OkHttp上进行设置

```java
private OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
        .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
        .build();
```

**小结：**
 如果client是单例模式，HttpClient在设置超时方面来的更灵活，针对不同请求类型设置不同的超时时间，OkHttp一旦设置了超时时间，所有请求类型的超时时间也就确定

### HttpClient和OkHttp性能比较

测试环境：

- CPU 六核
- 内存 8G
- windows10

每种测试用例都测试五次，排除偶然性

**client连接为单例：**

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/1/8/16f833980a4d5932~tplv-t2oaga2asx-watermark.awebp)

**client连接不为单例：**

![img](https://p1-jj.byteimg.com/tos-cn-i-t2oaga2asx/gold-user-assets/2020/1/8/16f8339809f49fa1~tplv-t2oaga2asx-watermark.awebp)

单例模式下，HttpClient的响应速度要更快一些，单位为毫秒，性能差异相差不大 非单例模式下，OkHttp的性能更好，HttpClient创建连接比较耗时，因为多数情况下这些资源都会写成单例模式，因此图一的测试结果更具有参考价值



### 总结

OkHttp和HttpClient在性能和使用上**不分伯仲**，根据实际业务选择即可
[示例代码](https://link.juejin.cn?target=https%3A%2F%2Fgithub.com%2FTiantianUpup%2Fhttp-call)