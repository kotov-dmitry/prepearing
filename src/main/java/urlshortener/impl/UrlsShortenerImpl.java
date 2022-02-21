package urlshortener.impl;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.DualHashBidiMap;
import urlshortener.PostfixGenerator;
import urlshortener.UrlsShortener;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public class UrlsShortenerImpl implements UrlsShortener {
    private final String prefix = "http://prefix/";
    private final PostfixGenerator generator;
    private final BidiMap urlByShortUrl = new DualHashBidiMap();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public UrlsShortenerImpl(PostfixGenerator generator) {
        this.generator = generator;
    }

    @Override
    public String createShortUrl(String url) {
        readWriteLock.writeLock().lock();
        try {
            if (urlByShortUrl.containsValue(url)) {
                return (String) urlByShortUrl.getKey(url);
            } else {
                String shortUrl = prefix + generator.generate();
                urlByShortUrl.put(shortUrl, url);
                return shortUrl;
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public String getUrl(String shortUrl) {
        readWriteLock.readLock().lock();
        try {
            Object result = urlByShortUrl.get(shortUrl);
            if (result != null) {
                return (String) result;
            } else {
                throw new RuntimeException("URL not found");
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
