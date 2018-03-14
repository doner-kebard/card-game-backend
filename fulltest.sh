docker-compose down
docker-compose build
docker-compose run backend lein test
docker-compose -f docker-compose.yml -f docker-compose.e2e.yml run e2e test-delayed
