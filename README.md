# card-game

An iteratively designed card game.

## Installation

Install [Docker](https://store.docker.com/search?type=edition&offering=community).

`git clone https://github.com/kenan-rhoton/card-game`

## Usage

`docker-compose build && docker-compose up`

## Development

While working on backend stuff (Clojure), please keep a terminal open at *all* times with the following command running:

`docker-compose run backend lein test-refresh`

This will give you instant feedback through the tests.

For End-to-End testing, use [testcafe](http://devexpress.github.io/testcafe/documentation/test-api/) to run the e2e tests:

`docker-compose -f docker-compose.yml -f docker-compose.e2e.yml run e2e test`

To run *all* tests, use: 

`bash fulltest.sh`

## License

Copyright © 2018 Kenan Rhoton

Distributed under the MIT License.
