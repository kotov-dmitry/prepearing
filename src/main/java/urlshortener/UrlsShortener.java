package urlshortener;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public interface UrlsShortener {
    String createShortUrl(String url);

    String getUrl(String shortUrl);
}
