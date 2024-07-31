#!/bin/bash
docker compose down
docker system prune -a --volumes
docker-compose up --build