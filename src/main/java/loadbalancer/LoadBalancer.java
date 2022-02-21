package loadbalancer;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public interface LoadBalancer {
    boolean registerUrl(String url);

    String getUrl();
}
