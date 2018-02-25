import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import Game from "@/components/Game.vue";

describe("Game", () => {
  it("Defaults to waiting for an opponent", () => {
    const $route = { resolve: () => {}, params: { gameID: "5" } };
    const wrapper = shallow(Game, { mocks: { $route } });
    wrapper.setData({
    	status: "waiting"
    });
    expect(wrapper.contains("#join-link")).to.be.true;
    expect(wrapper.find("#game-status").text()).to.eql("Waiting for opponent");
  });
});
