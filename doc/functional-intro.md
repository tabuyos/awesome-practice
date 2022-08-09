# Functional

## 参考

- [在 Java 中看函数式编程 - 掘金](https://juejin.cn/post/7002790565074960414)



## Lambda Calculus - λ 演算

函数式编程的主要理论基础是 λ 演算

λ 演算中包含了如下概念

- **匿名函数 (Anonymous Function)**

- **高阶函数 (High-Order Function)**

  - 以另一个函数的作为参数或是返回值的函数

- **柯里化 (Currying)**

  - 任意多参数的函数都可以通过嵌套转换成单参数的高阶函数

- **闭包 (Closure)**

  - 函数和函数内部能访问到的变量 (也叫环境) 的总和, 就是一个闭包

  - ```js
    var local = 'abc';
    function foo() {
      console.log(local);
    }
    // 上面三行代码在一个立即执行函数中, 组成了一个闭包
    ```

#### 总结

- **Functor** 类型解决的是如何将一个 **in context 的 value** 输入到**普通 function** 中, 并得到一个 **in context 的 value**
- **Applicative** 类型解决的是如何将一个 **in context 的 value** 输入到**只返回普通 value 的 in context 的 function** 中, 并得到一个 **in context 的 value**
- **Monad** 类型解决的是如何将一个 **in context 的 value** 输入到**只返回 in context 的 value 的 function** 中, 并得到一个 **in context 的 value**

#### 范畴论

- 在范畴论里, 一个范畴 (category) 由三部分组成：

  1. 一系列的对象 (object)
     - 数学上的群, 环, 甚至简单的有理数, 无理数等都可以归为一个对象
     - 对应到编程语言里, 可以理解为一个类型, 比如说整型, 布尔型
     - **类型事实上可以看成是值的集合**, 例如整型就是由 0, 1, 2...等组成的, 因此范畴论里的对象简单理解就可以看成是值(value) 的集合
  2. 一系列的态射 (morphism)
     - 态射指的是一种映射关系
     - 态射的作用就是把一个对象 A 里的值 va 映射为另一个对象 B 里的值 vb, 这和代数里的映射概念是很相近的, 因此也有单射, 满射等区分
     - 态射的存在反映了对象内部的结构, 这是范畴论用来研究对象的主要手法：对象内部的结构特性是通过与别的对象的关系反映出来的, 动静是相对的, 范畴论通过研究关系来达到探知对象的内部结构的目的
  3. 一个组合 (composition) 操作符, 用点 (.) 表示, 用于将态射进行组合
     - 组合操作符的作用是将两个态射进行组合, 例如, 假设存在态射 f: A -> B, g: B -> C,  则 g.f : A -> C

- 一个结构要想成为一个范畴, 除了必须包含上述三样东西, 它还要满足以下三个限制:

  1. 态射要满足结合律, 即 f.(g.h) = (f.g).h
  2. 态射在这个结构必须是封闭的, 也就是, 如果存在态射 f, g, 则必然存在 h = f.g
  3. 对结构中的每一个对象 A, 必须存在一个单位态射 Ia: A -> A,  对于单位态射, 显然, 对任意其它态射 f, f.I = f

- **“一个单子 (Monad )说白了不过就是自函子范畴上的一个幺半群而已 (A monad is just a monoid in the category of endofunctors)”**

- 要理解上面这句话所需要知道的几个知识点

  - 半群 (semigroup) 与幺半群 (monoid)

    - 数学里定义的群 (group): G 为非空集合, 如果在 G 上定义的二元运算 *, 满足以下情况则称 G 是群

      1. 封闭性 (Closure) : 对于任意 a, b ∈ G, 有 a*b ∈ G
      2. 结合律 (Associativity) : 对于任意 a, b, c ∈ G, 有 (a*b) * c = a * (b*c )
      3. 幺元 (Identity) : 存在幺元 e, 使得对于任意 a ∈ G, e*a = a*e = a
      4. 逆元: 对于任意 a ∈ G, 存在逆元 a^-1, 使得 a^-1 * a = a * a^-1 = e

    - 如果仅满足封闭性和结合律, 则称 G 是一个半群 (Semigroup)

    - 如果仅满足封闭性, 结合律并且有幺元, 则称 G 是一个幺半群 (Monoid)

    - 用 Java 代码实现半群和幺半群

      ```java
      // 半群
      interface Semigroup<T> {
        // 二元运算
        T append(T a, T b);
      }
      
      class IntSemigroup implements Semigroup<Integer> {
        @Override
        public Integer append(Integer a, Integer b) {
          return a + b;
        }
      }
      
      // 幺半群
      interface Monoid<T> extends Semigroup<T> {
        // 幺元
        T zero();
      }
      
      class IntMonoid implements Monoid<Integer> {
        @Override
        public Integer append(Integer a, Integer b) {
          return a + b;
        }
        @Override
        public Integer zero() {
          return 0;
        }
      }
      
      class StringMonoid implements Monoid<String> {
        @Override
        public String append(String a, String b) {
          return a + b;
        }
        @Override
        public String zero() {
          return "";
        }
      }
      
      class ListMonoid<T> implements Monoid<List<T>> {
        @Override
        public List<T> append(List<T> a, List<T> b) {
          List<T> ret = new ArrayList<>(a);
          ret.addAll(b);
          return ret;
        }
        @Override
        public List<T> zero() {
          return new ArrayList<>();
        }
      }
      ```

  - 自函子 (Endofunctor)

    - 先看看自函数 (Endofunction), 自函数是入参和出参的类型一致, 比如 `(x:Int) => x * 2` 或 `(x:Int) => x * 3` 都属于自函数

    - 再看看恒等函数 (Identity function), 恒等函数是什么也不做, 传入什么参数返回什么参数, 它属于自函数的一种特例

    - 自函子映射的结果是自身, 下图是一个简单的情况:

      - ![img](functional-intro/d046f8145c06486cbf9b4a6532dace98tplv-k3u1fbpfcp-zoom-in-crop-mark3024000.awebp)

      - 假设这个自函子为 `F`, 则对于 `F[Int]` 作用的结果仍是 `Int`, 对于函数 `f: Int=>String` 映射的结果 `F[f]` 也仍是函数 `f`

        - 所以这个自函子实际是一个恒等函子 (Identity functor) (自函子的一种特例), 即对范畴中的元素和关系不做任何改变

      - 把 haskell 里的所有类型和函数都放到一个范畴里, 取名叫 **Hask**, 那么对于这个 Hask 的范畴, 它看上去像是这样的：

        - ![img](functional-intro/a905ec52a9364c0bb9a551855a5175e5tplv-k3u1fbpfcp-zoom-in-crop-mark3024000.awebp)

        - `A`, `B` 代表普通类型如 `String`, `Int`, `Boolean` 等, 这些 (有限的) 普通类型是一组类型集合, 还有一组类型集合是衍生类型 (即由类型构造器与类型参数组成的), 这是一个无限集合 (可以无限衍生下去)

          - 这样 `范畴 Hask` 就涵盖了 haskell 中所有的类型

        - 对于范畴 Hask 来说, 如果有一个函子 F, 对里面的元素映射后, 其结果仍属于Hask, 比如我们用 `List` 这个函子：

        - ```css
          List[A], List[List[A]], List[List[List[A]]]...
          复制代码
          ```

        - 发现这些映射的结果也是属于 Hask 范畴 (子集), 所以这是一个自函子, 实际上在 Hask 范畴上的所有函子都是自函子

- 现在再来看 "一个单子 (Monad) 说白了不过就是自函子范畴上的一个幺半群而已" 这句话

  - 其实真正的原话是 "总而言之, 一切范畴 X 上的 Monad 都不过是范畴 X 上的自函子所构成范畴中的一个幺半群, 二元运算被自函子的组合所代替, 幺元则取恒等函子 (All told, a monad in X is just a monoid in the category of endofunctors of X, with product × replaced by composition of endofunctors and unit set by the identity endofunctor.)"
  - 可以结合[这个回答](https://link.juejin.cn?target=https%3A%2F%2Fwww.zhihu.com%2Fquestion%2F24972880%2Fanswer%2F134078723)进行理解

