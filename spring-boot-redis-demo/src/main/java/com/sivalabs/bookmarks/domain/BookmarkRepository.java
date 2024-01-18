package com.sivalabs.bookmarks.domain;

import java.util.Optional;

public interface BookmarkRepository {
    PagedResult<Bookmark> findAll(int pageNo);

    Optional<Bookmark> findById(String id);

    Bookmark save(Bookmark bookmark);

    Iterable<Bookmark> saveAll(Iterable<Bookmark> bookmarks);

    void deleteById(String id);

    long count();
}
