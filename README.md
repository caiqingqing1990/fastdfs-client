# fastdfs-client

fastdfs-client is [fastdfs](https://github.com/happyfish100/fastdfs) java client 
based on [Netty](http://netty.io) and commons-pool2.

## 前置条件

* Java 6+ required.
* Fastdfs 服务器端字符集编码需使用 UTF-8

## maven

```xml
<dependency>
	<groupId>cn.strong</groupId>
	<artifactId>fastdfs-client</artifactId>
	<version>1.0.0</version>
</dependency>
```

## 使用说明

程序的主要入口为 SimpleFastdfsClient 和 FastdfsClient 两个门面类。
它们两个其实是等价的，只是其中 FastdfsClient 使用的存储路径为 StoragePath 结构(包含分组和组内路径信息)，
而 SimpleFastdfsClient 使用的存储路径为合到一起的全路径。

以下以 SimpleFastdfsClient 为例做使用说明。

SimpleFastdfsClient 的后端依赖于 FastdfsExecutor 做实际的组件通信工作，以及一组 tracker 地址列表。

FastdfsExecutor 具有生命周期，它维护了一个通道连接池以及 IO 线程组等资源。
它需要在启动时调用 init() 方法初始化并且在关闭时调用 shutdown() 方法释放资源。
这两个方法分别具有 `@PostConstruct` 和 `@PreDestroy` 注解，可方便容器管理。

```java
FastdfsExecutor executor = new FastdfsExecutor();
executor.init();

try {
	List<InetSocketAddress> trackers = Arrays.asList(new InetSocketAddress(
				"192.168.20.68", 22122));
	SimpleFastdfsClient dfs = new SimpleFastdfsClient(executor, trackers);
	
	// do something
} finally {
	executor.shutdown();
}
```

得到 SimpleFastdfsClient 示例以后即可通过相应 API 进行上传下载等操作。

```java
byte[] bytes = "Hello Fastdfs".getBytes(CharsetUtil.UTF_8);
String uri = dfs.upload(bytes, "txt");
System.out.println("uploaded: ");
//-> uploaded:
System.out.println(uri);
//-> group1/M00/05/22/wKgURFUP60SEYdruAAAAAEKHwLQ894.txt

byte[] downloads = dfs.download(uri);
String content = new String(downloads, CharsetUtil.UTF_8);
System.out.println("downloaded: ");
//-> downloaded: 
System.out.println(content);
//-> Hello Fastdfs
```

详细用法，请详见 SimpleFastdfsClient API。