package urlshortener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import urlshortener.impl.UrlsShortenerImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author dkotov
 * @since 21.02.2022
 */
@ExtendWith(MockitoExtension.class)
public class UrlsShortenerTest {
    private final String prefix = "http://prefix/";

    private UrlsShortener urlsShortener;
    private PostfixGenerator postfixGenerator;

    @BeforeEach
    public void setUp(@Mock PostfixGenerator postfixGenerator) {
        this.postfixGenerator = postfixGenerator;
        urlsShortener = new UrlsShortenerImpl(postfixGenerator);
    }

    @Test
    public void testCreateShortUrl() {
        Mockito.when(postfixGenerator.generate()).thenReturn("1");
        String result = urlsShortener.createShortUrl("http://real");
        assertEquals(prefix + "1", result);
    }

    @Test
    public void testTryAddSameUrl() {
        Mockito.when(postfixGenerator.generate()).thenReturn("1");
        urlsShortener.createShortUrl("http://real");
        String result = urlsShortener.createShortUrl("http://real");
        Mockito.verify(postfixGenerator, Mockito.times(1)).generate();
        assertEquals(prefix + "1", result);
    }

    @Test
    public void testGetNotExistUrl() {
        try {
            urlsShortener.getUrl("https://not-exist-url");
            fail("URL not found exception excepted");
        } catch (RuntimeException e) {
            assertEquals("URL not found", e.getMessage());
        }
    }

    @Test
    public void testGetExistUrl() {
        Mockito.when(postfixGenerator.generate()).thenReturn("1");
        String shortUrl = urlsShortener.createShortUrl("http://real");
        String result = urlsShortener.getUrl(shortUrl);
        assertEquals("http://real", result);
    }
}
