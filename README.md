# Mp4课程视频网络爬虫

复杂且不熟悉的项目的开发思想，“自底向上”。可对比“自顶向下”。

## 简单的Mp4视频网络爬虫

未使用IO缓冲和多线程

下载三节课 耗时 75.269000 秒

## 使用IO缓冲的Mp4视频网络爬虫

下载三节课 耗时 75.471000 秒

性能提升不明显

## 使用IO缓冲和多线程的Mp4视频网络爬虫

下载三节课 耗时 56.584000

性能有提升

## 3中常用JSON库

+ Gson https://github.com/google/gson
+ FastJson https://github.com/alibaba/fastjson
+ Jackson https://github.com/FasterXML/jackson

## ArrayList 和 LinkedList 都不是线程安全

线程安全解决办法:

1. Collections.synchronizedList(new LinkedList<String>())
2. LinkedList和ArrayList换成线程安全的集合，如CopyOnWriteArrayList，ConcurrentLinkedQueue...
3. Vector(内部主要使用synchronized关键字实现同步)
4. 代码增加synchronized
