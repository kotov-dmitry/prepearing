package loadbalancer.impl;

import loadbalancer.LoadBalancer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public class LoadBalancerImpl implements LoadBalancer {
    private final List<String> urls = new ArrayList<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static final Integer URLS_LIMIT = 10;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean registerUrl(String url) {
        readWriteLock.writeLock().lock();
        try {
            if (urls.contains(url)) {
                throw new RuntimeException("URL already register");
            }
            if (urls.size() >= URLS_LIMIT) {
                throw new RuntimeException("Too many urls register");
            } else {
                urls.add(url);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public String getUrl() {
        readWriteLock.readLock().lock();
        try {
            if (urls.isEmpty()) {
                throw new RuntimeException("Does not have any url");
            }
            int currentIndex = counter.getAndUpdate(item -> item < urls.size() - 1 ? item + 1 : 0);
            return urls.get(currentIndex);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
