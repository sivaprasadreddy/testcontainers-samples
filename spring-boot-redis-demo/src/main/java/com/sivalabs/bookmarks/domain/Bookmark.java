package com.sivalabs.bookmarks.domain;

import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("bookmarks")
public class Bookmark {
    @Id
    private String id;

    @NotEmpty(message = "Title is mandatory")
    private String title;

    @NotEmpty(message = "Url is mandatory")
    private String url;

    private Instant createdAt;
}
