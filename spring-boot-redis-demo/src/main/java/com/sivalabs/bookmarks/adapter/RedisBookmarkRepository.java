package com.sivalabs.bookmarks.adapter;

import com.sivalabs.bookmarks.domain.Bookmark;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

interface RedisBookmarkRepository
        extends ListCrudRepository<Bookmark, String>, ListPagingAndSortingRepository<Bookmark, String> {}
