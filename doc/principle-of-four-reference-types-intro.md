# 参考

- [Java四种引用类型原理你真的搞明白了吗？五分钟带你深入理解！ - SegmentFault 思否](https://segmentfault.com/a/1190000039994284)



# Java四种引用类型原理你真的搞明白了吗？五分钟带你深入理解！

Java中一共有4种引用类型(其实还有一些其他的引用类型比如FinalReference)：强引用、软引用、弱引用、虚引用。

其中强引用就是我们经常使用的Object a = new Object(); 这样的形式，在Java中并没有对应的Reference类。

**本篇文章主要是分析软引用、弱引用、虚引用的实现，这三种引用类型都是继承于Reference这个类，主要逻辑也在Reference中。**

### **问题**

在分析前，先抛几个问题？

1.网上大多数文章对于软引用的介绍是：在内存不足的时候才会被回收，那内存不足是怎么定义的？什么才叫内存不足？

2.网上大多数文章对于虚引用的介绍是：形同虚设，虚引用并不会决定对象的生命周期。主要用来跟踪对象被垃圾回收器回收的活动。真的是这样吗？

3.虚引用在Jdk中有哪些场景下用到了呢？

### **Reference**

我们先看下Reference.java中的几个字段

```gradle
public abstract class Reference<T> {
    //引用的对象
    private T referent;        
    //回收队列，由使用者在Reference的构造函数中指定
    volatile ReferenceQueue<? super T> queue;
     //当该引用被加入到queue中的时候，该字段被设置为queue中的下一个元素，以形成链表结构
    volatile Reference next;
    //在GC时，JVM底层会维护一个叫DiscoveredList的链表，存放的是Reference对象，discovered字段指向的就是链表中的下一个元素，由JVM设置
    transient private Reference<T> discovered;  
    //进行线程同步的锁对象
    static private class Lock { }
    private static Lock lock = new Lock();
    //等待加入queue的Reference对象，在GC时由JVM设置，会有一个java层的线程(ReferenceHandler)源源不断的从pending中提取元素加入到queue
    private static Reference<Object> pending = null;
}
```

一个Reference对象的生命周期如下：

![img](principle-of-four-reference-type-intro/1460000039994288)

主要分为Native层和Java层两个部分。

Native层在GC时将需要被回收的Reference对象加入到DiscoveredList中（代码在referenceProcessor.cpp中process_discovered_references方法），然后将DiscoveredList的元素移动到PendingList中（代码在referenceProcessor.cpp中enqueue_discovered_ref_helper方法）,PendingList的队首就是Reference类中的pending对象。

看看Java层的代码

```typescript
private static class ReferenceHandler extends Thread {
         ...
        public void run() {
            while (true) {
                tryHandlePending(true);
            }
        }
  } 
static boolean tryHandlePending(boolean waitForNotify) {
        Reference<Object> r;
        Cleaner c;
        try {
            synchronized (lock) {
                if (pending != null) {
                    r = pending;
                     //如果是Cleaner对象，则记录下来，下面做特殊处理
                    c = r instanceof Cleaner ? (Cleaner) r : null;
                    //指向PendingList的下一个对象
                    pending = r.discovered;
                    r.discovered = null;
                } else {
                   //如果pending为null就先等待，当有对象加入到PendingList中时，jvm会执行notify
                    if (waitForNotify) {
                        lock.wait();
                    }
                    // retry if waited
                    return waitForNotify;
                }
            }
        } 
        ...

        // 如果时CLeaner对象，则调用clean方法进行资源回收
        if (c != null) {
            c.clean();
            return true;
        }
        //将Reference加入到ReferenceQueue，开发者可以通过从ReferenceQueue中poll元素感知到对象被回收的事件。
        ReferenceQueue<? super Object> q = r.queue;
        if (q != ReferenceQueue.NULL) q.enqueue(r);
        return true;
 }
```

流程比较简单：就是源源不断的从PendingList中提取出元素，然后将其加入到ReferenceQueue中去，开发者可以通过从ReferenceQueue中poll元素感知到对象被回收的事件。

另外需要注意的是，对于Cleaner类型（继承自虚引用）的对象会有额外的处理：**在其指向的对象被回收时，会调用clean方法，该方法主要是用来做对应的资源回收，在堆外内存DirectByteBuffer中就是用Cleaner进行堆外内存的回收，这也是虚引用在java中的典型应用。**

看完了Reference的实现，再看看几个实现类里，各自有什么不同。

SoftReference

```angelscript
public class SoftReference<T> extends Reference<T> {

    static private long clock;

    private long timestamp;

    public SoftReference(T referent) {
        super(referent);
        this.timestamp = clock;
    }

    public SoftReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
        this.timestamp = clock;
    }

    public T get() {
        T o = super.get();
        if (o != null && this.timestamp != clock)
            this.timestamp = clock;
        return o;
    }

}
```

软引用的实现很简单，就多了两个字段：clock和timestamp。clock是个静态变量，每次GC时都会将该字段设置成当前时间。timestamp字段则会在每次调用get方法时将其赋值为clock（如果不相等且对象没被回收）。

**那这两个字段的作用是什么呢？这和软引用在内存不够的时候才被回收，又有什么关系呢？**

这些还得看JVM的源码才行，因为决定对象是否需要被回收都是在GC中实现的。

```php
size_t
ReferenceProcessor::process_discovered_reflist(
  DiscoveredList               refs_lists[],
  ReferencePolicy*             policy,
  bool                         clear_referent,
  BoolObjectClosure*           is_alive,
  OopClosure*                  keep_alive,
  VoidClosure*                 complete_gc,
  AbstractRefProcTaskExecutor* task_executor)
{
 ...
   //还记得上文提到过的DiscoveredList吗?refs_lists就是DiscoveredList。
   //对于DiscoveredList的处理分为几个阶段，SoftReference的处理就在第一阶段
 ...
      for (uint i = 0; i < _max_num_q; i++) {
        process_phase1(refs_lists[i], policy,
                       is_alive, keep_alive, complete_gc);
      }
 ...
}

//该阶段的主要目的就是当内存足够时，将对应的SoftReference从refs_list中移除。
void
ReferenceProcessor::process_phase1(DiscoveredList&    refs_list,
                                   ReferencePolicy*   policy,
                                   BoolObjectClosure* is_alive,
                                   OopClosure*        keep_alive,
                                   VoidClosure*       complete_gc) {

  DiscoveredListIterator iter(refs_list, keep_alive, is_alive);
  // Decide which softly reachable refs should be kept alive.
  while (iter.has_next()) {
    iter.load_ptrs(DEBUG_ONLY(!discovery_is_atomic() /* allow_null_referent */));
    //判断引用的对象是否存活
    bool referent_is_dead = (iter.referent() != NULL) && !iter.is_referent_alive();
    //如果引用的对象已经不存活了，则会去调用对应的ReferencePolicy判断该对象是不时要被回收
    if (referent_is_dead &&
        !policy->should_clear_reference(iter.obj(), _soft_ref_timestamp_clock)) {
      if (TraceReferenceGC) {
        gclog_or_tty->print_cr("Dropping reference (" INTPTR_FORMAT ": %s"  ") by policy",
                               (void *)iter.obj(), iter.obj()->klass()->internal_name());
      }
      // Remove Reference object from list
      iter.remove();
      // Make the Reference object active again
      iter.make_active();
      // keep the referent around
      iter.make_referent_alive();
      iter.move_to_next();
    } else {
      iter.next();
    }
  }
 ...
}
```

refs_lists中存放了本次GC发现的某种引用类型（虚引用、软引用、弱引用等），而process_discovered_reflist方法的作用就是将不需要被回收的对象从refs_lists移除掉，refs_lists最后剩下的元素全是需要被回收的元素，最后会将其第一个元素赋值给上文提到过的Reference.java#pending字段。

> ReferencePolicy一共有4种实现：NeverClearPolicy，AlwaysClearPolicy，LRUCurrentHeapPolicy，LRUMaxHeapPolicy。

其中NeverClearPolicy永远返回false，代表永远不回收SoftReference，在JVM中该类没有被使用，AlwaysClearPolicy则永远返回true，在referenceProcessor.hpp#setup方法中中可以设置policy为AlwaysClearPolicy，至于什么时候会用到AlwaysClearPolicy，大家有兴趣可以自行研究。

LRUCurrentHeapPolicy和LRUMaxHeapPolicy的should_clear_reference方法则是完全相同：

```pgsql
bool LRUMaxHeapPolicy::should_clear_reference(oop p,
                                             jlong timestamp_clock) {
  jlong interval = timestamp_clock - java_lang_ref_SoftReference::timestamp(p);
  assert(interval >= 0, "Sanity check");

  // The interval will be zero if the ref was accessed since the last scavenge/gc.
  if(interval <= _max_interval) {
    return false;
  }

  return true;
}
```

timestamp_clock就是SoftReference的静态字段clock，java_lang_ref_SoftReference::timestamp(p)对应是字段timestamp。如果上次GC后有调用SoftReference#get，interval值为0，否则为若干次GC之间的时间差。

_max_interval则代表了一个临界值，它的值在LRUCurrentHeapPolicy和LRUMaxHeapPolicy两种策略中有差异。

```javascript
void LRUCurrentHeapPolicy::setup() {
  _max_interval = (Universe::get_heap_free_at_last_gc() / M) * SoftRefLRUPolicyMSPerMB;
  assert(_max_interval >= 0,"Sanity check");
}

void LRUMaxHeapPolicy::setup() {
  size_t max_heap = MaxHeapSize;
  max_heap -= Universe::get_heap_used_at_last_gc();
  max_heap /= M;

  _max_interval = max_heap * SoftRefLRUPolicyMSPerMB;
  assert(_max_interval >= 0,"Sanity check");
}
```

其中SoftRefLRUPolicyMSPerMB默认为1000，前者的计算方法和上次GC后可用堆大小有关，后者计算方法和（堆大小-上次gc时堆使用大小）有关。

看到这里你就知道SoftReference到底什么时候被被回收了，它和使用的策略（默认应该是LRUCurrentHeapPolicy），堆可用大小，该SoftReference上一次调用get方法的时间都有关系。

### **WeakReference**

```angelscript
public class WeakReference<T> extends Reference<T> {

    public WeakReference(T referent) {
        super(referent);
    }

    public WeakReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
    }

}
```

可以看到WeakReference在Java层只是继承了Reference，没有做任何的改动。那referent字段是什么时候被置为null的呢？要搞清楚这个问题我们再看下上文提到过的process_discovered_reflist方法：

```angelscript
size_t
ReferenceProcessor::process_discovered_reflist(
  DiscoveredList               refs_lists[],
  ReferencePolicy*             policy,
  bool                         clear_referent,
  BoolObjectClosure*           is_alive,
  OopClosure*                  keep_alive,
  VoidClosure*                 complete_gc,
  AbstractRefProcTaskExecutor* task_executor)
{
 ...

  //Phase 1:将所有不存活但是还不能被回收的软引用从refs_lists中移除（只有refs_lists为软引用的时候，这里policy才不为null）
  if (policy != NULL) {
    if (mt_processing) {
      RefProcPhase1Task phase1(*this, refs_lists, policy, true /*marks_oops_alive*/);
      task_executor->execute(phase1);
    } else {
      for (uint i = 0; i < _max_num_q; i++) {
        process_phase1(refs_lists[i], policy,
                       is_alive, keep_alive, complete_gc);
      }
    }
  } else { // policy == NULL
    assert(refs_lists != _discoveredSoftRefs,
           "Policy must be specified for soft references.");
  }

  // Phase 2:
  // 移除所有指向对象还存活的引用
  if (mt_processing) {
    RefProcPhase2Task phase2(*this, refs_lists, !discovery_is_atomic() /*marks_oops_alive*/);
    task_executor->execute(phase2);
  } else {
    for (uint i = 0; i < _max_num_q; i++) {
      process_phase2(refs_lists[i], is_alive, keep_alive, complete_gc);
    }
  }

  // Phase 3:
  // 根据clear_referent的值决定是否将不存活对象回收
  if (mt_processing) {
    RefProcPhase3Task phase3(*this, refs_lists, clear_referent, true /*marks_oops_alive*/);
    task_executor->execute(phase3);
  } else {
    for (uint i = 0; i < _max_num_q; i++) {
      process_phase3(refs_lists[i], clear_referent,
                     is_alive, keep_alive, complete_gc);
    }
  }

  return total_list_count;
}

void
ReferenceProcessor::process_phase3(DiscoveredList&    refs_list,
                                   bool               clear_referent,
                                   BoolObjectClosure* is_alive,
                                   OopClosure*        keep_alive,
                                   VoidClosure*       complete_gc) {
  ResourceMark rm;
  DiscoveredListIterator iter(refs_list, keep_alive, is_alive);
  while (iter.has_next()) {
    iter.update_discovered();
    iter.load_ptrs(DEBUG_ONLY(false /* allow_null_referent */));
    if (clear_referent) {
      // NULL out referent pointer
      //将Reference的referent字段置为null，之后会被GC回收
      iter.clear_referent();
    } else {
      // keep the referent around
      //标记引用的对象为存活，该对象在这次GC将不会被回收
      iter.make_referent_alive();
    }
    ...
  }
    ...
}
```

不管是弱引用还是其他引用类型，将字段referent置null的操作都发生在process_phase3中，而具体行为是由clear_referent的值决定的。而clear_referent的值则和引用类型相关。

```cpp
ReferenceProcessorStats ReferenceProcessor::process_discovered_references(
  BoolObjectClosure*           is_alive,
  OopClosure*                  keep_alive,
  VoidClosure*                 complete_gc,
  AbstractRefProcTaskExecutor* task_executor,
  GCTimer*                     gc_timer) {
  NOT_PRODUCT(verify_ok_to_handle_reflists());
    ...
  //process_discovered_reflist方法的第3个字段就是clear_referent
  // Soft references
  size_t soft_count = 0;
  {
    GCTraceTime tt("SoftReference", trace_time, false, gc_timer);
    soft_count =
      process_discovered_reflist(_discoveredSoftRefs, _current_soft_ref_policy, true,
                                 is_alive, keep_alive, complete_gc, task_executor);
  }

  update_soft_ref_master_clock();

  // Weak references
  size_t weak_count = 0;
  {
    GCTraceTime tt("WeakReference", trace_time, false, gc_timer);
    weak_count =
      process_discovered_reflist(_discoveredWeakRefs, NULL, true,
                                 is_alive, keep_alive, complete_gc, task_executor);
  }

  // Final references
  size_t final_count = 0;
  {
    GCTraceTime tt("FinalReference", trace_time, false, gc_timer);
    final_count =
      process_discovered_reflist(_discoveredFinalRefs, NULL, false,
                                 is_alive, keep_alive, complete_gc, task_executor);
  }

  // Phantom references
  size_t phantom_count = 0;
  {
    GCTraceTime tt("PhantomReference", trace_time, false, gc_timer);
    phantom_count =
      process_discovered_reflist(_discoveredPhantomRefs, NULL, false,
                                 is_alive, keep_alive, complete_gc, task_executor);
  }
    ...
}
```

可以看到，对于Soft references和Weak references clear_referent字段传入的都是true，这也符合我们的预期：对象不可达后，引用字段就会被置为null，然后对象就会被回收（对于软引用来说，如果内存足够的话，在Phase 1，相关的引用就会从refs_list中被移除，到Phase 3时refs_list为空集合）。

但对于Final references和 Phantom references，clear_referent字段传入的是false，也就意味着被这两种引用类型引用的对象，如果没有其他额外处理，只要Reference对象还存活，那引用的对象是不会被回收的。Final references和对象是否重写了finalize方法有关，不在本文分析范围之内，我们接下来看看Phantom references。

### **PhantomReference**

```scala
public class PhantomReference<T> extends Reference<T> {

    public T get() {
        return null;
    }

    public PhantomReference(T referent, ReferenceQueue<? super T> q) {
        super(referent, q);
    }

}
```

可以看到虚引用的get方法永远返回null，我们看个demo。

```pgsql
 public static void demo() throws InterruptedException {
        Object obj = new Object();
        ReferenceQueue<Object> refQueue =new ReferenceQueue<>();
        PhantomReference<Object> phanRef =new PhantomReference<>(obj, refQueue);

        Object objg = phanRef.get();
        //这里拿到的是null
        System.out.println(objg);
        //让obj变成垃圾
        obj=null;
        System.gc();
        Thread.sleep(3000);
        //gc后会将phanRef加入到refQueue中
        Reference<? extends Object> phanRefP = refQueue.remove();
         //这里输出true
        System.out.println(phanRefP==phanRef);
    }
```

从以上代码中可以看到，虚引用能够在指向对象不可达时得到一个'通知'（其实所有继承References的类都有这个功能），**需要注意的是GC完成后，phanRef.referent依然指向之前创建Object，也就是说Object对象一直没被回收！**

而造成这一现象的原因在上一小节末尾已经说了：对于Final references和 Phantom references，clear_referent字段传入的时false，也就意味着被这两种引用类型引用的对象，如果没有其他额外处理，在GC中是不会被回收的。

对于虚引用来说，从refQueue.remove();得到引用对象后，可以调用clear方法强行解除引用和对象之间的关系，使得对象下次可以GC时可以被回收掉。

### **End**

针对文章开头提出的几个问题，看完分析，我们已经能给出回答：

**1.我们经常在网上看到软引用的介绍是：在内存不足的时候才会回收，那内存不足是怎么定义的？为什么才叫内存不足**？

软引用会在内存不足时被回收，内存不足的定义和该引用对象get的时间以及当前堆可用内存大小都有关系，计算公式在上文中也已经给出。

**2.网上对于虚引用的介绍是：形同虚设，与其他几种引用都不同，虚引用并不会决定对象的生命周期。主要用来跟踪对象被垃圾回收器回收的活动。真的是这样吗**？

严格的说，虚引用是会影响对象生命周期的，如果不做任何处理，只要虚引用不被回收，那其引用的对象永远不会被回收。所以一般来说，从ReferenceQueue中获得PhantomReference对象后，如果PhantomReference对象不会被回收的话（比如被其他GC ROOT可达的对象引用），需要调用clear方法解除PhantomReference和其引用对象的引用关系。

**3.虚引用在Jdk中有哪些场景下用到了呢**？

DirectByteBuffer中是用虚引用的子类Cleaner.java来实现堆外内存回收的，后续会写篇文章来说说堆外内存的里里外外。

