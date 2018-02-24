import { shallow } from "@vue/test-utils";
import CreateGame from "@/components/CreateGame.vue";

describe("CreateGame", () => {
  it("renders", () => {
    shallow(CreateGame, {});
  });
});
