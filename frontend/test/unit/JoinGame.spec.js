import { shallow } from "@vue/test-utils";
import JoinGame from "@/components/JoinGame.vue";

describe("JoinGame", () => {
  it("renders", () => {
    const $route = { params: { gameID: 5 } };
    shallow(JoinGame, { mocks: { $route }});
  });
});
