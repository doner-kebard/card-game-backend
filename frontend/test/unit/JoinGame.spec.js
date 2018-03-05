import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import JoinGame from "@/components/JoinGame.vue";

describe("JoinGame", () => {
  it("renders", async () => {
    var pushParams = {};
    const $route = { params: { joinID: 5 } };
    const $router = { push: a => (pushParams = a) };
    await shallow(JoinGame, {
      mocks: { $route, $router },
      methods: {
        fetchGame: async () => {
          return { data: { "player-id": 3 } };
        }
      }
    });
    expect(pushParams).to.eql({
      name: "Game",
      params: {
        gameID: 5,
        playerID: 3
      }
    });
  });
});
