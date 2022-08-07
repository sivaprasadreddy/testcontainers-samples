package main

import (
	"fmt"
	log "github.com/sirupsen/logrus"
	"github.com/sivaprasadreddy/testcontainers-samples/golang-db/config"
	"net/http"
	"time"
)

func main() {
	cfg := config.GetConfig()
	app := NewApp(cfg)

	port := fmt.Sprintf(":%d", cfg.AppPort)
	srv := &http.Server{
		Handler:        app.Router,
		Addr:           port,
		ReadTimeout:    10 * time.Second,
		WriteTimeout:   10 * time.Second,
		MaxHeaderBytes: 1 << 20,
	}

	log.Printf("listening on port %d", cfg.AppPort)
	if err := srv.ListenAndServe(); err != nil {
		log.Fatal(err)
	}
}
