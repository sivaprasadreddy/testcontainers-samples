package main

import (
	"context"
	"fmt"
	"github.com/gorilla/mux"
	log "github.com/sirupsen/logrus"
	"github.com/sivaprasadreddy/testcontainers-samples/golang-db/config"
	"github.com/stretchr/testify/assert"
	"github.com/testcontainers/testcontainers-go"
	"github.com/testcontainers/testcontainers-go/wait"
	"net/http"
	"net/http/httptest"
	"os"
	"testing"
)

const postgresTestUserName = "test"
const postgresTestPassword = "test"
const postgresTestDatabase = "test"

var cfg config.AppConfig
var app *App
var router *mux.Router

func TestMain(m *testing.M) {
	//Common Setup
	ctx := context.Background()
	pgC, terminateContainerFn, err := SetupTestDatabase(ctx)
	if err != nil {
		log.Error("failed to setup Postgres container")
		panic(err)
	}
	defer terminateContainerFn()
	overrideEnv(ctx, pgC)

	cfg = config.GetConfig()
	app = NewApp(cfg)
	router = app.Router

	code := m.Run()

	//Common Teardown
	os.Exit(code)
}

func SetupTestDatabase(ctx context.Context) (testcontainers.Container, func(), error) {
	req := testcontainers.ContainerRequest{
		Image: "postgres:14-alpine",
		Env: map[string]string{
			"POSTGRES_DB":       postgresTestDatabase,
			"POSTGRES_USER":     postgresTestUserName,
			"POSTGRES_PASSWORD": postgresTestPassword,
		},
		ExposedPorts: []string{"5432/tcp"},
		WaitingFor:   wait.ForListeningPort("5432/tcp"),
	}

	pgC, err := testcontainers.GenericContainer(ctx, testcontainers.GenericContainerRequest{
		ContainerRequest: req,
		Started:          true,
	})
	if err != nil {
		panic(fmt.Sprintf("%v", err))
	}
	closeContainer := func() {
		log.Info("terminating container")
		err := pgC.Terminate(ctx)
		if err != nil {
			log.Errorf("error terminating postgres container: %s", err)
			panic(fmt.Sprintf("%v", err))
		}
	}

	return pgC, closeContainer, nil
}

func overrideEnv(ctx context.Context, pgC testcontainers.Container) {
	host, _ := pgC.Host(ctx)
	p, _ := pgC.MappedPort(ctx, "5432/tcp")
	port := p.Int()
	os.Setenv("APP_DB_HOST", host)
	os.Setenv("APP_DB_PORT", fmt.Sprint(port))
	os.Setenv("APP_DB_USERNAME", postgresTestUserName)
	os.Setenv("APP_DB_PASSWORD", postgresTestPassword)
	os.Setenv("APP_DB_NAME", postgresTestDatabase)
	os.Setenv("APP_DB_RUN_MIGRATIONS", "true")
}

func TestGetAllBookmarks(t *testing.T) {
	req, _ := http.NewRequest("GET", "/api/bookmarks", nil)
	response := executeRequest(req)

	checkResponseCode(t, http.StatusOK, response.Code)

	actualResponseJson := response.Body.String()
	assert.NotEqual(t, "[]", actualResponseJson)

	if actualResponseJson == "[]" {
		t.Errorf("Expected an non-empty array. Got %s", actualResponseJson)
	}
}

func executeRequest(req *http.Request) *httptest.ResponseRecorder {
	rr := httptest.NewRecorder()
	router.ServeHTTP(rr, req)

	return rr
}

func checkResponseCode(t *testing.T, expected, actual int) {
	if expected != actual {
		t.Errorf("Expected response code %d. Got %d\n", expected, actual)
	}
}
