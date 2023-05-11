/*
 * copyright(c) 2018-2023 tabuyos all right reserved.
 */
package com.tabuyos.reactor.test;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.CorePublisher;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

/**
 * PS
 *
 * @author tabuyos
 * @since 2023/5/10
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class PS {

  public static void main(String[] args) {

    // Mono.just(effectWithTime(30, "hello"))
    //   .subscribe(message -> {
    //     System.out.println(System.currentTimeMillis());
    //     System.out.println(message);
    //   });

    // Mono.from(new MyPublisher())
    //   .doOnNext(message -> {
    //     System.out.println("===== next =====");
    //     System.out.println("message0: " + message);
    //   })
    //   .doOnNext(message -> {
    //     System.out.println("===== next =====");
    //     System.out.println("message1: " + message);
    //   })
    //   .subscribe(message -> {
    //     System.out.println("===== over =====");
    //     System.out.println("message: " + message);
    //   });

    // MyPublisher publisher = new MyPublisher();
    // publisher.subscribe(new MySubscriber());


    // Flux.from(new MyPublisher())
    //   .doOnNext(message -> {
    //     System.out.println("===== next =====");
    //     System.out.println("message: " + message);
    //   })
    //   .collectList()
    //   .subscribe(message -> {
    //     System.out.println("===== over =====");
    //     System.out.println("message: " + message);
    //   });


    Mono.from(new MyPublisher())
      .doOnNext(message -> {
        System.out.println("===== next =====");
        System.out.println("message: " + message);
      })
      .subscribe(message -> {
        System.out.println("===== over =====");
        System.out.println("message: " + message);
      });


    // Mono.fromSupplier(() -> {
    //   RestTemplate template = new RestTemplate();
    //   ResponseEntity<String> response = template.getForEntity("http://localhost:8231/api/auth/identify/check-validity?name=tabuyossss", String.class);
    //   String body = response.getBody();
    //   System.out.println(body);
    //   return body;
    // }).subscribe(System.out::println);

    // Mono.from(publisher)
    //   .subscribe(new MySubscriber());

    while (true) {

    }
  }

  public static String effectWithTime(long duration, String message) {
    System.out.println(message + " :> " + System.currentTimeMillis());
    System.out.println(message);
    try {
      TimeUnit.SECONDS.sleep(duration);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return message;
  }


  static class MyPublisher implements CorePublisher<String> {
    Subscriber<? super String> subscriber;

    @Override
    public void subscribe(@NotNull CoreSubscriber<? super String> subscriber) {
      subscriber.onSubscribe(new MySubscription(subscriber));
    }

    @Override
    public void subscribe(Subscriber<? super String> subscriber) {
      subscriber.onSubscribe(new MySubscription(subscriber));
    }
  }

  static class MySubscriber implements CoreSubscriber<String> {
    @Override
    public void onSubscribe(Subscription subscription) {
      subscription.request(4);
    }

    @Override
    public void onNext(String s) {
      System.out.println("on next: " + s);
    }

    @Override
    public void onError(Throwable t) {
      System.out.println("on error: " + t.getMessage());
    }

    @Override
    public void onComplete() {
      System.out.println("on complete");
    }
  }

  static class MySubscription implements Subscription {
    Subscriber<? super String> subscriber;
    RestTemplate template = new RestTemplate();
    int requested = 5;

    public MySubscription(Subscriber<? super String> subscriber) {
      this.subscriber = subscriber;
    }

    @Override
    public void request(long n) {
      while (requested > 0) {
        ResponseEntity<String> response = template.getForEntity("http://localhost:8231/api/auth/identify/check-validity?name=tabuyossss", String.class);
        String body = response.getBody();
        subscriber.onNext(body == null ? "" : body);
      }
      subscriber.onComplete();
    }

    @Override
    public void cancel() {
    }
  }
}
