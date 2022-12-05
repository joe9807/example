rem start "Docker Postgres" 
docker run --name joe-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=example -p 5432:5432 postgres