# card-game

FIXME: description

## Installation

Install [Docker](https://store.docker.com/search?type=edition&offering=community).

`git clone https://github.com/kenan-rhoton/card-game`

## Usage

`docker-compose build && docker-compose up`

## Development

While working on backend stuff (Clojure), please keep a terminal open at *all* times with the following command running:

`docker-compose build; docker-compose run backend lein autoexpect; docker-compose down`

This will give you instant feedback through the tests.

## License

Copyright © 2018 Kenan Rhoton

Distributed under the MIT License.
