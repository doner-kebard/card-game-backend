# card-game

FIXME: description

## Installation

Install [Docker](https://store.docker.com/search?type=edition&offering=community).

`git clone https://github.com/kenan-rhoton/card-game`

## Usage

`docker-compose build && docker-compose up`

## Development

While working on backend stuff (Clojure), please keep a terminal open at *all* times with the following command running:

`docker-compose build; docker-compose run backend lein test-refresh; docker-compose down`

If working on frontend stuff, use instead:

`docker-compose build; docker-compose up -d; docker-compose run frontend yarn test -w; docker-compose down`

This will give you instant feedback through the tests.

For End-to-End testing, use testcafe to run the e2e tests:

`testcafe all e2e/*.js`

## License

Copyright © 2018 Kenan Rhoton

Distributed under the MIT License.
