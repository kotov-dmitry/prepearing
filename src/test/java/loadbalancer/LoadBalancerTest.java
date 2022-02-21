package loadbalancer;

import loadbalancer.impl.LoadBalancerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public class LoadBalancerTest {

    private LoadBalancerImpl loadBalancer;

    @BeforeEach
    public void setUp() {
        loadBalancer = new LoadBalancerImpl();
    }

    @Test
    public void testRegisterCorrectUrl() {
        assertTrue(loadBalancer.registerUrl("http://localhost/1"));
    }

    @Test
    public void testRegisterSameUrl() {
        loadBalancer.registerUrl("http://localhost/1");
        try {
            loadBalancer.registerUrl("http://localhost/1");
            fail("Possible add same urls");
        } catch (RuntimeException e) {
            assertEquals("URL already register", e.getMessage());
        }
    }

    @Test
    public void testRegisterToManyUrls() {
        try {
            IntStream.range(0, 11)
                    .forEach(position -> loadBalancer.registerUrl("http://localhost/" + position));
            fail();
        } catch (RuntimeException e) {
            assertEquals("Too many urls register", e.getMessage());
        }
    }

    @Test
    public void testGetUrlWhenNoRegisteredUrl() {
        try {
            loadBalancer.getUrl();
            fail("Possible get url when no one url was added");
        } catch (RuntimeException e) {
            assertEquals("Does not have any url", e.getMessage());
        }
    }

    @Test
    public void testRepeatableGetUrl() {
        IntStream.range(0, 3)
                .forEach(position -> loadBalancer.registerUrl("http://localhost/" + position));
        IntStream.range(0, 10)
                .forEach(position -> assertEquals("http://localhost/" + (position % 3), loadBalancer.getUrl()));
    }
}
