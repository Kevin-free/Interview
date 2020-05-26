# JVM知识点

[TOC]



## 一、什么是JVM？

JVM（ Java Virtual Machine）即 Java虚拟机，虚拟机是物理机的软件实现。 JVM是运行在操作系统之上的，与硬件没有直接的交互，但是可以调用底层的硬件，用JIN （Java本地接口调用底层硬件接口，了解下就好，已经过时了） 

 ![img](https://img2018.cnblogs.com/blog/896914/201912/896914-20191214212820607-179669159.png) 



## 二、JVM体系结构

 Java 是用 **WORA(编写一次运行到任何地方)**的概念开发的，它在 **VM** 上运行。编译器将Java文件**编译**成 Java .class 文件，然后将 .class 文件输入 JVM ,  JVM 加载并执行类文件。下面是 JVM 系统结构概览（来自 https://www.cnblogs.com/zongheng14/p/12041005.html ）。 

 ![img](https://img2018.cnblogs.com/blog/896914/201912/896914-20191214212856815-499698999.png) 

> 详细架构图如下（图片来自 https://www.cnblogs.com/liululee/archive/2019/09/04/11461998.html ）

 ![file](https://img2018.cnblogs.com/blog/1692986/201909/1692986-20190904223533352-60205092.png;%20charset=binary) 

如图所示，JVM 分为三个主要子系统：

1. 类加载器子系统
2. 运行时数据区
3. 执行引擎



## 三、类加载器子系统

#### 1、类加载器的概念

> 负责加载class文件，class文件**在文件开头有特定的文件标识**（ 很多类型的文件，其起始的几个字节的内容是固定的， 这几个字节的内容也被称为**魔数** (magic number)  ，因为根据这几个字节的内容就可以确定文件类型。【 class的 魔数就是`0xCAFEBABE`】）
>
>  将class文件字节码内容加载到内存中，并将这些内容转换成方法区中的运行时数据结构并且ClassLoader只负责class文件的加载，至于它是否可以运行，则由Execution Engine决定。   



 ![img](https://img2018.cnblogs.com/blog/896914/201912/896914-20191214213912696-2039430668.png) 

> 解释：Car.class  是由 .java 文件 经过编译而得来的 .class文件，存在本地磁盘     
>
> ClassLoader: 类转载器，作用就是加载并初始化 .class文件 ，得到真正的 Class 类，即模板  （此处不明白则带着疑问继续往下看，为什么叫模板）             
>
> Car Class : 由 Car.class 字节码文件，通过ClassLoader 加载并初始化而得，那么此时 这个 Car 就是当前类的模板，这个Car Class 模板就存在 【方法区】            
>
> car1,car2,car3 : 是由Car模板经过实例化而得，即 new出来的 --> Car car1 = new Car() , Car car2 = new Car() ,Car car3 = new Car() , 因此可知，由一个模板，可以得到多个实例对象，即模板一个，实例多个              
>
> 所以，拿car1举例，car1.getClass 可以得到其模板Car 类，Car.getClassLoader() 可得到其装载器  



#### 2、类加载器的种类

- 虚拟机自带的类加载器
  - 启动类加载器，也叫根加载器（Bootstrap），由C++编写， 程序中自带的类， 存储在$JAVAHOME/jre/lib/rt.jar中，如Object类等 
  - 扩展类加载器（Extension），Java编写， 在我们平时看到的类路径中，凡是以javax 开头的，都是拓展包，存储在$JAVAHOME/jre/lib/ext/*.jar 中  
  - 应用类加载器（AppClassLoader）， 即平时程序中自定义的类  `new` 出来的   

- 用户自定义加载器

   Java.lang.ClassLoader 的子类，用户可以定制类的加载方式，即如果你的程序有特殊的需求，你也可以自定义你的类加载器的加载方式 ，进入ClassLoader的源码，其为抽象类，因此在你定制化开发的时候，需要你定义自己的加载器类来继承ClassLoader抽象类即可，即 `MyClassLoader extends ClassLoader `

 ![img](https://img2018.cnblogs.com/blog/896914/201912/896914-20191219003120840-1250259190.png) 

>  所以，Java 的类的加载机制，永远是从 启动类加载器 -->  拓展类加载器  --> 应用程序类加载器  这样的一个顺序进行加载 



#### 3、类加载器的双亲委派机制

>先举一个例子，来说明什么是双亲委派机制，比如，有一个类是 A.java ，当要使用 A 类是，类加载器要先去 【启动类加载器】中找，如果找到就使用启动类加载器中的A类，不继续往下执行，但是如果找不到，则往下找，去 【扩展类加载器】中找，同理找到使用，找不到继续往下，再去【应用类加载器】中找，找到使用，此时还找不到就会报 `classNotFund Exception `的异常
>
>概念：当一个类收到类加载是，它首先不会尝试自己去加载这个类，而是吧这个请求委派给父类去完成，每一个层次的类加载器都是如此，因此所有的类加载请求都是应该传到启动类加载器中的，只有当其父类加载器无法完成这个请求时（在它的加载路径没有找到所需加载的Class），子类加载器才会尝试自己去加载。
>
>好处：保证了使用不同的类加载器最终得到的都是同一个对象。



#### 4、类加载器沙箱安全机制

> 通过双亲委派机制，类的加载永远都是从启动类加载器开始，依次下放，保证你写的代码，不会污染Java自带的源代码，保证了沙箱安全。



## 四、运行时数据区

 Java 虚拟机在执行 Java 程序的过程中会把它管理的内存划分成若干个不同的数据区域。JDK. 1.8 和之前的版本略有不同，下面会介绍到。 

**JDK 1.8 之前**

 ![img](https://mmbiz.qpic.cn/mmbiz_png/iaIdQfEric9TzUZjSyJWO8qdn6Z6mmmM4YibA83XJ4gC05HRL8FzzOZGMjibgSSrSyiaOMHLuSwGeUVicqwvfwMFu7Cg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 



 **JDK 1.8 ：** 

 ![img](https://mmbiz.qpic.cn/mmbiz_png/iaIdQfEric9TzUZjSyJWO8qdn6Z6mmmM4Y6z4cdYe8LBbtWt2JKQD7oyq8pib6v1ugQY4fwzibTQvhzAKEdJMVPpyQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 



**线程私有的：**

- 程序计数器
- 虚拟机栈
- 本地方法栈

**线程共享的：**

- 堆
- 方法区
- 直接内存（非运行时数据区的一部分）



#### 1、程序计数器

程序计数器是一块较小的内存空间，可以看作是当前线程所执行字节码的行号指示器。**字节码解释器工作时通过改变这个计数器的值来选取下一条需要执行的字节码指令，分支、循环、跳转、异常处理、线程恢复等功能都需要依赖这个计数器来完成。**

另外，**为了线程切换后能回到正常的执行位置，没个线程都需要一个独立的线程计数器，各线程之间的计数器互不影响，独立存储，我们称这类内存区域为“线程私有”的内存。**

 **从上面的介绍中我们知道程序计数器主要有两个作用：** 

1. 字节码解释器通过改变程序计数器来依次读取指令，从而实现代码的流程控制，如：顺序执行、选择、循环、异常处理。
2. 在多线程的情况下，程序计数器用于记录当前线程执行的位置，从而当线程被切换回来的时候能够知道该线程上次运行到哪儿了。

**注意：程序计数器是唯一一个不会出现 `OutOfMemoryError(OOM)` 的内存区域，它的生命周期随着线程的创建而创建，随着线程的结束而死亡。** 

#### 2、Java虚拟机栈

**与线程计数器一样，Java虚拟机栈也是线程私有的，它的生命周期和线程相同，描述的是Java方法执行的内存模型，每次方法调用的数据都是通过栈传递的。**

**Java内存可以粗糙的分为堆内存（Heap）和栈内存（Stack），其中栈就是现在说的虚拟机栈，或者说是虚拟机栈中的局部变量表部分。**（实际上，Java虚拟机栈是由一个一个栈帧组成，而每个栈帧都拥有：局部变量表、操作数栈、动态链接、方法出口信息）

**局部变量表主要存放了编译器可知的各种数据类型**（boolean、byte、char、short、int、long、float、double）、**对象引用**（reference 类型，它不用于对象本身，可能是一个指向对象起始地址的引用指针，也可能是指向一个代表对象的句柄或其他与此对象相关的位置）

**Java虚拟机栈会有两种错误：StackOverFlowError 和 OutOfMemoryError。**

- **StackOverFlowError**：若Java虚拟机栈的内存大小不允许动态扩展，那么当线程请求的栈的深度超过Java虚拟机栈的最大深度的时候，就抛出StackOverFlowError 错误。
- **OutOfMemoryError**：若Java虚拟机栈的内存大小允许动态扩展，且当线程请求的栈时内存用完了，无法再动态扩展了，此时抛出OutOfMemoryError错误。

> 扩展，那么函数/方法如何调用？

 Java 栈可用类比数据结构中栈，Java 栈中保存的主要内容是栈帧，每一次函数调用都会有一个对应的栈帧被压入 Java 栈，每一个函数调用结束后，都会有一个栈帧被弹出。 

Java 方法有两种返回方式：

1. return 语句。
2. 抛出异常。

不管哪种返回方式都会导致栈帧被弹出。

 ![img](https://img2018.cnblogs.com/blog/896914/201912/896914-20191221160238092-698106216.png) 



#### 3、本地方法栈

和虚拟机栈发挥的作用非常相似，区别是：**虚拟机栈为虚拟机为Java方法（也就是字节码）服务，而本地方法栈则为虚拟机使用到的Native 方法服务。在HotSpot虚拟机中Java虚拟机栈合二为一。**

本地方法被执行的时候，在本地方法栈也会创建一个栈帧，用于存放该本地方法的局部变量表、操作数栈、动态链接、出口信息。

方法执行完毕后相应的栈帧也会出栈并释放内存空间，也会出现 StackOverFlowError 和 OutOfMemoryError 两种错误。

#### 4、堆

JVM所管理的内存中最大的一块，Java 堆 所有线程共享的一块内存区域，在虚拟机启动时创建。**此内存的唯一目的就是存放对象实例，几乎所有的对象实例和数组都在这里分配内存。**

Java堆是垃圾收集器管理的主要区域，因此也被称为**GC堆（Garbage Collected Heap）**。从辣鸡回收的角度，由于现在收集器主要采用分代垃圾收集算法，所以Java堆还可以细分为：新生代和老年代，再细致有：Eden区，From Survivor、To Survivor区等。**进一步划分的目的是更好的回收内存，或者更快的分配内存。**

在JDK 7 以及 JDK 7 之前，堆内存通常被分为以下三部分：

1. 新生代内存（Young Generation）
2. 老生代（Old Generation）
3. 永生代（Permanent Generation）

 ![img](https://mmbiz.qpic.cn/mmbiz_jpg/iaIdQfEric9TzUZjSyJWO8qdn6Z6mmmM4Y8YXibBzuJS1ITzDUian0xSRJh7WKNicu692LnKy7ibIqiaMSiaTgxibdMpERw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 

JDK 8 开始方法区（HotSpot的永久代）被彻底移除了，取而代之的是 **元空间**，元空间使用的直接内存。

 ![img](https://mmbiz.qpic.cn/mmbiz_jpg/iaIdQfEric9TzUZjSyJWO8qdn6Z6mmmM4YHhYgFosCn6haibYDNfkRDsOnOhfU7Anyb3xkK7PLoz3cKcvicHic2Xxzw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 

**上图所示的 Eden 区（伊甸区）、两个 Survivor 区（幸存区）都属于新生代（为了区分，连个Survivor区域按照顺序被命名为 from 和 to），下一层属于老年代。**

大部分情况，对象首先都会在Eden区分配，在一次新生代垃圾回收后，如果对象还存货，则会进入s0或s1，并且对象的年龄会加1（Eden区 --> Survivor区后 对象的初始年龄变为1），当它的年龄增加到一定程度（默认为15岁），就会被晋升到老年代中。对象晋升到老年代中的年龄阈值，可以通过参数 `-XX:MaxTenuringThreshold` 来设置。

> 修正：Hotspot遍历所有对象时，按照年龄从小到大对其所占有的大小进行累积，当累积的某个年龄大小超过了Survivor区的一半是，取这个年龄和 MaxTenuringThreshold中的更小的一个值，作为新的晋升年龄阈值。
>
>  **动态年龄计算的代码如下** 
>
> ```java
> uint ageTable::compute_tenuring_threshold(size_t survivor_capacity) {
> //survivor_capacity是survivor空间的大小
>   size_t desired_survivor_size = (size_t)((((double) 				survivor_capacity)*TargetSurvivorRatio)/100);
>   size_t total = 0;
>   uint age = 1;
>   while (age < table_size) {
>     total += sizes[age];//sizes数组是每个年龄段对象大小
>     if (total > desired_survivor_size) break;
>     age++;
>   }
>   uint result = age < MaxTenuringThreshold ? age : MaxTenuringThreshold;
> ...
> }
> ```

堆这里最容易出现的就是 OOM 错误，并且出现这种错误之后的表现形式还会有几种，比如：

1. `OutOfMemoryError: GC Overhead Limit Exceeded`：当JVM花太多时间执行垃圾回收并且只能回收很少的堆空间时，就会发生次错误。
2.  **`OutOfMemoryError: Java heap space`** ：假如在创建新的对象时，堆内存中的空间不足以存放新创建的对象， 就会引发`java.lang.OutOfMemoryError: Java heap space` 错误。(和本机物理内存无关，和你配置的堆内存大小有关！) 

>  JVM参数配置如下图（这都代表啥玩意我枯了你呢 : (

 ![img](https://images2015.cnblogs.com/blog/285763/201611/285763-20161118115316810-1826109116.png) 

#### 5、方法区

方法区与Java堆一样，是各个线程共享的内存区域，它用于已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据。 虽然 **Java 虚拟机规范把方法区描述为堆的一个逻辑部分**，但是它却有一个别名叫做 **Non-Heap（非堆）**，目的应该是与 Java 堆区分开来。 

 方法区也被称为永久代。很多人都会分不清方法区和永久代的关系，为此我也查阅了文献。 

- #### 方法区和永久代的关系

> 《Java 虚拟机规范》只是规定了有方法区这么个概念和它的作用，并没有规定如何去实现它。那么，在不同的 JVM 上方法区的实现肯定是不同的了。 **方法区和永久代的关系很像 Java 中接口和类的关系，类实现了接口，而永久代就是 HotSpot 虚拟机对虚拟机规范中方法区的一种实现方式。** 也就是说，永久代是 HotSpot 的概念，方法区是 Java 虚拟机规范中的定义，是一种规范，而永久代是一种实现，一个是标准一个是实现，其他的虚拟机实现并没有永久代这一说法。 

- #### 常用参数

 JDK 1.8 之前永久代还没被彻底移除的时候通常通过下面这些参数来调节方法区大小 

```
-XX:PermSize=N //方法区 (永久代) 初始大小
-XX:MaxPermSize=N //方法区 (永久代) 最大大小,超过这个值将会抛出 OutOfMemoryError 异常:java.lang.OutOfMemoryError: PermGen
```

相对而言，垃圾收集行为在这个区域是比较少出现的，但并非数据进入方法区后就“永久存在”了。

JDK 1.8 的时候，方法区（HotSpot 的永久代）被彻底移除了（JDK1.7 就已经开始了），取而代之是元空间，元空间使用的是直接内存。

下面是一些常用参数：

```
-XX:MetaspaceSize=N //设置 Metaspace 的初始（和最小大小）-XX:MaxMetaspaceSize=N //设置 Metaspace 的最大大小
```

 与永久代很大的不同就是，如果不指定大小的话，随着更多类的创建，虚拟机会耗尽所有可用的系统内存。 

- #### 为什么要将永久代 (PermGen) 替换为元空间 (MetaSpace) 呢?

动机：Oracle收购Sun公司，JRocket 和 Hotspot，两套规范，整合为一。

 整个永久代有一个 JVM 本身设置固定大小上限，无法进行调整，而元空间使用的是直接内存，受本机可用内存的限制，并且永远不会得到 `java.lang.OutOfMemoryError`。你可以使用 `-XX：MaxMetaspaceSize` 标志设置最大元空间大小，默认值为 unlimited，这意味着它只受系统内存的限制。`-XX：MetaspaceSize` 调整标志定义元空间的初始大小如果未指定此标志，则 Metaspace 将根据运行时的应用程序需求动态地重新调整大小。 

#### 6、运行时常量池

运行时常量池是方法区的一部分。class文件中除了有类的版本、字段、方法、接口等描述信息外，还有常量池信息（ 用于存放编译期生成的各种字面量和符号引用 ）

 既然运行时常量池是方法区的一部分，自然受到方法区内存的限制，当常量池无法再申请到内存时会抛出 OutOfMemoryError 错误。 

 **JDK1.7 及之后版本的 JVM 已经将运行时常量池从方法区中移了出来，在 Java 堆（Heap）中开辟了一块区域存放运行时常量池。** 

![img](https://mmbiz.qpic.cn/mmbiz_png/iaIdQfEric9TzUZjSyJWO8qdn6Z6mmmM4Y9RnjkCib3dWIhFlgg1IF9FFLAickibBxBArPyBGCCCEemFUCibNYYdp4UA/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1) 

 ——图片来源：https://blog.csdn.net/wangbiao007/article/details/78545189 

#### 7、直接内存

 **直接内存并不是虚拟机运行时数据区的一部分，也不是虚拟机规范中定义的内存区域，但是这部分内存也被频繁地使用。而且也可能导致 OutOfMemoryError 错误出现。** 

JDK1.4 中新加入的 **NIO(New Input/Output) 类**，引入了一种基于**通道（Channel）** 与**缓存区（Buffer）** 的 I/O 方式，它可以直接使用 Native 函数库直接分配堆外内存，然后通过一个存储在 Java 堆中的 DirectByteBuffer 对象作为这块内存的引用进行操作。这样就能在一些场景中显著提高性能，因为**避免了在 Java 堆和 Native 堆之间来回复制数据**。

本机直接内存的分配不会受到 Java 堆的限制，但是，既然是内存就会受到本机总内存大小以及处理器寻址空间的限制。





## 五、垃圾收集（GC）算法

#### 0、java对象内存申请过程

![img](https://mmbiz.qpic.cn/mmbiz_jpg/iaIdQfEric9TzUZjSyJWO8qdn6Z6mmmM4Y8YXibBzuJS1ITzDUian0xSRJh7WKNicu692LnKy7ibIqiaMSiaTgxibdMpERw/640?wx_fmt=jpeg&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

1. JVM会试图为相关Java对象在Eden中初始化一块内存区域；当Eden空间足够时，内存申请结束。否则到下一步；
2. JVM试图释放在Eden中所有不活跃的对象（minor collection），释放后若Eden空间仍然不足以放入新对象，则试图将部分Eden中活跃对象放入Survivor区；
3. Survivor区被用来作为Eden及old的中间交换区域，当old区空间足够时，Survivor区的对象会被移到Old区，否则会被保留在Survivor区；
4. 当old区空间不够时，JVM会在old区进行major collection；
5. 垃圾收集后，若Survivor及old区仍然无法存放从Eden复制过来的部分对象，导致JVM无法在Eden区为新对象创建内存区域，则出现"Out of memory错误"；



#### 1、stop the world

在新生代进行的GC叫做minor GC，在老年代进行的GC都叫major GC，Full GC同时作用于新生代和老年代。在垃圾回收过程中经常涉及到对对象的挪动（比如上文提到的对象在Survivor 0和Survivor 1之间的复制），进而导致需要对对象引用进行更新。**为了保证引用更新的正确性，Java将暂停所有其他的线程，这种情况被称为“Stop-The-World”，导致系统全局停顿。Stop-The-World对系统性能存在影响，因此垃圾回收的一个原则是尽量减少“Stop-The-World”的时间。 **

不同垃圾收集器的Stop-The-World情况，Serial、Parallel和CMS收集器均存在不同程度的Stop-The-Word情况；而即便是最新的G1收集器也不例外。 

- Java 中一种全局暂停的现象，JVM挂起状态
- 全局停顿，所有Java代码停止，native代码可以执行，但不能与JVM交互
- 多半由于JVM的GC引用，如：
  - 老年代空间不足
  -  永生代（jkd7）或者元数据空间（jkd8）不足。 
  -  System.gc()方法调用。 
  -  CMS GC时出现promotion failed和concurrent mode failure
  - YoungGC时晋升老年代的内存平均值大于老年代剩余空间
  - 有连续的大对象需要分配 
- 除了GC还有以下原因：
  - 死锁检查
  - Dump线程--人为因素
  - 堆Dump--人为因素

关于stop the world 可查看： https://www.jianshu.com/p/d686e108d15f 



#### 2、如何判断对象是否“死去”？

在堆里面存放着Java中几乎所有的对象实例，垃圾收集器在对堆进行回收之前，第一件事情就是要确定哪些对象还存活着，哪些对象已经死去（即不可能在被任何途径使用的对象）。

- 引用计数算法
- 可达性分析算法

##### a、引用计数算法

 引用计数法顾名思义，就是对一个对象被引用的次数进行计数，当增加一个引用计数就加1，减少一个引用计数就减1。 

 ![image](https://images2017.cnblogs.com/blog/352511/201708/352511-20170818152927896-936352874.png) 

图片来自 https://www.cnblogs.com/leefreeman/p/7389919.html 

 上图表示3个Teacher的引用指向堆中的Teacher对象，那么Teacher对象的引用计数就是3，以此类推Student对象的引用计数就是2。 

 ![image](https://images2017.cnblogs.com/blog/352511/201708/352511-20170818152929100-1650698689.png) 

上图表示Teacher对象的引用减少为2，Student对象的引用减少为0（减少的原因是该引用指向了null，例如teacher3=null）,按照引用计数算法，Student对象的内存空间将被回收掉。

引用计数算法原理非常简单，是最原始的回收算法，但是java中没有使用这种算法，原因有2:

1. 频繁的计数影响性能，

2. 它无法处理【**循环引用**】的问题。

   例如Teacher对象中引用了Student对象，Student对象中又引用了Teacher对象，这种情况下，对象将永远无法被回收。

##### b、可达性分析算法

这个算法的基本思想就是，通过一系列称为 “**GC Roots**” 的对象作为起始点，从这些节点开始向下搜索，搜索所走过的路径称为【**引用链**】（Reference Chain），当一个对象到 GC Roots 没有任何引用链相连（用图论的话来说，就是从 GC Roots 到这个对象不可达）时，则证明此对象时不可用的。

 ![1.jpg-40.4kB](http://static.zybuluo.com/Yano/bgl07z2uk1qdwirwoy0rrrj7/1.jpg) 

在 Java 语言中，可作为 GC Roots 的对象包括一下几种：

- 虚拟机栈（栈帧中的本地变量表）中引用的对象
- 方法区中静态属性引用的对象
- 方法区中常量引用的对象
- 本地方法栈中 JNI （即一般说是 Native 方法）引用的对象

##### c、再谈引用

 在 JDK 1.2 之后，Java 对引用的概念进行了扩充，将引用分为 

1. 强引用（Strong Reference）
2. 软引用（Soft Reference）
3. 弱引用（Weak Reference）
4. 虚引用（Phantom Reference）

- 强引用

  代码中普遍存在的，类似 “ Object obj = new Object() ” 这类的引用，只要强引用还存在，垃圾收集器永远不会回收掉被引用的对象。

- 软引用

  用来描述一些**还有用但非必须**的对象。软引用所关联的对象，在系统将要**发生内存溢出异常之前**，将会把这些对象列入回收范围，并进行**二次回收**。如果这次回收还没有足够的内存，才会抛出内存溢出异常。提供了 `SoftReference` 类实现软引用。

- 弱引用

  描述非必须的对象。强度比软引用更弱一些，被弱引用关联的对象，只能**生存到下一次垃圾收集发生之前。**当垃圾收集器工作时，无论当前内存是否足够，都会回收掉只被弱引用关联的对象。提供了 `WeakReference`类实现弱引用。

- 虚引用

  一个对象是否有虚引用，完全不会对其生存时间构成影响，也无法通过一个虚引用来取得一个对象实例。为一个对象设置虚引用关联的唯一目的，就是能在这个对象**被收集器回收时**收到一个**系统通知**。通过了 `PhantomReference` 类实现虚引用。



#### 3、标记-清除算法（Mark-Sweep）

##### 什么是标记-清除算法

分为【**标记**】和【**清除**】两个阶段。首先标记处所有需要回收的对象，在标记完成后统一回收被标记的对象。

##### 有什么缺点？

1. 效率问题：标记和清除的效率都不高
2. 空间问题：标记清除后会产生大量**【不连续的内存碎片】**，空间碎片太多可能导致，程序**分配较大对象**时无法找到足够的连续内存，不得不提前出发另一次垃圾收集动作。 

 ![2.jpg-81.6kB](http://static.zybuluo.com/Yano/yx9r4uty6h69bsmdv7tqxq9o/2.jpg) 



#### 4、复制算法（Copying）- 新生代

 将可用内存按容量划分为【大小相等的两块】，每次只使用其中一块。当这一块的内存用完了，就**将存活着的对象复制到另一块上面**，然后再把已经使用过的内存空间一次清理掉。 

##### 优点？

复制算法使得每次都是针对其中的一块进行内存回收，内存分配时也**不用考虑内存碎片**等复杂情况，只要移动堆顶指针，按顺序分配内存即可，实现简单，运行高效。

##### 缺点？

将内存缩小为原来的一半。在**对象存活率较高时**，需要执行**较多的【复制操作】**，效率会变低。

 ![3.jpg-95.7kB](http://static.zybuluo.com/Yano/b7gljelr9w4zwfx9xic1m90m/3.jpg) 



##### 应用？

商业的虚拟机都采用复制算法来**回收新生代**。因为新生代中的对象容易死亡，所以并不需要按照1:1的比例划分内存空间，而是将内存分为**一块较大的 Eden 空间**和**两块较小的 Survivor 空间**。每次使用 Eden 和其中的一块 Survivor。

当回收时，将 Eden 和 Survivor 中还存活的对象一次性拷贝到另外一块 Survivor 空间上，最后清理掉 Eden 和刚才用过的 Survivor 空间。**Hotspot 虚拟机默认 Eden 和 Survivor 的大小比例是8:1，**也就是每次新生代中可用内存空间为整个新生代容量的90%（80% + 10%），只有10%的内存是会被“浪费”的。



#### 5、标记-整理算法（Mark-Compact）- 老年代

 标记过程仍然与“标记-清除”算法一样，但不是直接对可回收对象进行清理，而是**让所有存活的对象向一端移动，然后直接清理掉边界以外的内存。** 

 ![img](http://static.zybuluo.com/Yano/ykokv3ov9vxyiuntfv3d3i18/4.jpg) 



##### 优点？

经过整理（压缩），即再次扫描，并往一端**【滑动】**存活对象，**没有内存碎片**

##### 缺点？

需要移动对象的成本



#### 6、分代收集算法

根据对象的存活周期，将内存分为几块。一般是把【Java堆】分为【新生代】和【老年代】， 这样就可以根据各个年代的特点，采用最适当的收集算法。 

- **新生代**：每次垃圾收集时会有大批对象死去，只有少量存活，所以选择【复制算法】，只需要少量存活对象的复制成本就可以完成收集。
- **老年代**：对象存活率较高，没有额外空间对它进行分配担保，必须使用【标记-清除】或【标记-整理】算法进行收集。





## 六、JVM参数设置

### 0、JVM 参数如下图

![1586229359278](C:\Users\Kevin\AppData\Roaming\Typora\typora-user-images\1586229359278.png)



### 1、JVM 参数类型

#### 标配参数

-version, -help, java -showversion

#### X参数

-Xint  解释执行

-Xcomp  第一次使用就编译成本地代码

-Xmixed  混合模式

#### **XX参数（重点）**

- Boolean 类型

  公式：-XX: + 或者 - 某个属性值  (+ 表示开启， - 表示关闭）

  如：-XX:**-**PrintGCDetails  ,  +XX:**+**PrintGCDetails（是否打印GC收集细节）

  

  - 如何查看一个正在运行的 Java 程序，它的某个 jvm 参数是否开启？具体值是多少？

    jps 找到进程编号

    jinfo  查看详细信息

  ![1586156269767](C:\Users\Kevin\AppData\Roaming\Typora\typora-user-images\1586156269767.png)

  

- KV 设置类型

  公式：-XX:属性key = 属性值value

  如：-XX:MetaspaceSize = 128m （元空间大小）

  ​		-XX:MaxTenuringThreshold = 15 （多大年龄晋升老年代）



- jinfo 举止，如何查看当前运行程序的配置

  jinfo -flag 配置项 进程编号

  jinfo -flags 		   进程编号

  ![1586157602072](C:\Users\Kevin\AppData\Roaming\Typora\typora-user-images\1586157602072.png)



- 题外话（坑题）

  两个经典的参数，属于哪一类

  - **-Xms == -XX:InitialHeapSize **（初始堆内存大小）
  - **-Xmx == -XX:MaxHeapSize**（最大堆内存大小）

  > 顺便一提，Xms 和 Xmx 配成一样！避免扩容带来的消耗



### 2、查看JVM初始默认值

#### java -XX:+PrintFlagsInitial （主要查看初始化默认）

![1586158039613](C:\Users\Kevin\AppData\Roaming\Typora\typora-user-images\1586158039613.png)



#### java -XX:+PrintFlagsFinal -version （主要查看修改更新）

![1586158378605](C:\Users\Kevin\AppData\Roaming\Typora\typora-user-images\1586158378605.png)



=  jvm默认加载的

: = jvm加载修改过的或者人为改过的



#### java -XX:+PrintCommandLineFlags（打印命令行参数）





# 面试常问



## 1、平时用过的JVM常用配置参数有哪些？

- -**Xms**

  初始大小内存，默认为物理内存的 1/64

  等价于 -XX:InitialHeapSize

  

- -**Xmx**

  最大分配内存，默认为物理内存的 1/4

  等价于 -XX:MaxHeapSize



-  -**Xss**

  设置单个线程栈的大小，一般默认为 512K ~ 1024K 

  等价于 -XX:ThreadStackSize

  > 注意！![1586231614551](C:\Users\Kevin\AppData\Roaming\Typora\typora-user-images\1586231614551.png)

  **不修改的话是0，意思是使用默认，默认大小取决于平台！**官方文档如下：

  ![1586232146650](C:\Users\Kevin\AppData\Roaming\Typora\typora-user-images\1586232146650.png)
  
  
  
  修改则先显示修改值
  
  ![1586231745357](C:\Users\Kevin\AppData\Roaming\Typora\typora-user-images\1586231745357.png)

























参考资料：

 《深入理解Java虚拟机（第2版）》 周志明 

 https://github.com/Snailclimb/JavaGuide 

 https://www.cnblogs.com/zongheng14/p/12041005.html 

 https://www.cnblogs.com/liululee/archive/2019/09/04/11461998.html 

 https://blog.csdn.net/zfgogo/article/details/81260172 