# Reactive

## 参考

- [八个层面比较 Java 8, RxJava, Reactor - 掘金](https://juejin.cn/post/6844903765670101006)



Java 圈子有一个怪事，那就是对 RxJava，Reactor，WebFlux 这些响应式编程的名词、框架永远处于渴望了解，感到新鲜，却又不甚了解，使用贫乏的状态。

关于响应式编程(Reactive Programming)，你可能有过这样的疑问：我们已经有了 Java8 的 Stream, CompletableFuture, 以及 Optional，为什么还必要存在 RxJava 和 Reactor？

回答这个问题并不难，如果在响应式编程中处理的问题非常简单，你的确不需要那些第三方类库的支持。 但随着复杂问题的出现，你写出了一堆难看的代码。然后这些代码变得越来越复杂，难以维护，而 RxJava 和 Reactor 具有许多方便的功能，可以解决你当下问题，并保障了未来一些可预见的需求。

