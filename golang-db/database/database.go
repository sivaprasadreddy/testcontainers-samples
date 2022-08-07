package database

import (
	"database/sql"
	"fmt"
	_ "github.com/lib/pq"
	log "github.com/sirupsen/logrus"
	"github.com/sivaprasadreddy/testcontainers-samples/golang-db/config"

	"github.com/golang-migrate/migrate/v4"
	_ "github.com/golang-migrate/migrate/v4/database/postgres"
	_ "github.com/golang-migrate/migrate/v4/source/file"
)

func GetDb(config config.AppConfig) *sql.DB {
	connStr := fmt.Sprintf("host=%s port=%d user=%s password=%s dbname=%s sslmode=disable",
		config.DbHost, config.DbPort, config.DbUserName, config.DbPassword, config.DbDatabase)
	db, err := sql.Open("postgres", connStr)
	if err != nil {
		log.Fatal(err)
	}
	if config.DbRunMigrations {
		runMigrations(config)
	}
	return db
}

func runMigrations(config config.AppConfig) {
	sourceURL := config.DbMigrationsLocation
	databaseURL := fmt.Sprintf("postgres://%s:%s@%s:%d/%s?sslmode=disable",
		config.DbUserName, config.DbPassword, config.DbHost, config.DbPort, config.DbDatabase)
	//log.Printf("DB Migration sourceURL: %s\n", sourceURL)
	//log.Printf("DB Migration URL: %s\n", databaseURL)
	m, err := migrate.New(sourceURL, databaseURL)
	if err != nil {
		log.Fatalf("Database migration error: %v", err)
	}
	if err := m.Up(); err != nil && err != migrate.ErrNoChange {
		log.Fatalf("Databse migrate.up() error: %v", err)
	}
	log.Printf("Database migration completed")
}
