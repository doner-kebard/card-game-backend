set -e # Stop the script on any failure or error
docker-compose -f docker-compose.yml -f docker-compose.e2e.yml down
docker-compose -f docker-compose.yml -f docker-compose.e2e.yml build
docker-compose run backend lein test
docker-compose run frontend yarn test
docker-compose run frontend yarn install
docker-compose -f docker-compose.yml -f docker-compose.e2e.yml run e2e test
docker-compose -f docker-compose.yml -f docker-compose.e2e.yml down
