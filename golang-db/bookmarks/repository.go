package bookmarks

import (
	"context"
	"database/sql"
)

type BookmarkRepository interface {
	GetBookmarks() ([]Bookmark, error)
}

type bookmarkRepo struct {
	db *sql.DB
}

func NewBookmarkRepo(db *sql.DB) BookmarkRepository {
	var repo BookmarkRepository = bookmarkRepo{db}
	return repo
}

func (b bookmarkRepo) GetBookmarks() ([]Bookmark, error) {
	ctx := context.Background()
	tx, err := b.db.BeginTx(ctx, nil)
	if err != nil {
		return nil, err
	}
	query := "SELECT id, title, url, created_at FROM bookmarks"
	rows, err := tx.QueryContext(ctx, query)
	if err != nil {
		tx.Rollback()
		return nil, err
	}
	var bookmarks []Bookmark

	defer rows.Close()
	for rows.Next() {
		var bookmark = Bookmark{}
		err = rows.Scan(&bookmark.Id, &bookmark.Title, &bookmark.Url, &bookmark.CreatedDate)
		if err != nil {
			tx.Rollback()
			return nil, err
		}
		bookmarks = append(bookmarks, bookmark)
	}
	err = tx.Commit()
	if err != nil {
		return nil, err
	}
	return bookmarks, nil
}
