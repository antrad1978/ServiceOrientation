package ngt.consul.controllers;

import java.net.URI;
import java.util.*;

import ngt.consul.models.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleServiceController {

    private static List<Article> articles = new ArrayList<>();

    static {
        Article articles = new Article("iPhone", "...");
        ArticleServiceController.articles.add(articles);
        articles = new Article("Samsung Galaxy", "...");
        ArticleServiceController.articles.add(articles);
        articles = new Article("HTC", "...");
        ArticleServiceController.articles.add(articles);
    }

    @Autowired
    private DiscoveryClient discoveryClient;


    @GetMapping("/services")
    public Optional<URI> serviceURL(){
        return discoveryClient.getInstances("articles-service")
                .stream()
                .map(instance -> instance.getUri())
                .findFirst();
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET)
    public List<Article> getArticles() {
        return articles;
    }
}
