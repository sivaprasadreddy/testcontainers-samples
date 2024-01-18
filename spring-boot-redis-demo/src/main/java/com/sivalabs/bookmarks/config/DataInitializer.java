package com.sivalabs.bookmarks.config;

import com.sivalabs.bookmarks.domain.Bookmark;
import com.sivalabs.bookmarks.domain.BookmarkRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    private final BookmarkRepository repo;

    @Override
    public void run(String... args) {
        long count = repo.count();
        if (count > 0) {
            log.info("Data already exists. Skipping data initialization");
            return;
        }
        repo.saveAll(getBookmarks());
    }

    private List<Bookmark> getBookmarks() {
        return List.of(
                Bookmark.builder()
                        .title("How (not) to ask for Technical Help?")
                        .url("https://sivalabs.in/how-to-not-to-ask-for-technical-help")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Announcing My SpringBoot Tips Video Series on YouTube")
                        .url("https://sivalabs.in/announcing-my-springboot-tips-video-series")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Kubernetes - Exposing Services to outside of Cluster using Ingress")
                        .url("https://sivalabs.in/kubernetes-ingress")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Kubernetes - Blue/Green Deployments")
                        .url("https://sivalabs.in/kubernetes-blue-green-deployments")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title(
                                "Kubernetes - Releasing a new version of the application using Deployment Rolling Updates")
                        .url("https://sivalabs.in/kubernetes-deployment-rolling-updates")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Getting Started with Kubernetes")
                        .url("https://sivalabs.in/getting-started-with-kubernetes")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Get Super Productive with Intellij File Templates")
                        .url("https://sivalabs.in/get-super-productive-with-intellij-file-templates")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Few Things I learned in the HardWay in 15 years of my career")
                        .url("https://sivalabs.in/few-things-i-learned-the-hardway-in-15-years-of-my-career")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("All the resources you ever need as a Java & Spring application developer")
                        .url(
                                "https://sivalabs.in/all-the-resources-you-ever-need-as-a-java-spring-application-developer")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("GoLang from a Java developer perspective")
                        .url("https://sivalabs.in/golang-from-a-java-developer-perspective")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Imposing Code Structure Guidelines using ArchUnit")
                        .url("https://sivalabs.in/impose-architecture-guidelines-using-archunit")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("SpringBoot Integration Testing using TestContainers Starter")
                        .url("https://sivalabs.in/spring-boot-integration-testing-using-testcontainers-starter")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Creating Yeoman based SpringBoot Generator")
                        .url("https://sivalabs.in/creating-yeoman-based-springboot-generator")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Testing REST APIs using Postman and Newman")
                        .url("https://sivalabs.in/testing-rest-apis-with-postman-newman")
                        .createdAt(Instant.now())
                        .build(),
                Bookmark.builder()
                        .title("Testing SpringBoot Applications")
                        .url("https://sivalabs.in/spring-boot-testing")
                        .createdAt(Instant.now())
                        .build());
    }
}
