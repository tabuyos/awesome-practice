package com.tabuyos.quarkus.reactive;

import com.tabuyos.quarkus.reactive.ReactiveGreetingResourceTest;
import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeReactiveGreetingResourceIT extends ReactiveGreetingResourceTest {

    // Execute the same tests but in native mode.
}
