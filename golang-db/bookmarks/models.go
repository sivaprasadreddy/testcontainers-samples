package bookmarks

import "time"

type Bookmark struct {
	Id          int       `json:"id"`
	Title       string    `json:"title"`
	Url         string    `json:"url"`
	CreatedDate time.Time `json:"created_date"`
	UpdatedDate time.Time `json:"updated_date"`
}
