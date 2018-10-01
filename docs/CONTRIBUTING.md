# Contributing

This document is intended to give guidelines for those who are willing to contribute in the development of the TCG backend.
It is divided in three parts:
- **Installation and usage** (how to prepare your computer for locally running the game and tests)
- **Project architecture** (which tools we use, and why)
- **Project structure** (how is the project organized)
- **Development practices** (what is expected from you as a collaborator when programming TCG)

## Installation and usage

You'll need to have the repository locally cloned: `git clone https://github.com/doner-kebard/card-game-backend`

### Leiningen

To work on the backend you'll need to install [Leiningen](https://leiningen.org/)

### Usage

`lein ring server-headless` to execute the backend's ring server

### Tests

While developing, please keep a terminal open at *all* times with the following command running:

`lein test-refresh`

This will give you instant feedback through the tests.

### Code-review tools

We have installed on the project a couple of tools to help us review the code.

- *Cloverage*: Run `lein cloverage` to get a report of how well our code is being tested.
- *Yagni*: Run `lein yagni` to get a report of which functions are not being used.
- *Eastwood*: Run `lein eastwood` to get a report of possible errors in your implementation and pain points.

## Project architecture

We use [Clojure](https://www.braveclojure.com) as the backend language for pretty much **everything** due to basically two reasons:

1. Because of the "everything is immutable" paradigm, it's very easy to reason about the logic of the code, which will probably get very complex as time goes on.
2. Due to its functional nature, it is *very* easy to run tests on it and a strict [TDD](https://www.youtube.com/watch?v=qkblc5WRn-U) discipline.

The backend also uses [Redis](https://redis.io) as a key-value storage mechanism for any persistent information necessary (such as the state of a game) since we will *always* know exactly what key we're searching for (we'll know the game ID, player ID, etc.).

Finally, the backend uses [Compojure](https://github.com/weavejester/compojure) to define it's [REST API](http://www.restapitutorial.com), which is the way the *frontend* will communicate with it.

## Project structure

- **project.clj**: Defines the project dependencies and entry point
- **test/**: Contains all the unit tests for the code, following the same structure as src/. Remember that a unit test must be done *before* the actual code.
- **src/rules**: Contains the code that will run in production, the actual logic.
- **src/persistence/persistence.clj**: This is the only file to interact directly with the database. All database calls *must* pass through here.
- **src/api/handler.clj**: This is the only file to interact directly with frontend. Any actions to take as a response to a frontend call *must* be defined here.

## Development practices

For any change or contribution, this is what is expected of you:

1. Announce within an issue that you want it assigned to you (or open said issue yourself).
2. Do not take on more than one issue at a time
3. Design the tests for your issue (unit tests always and in some cases e2e tests may be required)
4. Make them pass
5. Ask for a review from a collaborator
6. If changes are requested or travis does not pass, go to 3.

We will value the following things on your contribution *in this order*:

1. The tests pass
2. The code has tests
3. The tests are well-designed
4. The code is readable
5. The code is well-organized
6. The code works and fixes the issue
7. Any rule changed or included is also on the [Manual](https://github.com/kenan-rhoton/card-game/wiki/0.-Troll:-Card-Game-Rules)

If at any point you have a question, please ask it if possible through a comment so others may benefit from the answer.

### Other notes

1. We use a strict [TDD](http://www.javiersaldana.com/tech/2014/11/26/refactoring-the-three-laws-of-tdd.html) discipline to contribute to this project, and we expect at *least* that level of tests from any contribution. If we believe the tests don't cover enough production cases, you will be asked to review your code.
2. All merges should be done to the *develop* branch. At some points a pull request from develop to master will be made by a team member.
3. We always merge through `Squash & Merge`, which will cause you conflicts if you make a pull request from your personal fork's  master branch. We will warn you about this if we see it, as we think it's much more convenient to use your master branch *purely* to be up to date with ours and to make a branch for each feature.
