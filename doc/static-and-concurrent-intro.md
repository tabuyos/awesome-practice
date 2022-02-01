# 参考

- [static与并发 - 编程猎人](https://www.programminghunter.com/article/9790843610/)



## static与并发

在java中static用来修饰Class类中**属性**和**方法**。

被static修饰的成员属性和成员方法**独立**于该类的任何对象，它们在内存空间上会被放在描述Class的位置中，也就是说它们为此类（Class）所有的实例对象共用。

所以说只要这个类被加载，那么这些被static修饰的属性和方法都已经在JVM中存在了，因此，我们可以通过类名直接调用这些属性和方法。

那么我就想到了一个在多线程和并发的情况下，被static修饰的方法会不会出现不正确的数据处理呢？

下面写一段测试的代码

Porter.java

```
public class Porter {
    public static void test(int num){
        System.out.printf("time : %s , num : %d \n",System.currentTimeMillis(),num);
    }
}
```

Runner.java

```java
public class Runner implements Runnable{
    private int num;
    @Override
    public void run() {
        Porter.test(getNum());
    }
    public void setNum(int num){
        this.num = num;
    }
    public int getNum(){
        return this.num;
    }
    public static void main(String args[]){
        Thread th[] = new Thread[100];
        for (int i = 0; i < th.length; i++) {
            Runner run =new Runner();
            run.setNum(i);
            th[i] = new Thread(run);
            th[i].start();
        }

    }
}
```

在这个多线程程序中，run()方法一直都在调用Porter的一个static方法test()。实际程序输出如下：

```
time : 1423399741495 , num : 0  
time : 1423399741521 , num : 40  
time : 1423399741520 , num : 39  
time : 1423399741520 , num : 38  
time : 1423399741518 , num : 37  
time : 1423399741518 , num : 36  
time : 1423399741517 , num : 34  
time : 1423399741517 , num : 35  
time : 1423399741523 , num : 43  
time : 1423399741516 , num : 32  
time : 1423399741516 , num : 33  
time : 1423399741516 , num : 30  
time : 1423399741516 , num : 31  
time : 1423399741515 , num : 29  
time : 1423399741515 , num : 28
……
```

虽然省略了一部分，但是程序确实没有执行错误，通过系统的时间戳发现，中间有几个线程确实也是同时执行的。这样说来被static修饰的方法并不存在并发的问题，那么这是为什么呢？

其实这就是变量的问题了，每次静态方法每次调用的内部变量，都是局部变量，每次调用静态方法时都会为它重新分配内存空间，所以是安全的。