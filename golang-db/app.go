package main

import (
	"database/sql"
	"github.com/gorilla/mux"
	"github.com/sivaprasadreddy/testcontainers-samples/golang-db/bookmarks"
	"github.com/sivaprasadreddy/testcontainers-samples/golang-db/config"
	"github.com/sivaprasadreddy/testcontainers-samples/golang-db/database"
	"net/http"
)

type App struct {
	Router             *mux.Router
	db                 *sql.DB
	bookmarkController *bookmarks.BookmarkController
}

func NewApp(config config.AppConfig) *App {
	app := &App{}
	app.init(config)
	return app
}

func (app *App) init(config config.AppConfig) {
	app.db = database.GetDb(config)

	bookmarksRepo := bookmarks.NewBookmarkRepo(app.db)
	app.bookmarkController = bookmarks.NewBookmarkController(bookmarksRepo)

	app.Router = app.setupRoutes()
}

func (app *App) setupRoutes() *mux.Router {
	router := mux.NewRouter().StrictSlash(true)

	router.HandleFunc("/api/bookmarks", app.bookmarkController.GetAll).Methods(http.MethodGet)

	return router
}
