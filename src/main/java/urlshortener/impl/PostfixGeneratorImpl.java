package urlshortener.impl;

import urlshortener.PostfixGenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public class PostfixGeneratorImpl implements PostfixGenerator {
    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public String generate() {
        return String.valueOf(counter.incrementAndGet());
    }
}
