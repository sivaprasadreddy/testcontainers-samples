package bookmarks

import (
	"encoding/json"
	log "github.com/sirupsen/logrus"
	"net/http"
)

type BookmarkController struct {
	repo BookmarkRepository
}

func NewBookmarkController(repo BookmarkRepository) *BookmarkController {
	return &BookmarkController{repo}
}

func (b *BookmarkController) GetAll(w http.ResponseWriter, r *http.Request) {
	log.Info("Fetching all bookmarks")
	bookmarks, err := b.repo.GetBookmarks()
	if err != nil {
		log.Errorf("Error while fetching bookmarks")
		RespondWithError(w, http.StatusInternalServerError, "Unable to fetch bookmarks")
		return
	}
	if bookmarks == nil {
		bookmarks = []Bookmark{}
	}
	RespondWithJSON(w, http.StatusOK, bookmarks)
}

func RespondWithError(w http.ResponseWriter, code int, message string) {
	RespondWithJSON(w, code, map[string]string{"error": message})
}

func RespondWithJSON(w http.ResponseWriter, code int, payload interface{}) {
	response, _ := json.Marshal(payload)
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	w.Write(response)
}
