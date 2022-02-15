package org.acme;

import io.quarkus.test.junit.NativeImageTest;

/**
 * @author tabuyos
 */
@NativeImageTest
public class NativeGreetingResourceIT extends GreetingResourceTest {

    // Execute the same tests but in native mode.
}