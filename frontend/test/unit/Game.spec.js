import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import Game from "@/components/Game.vue";

describe("Game", () => {
	it("Defaults to waiting for an opponent", () => {
    const wrapper = shallow(Game, {});
    expect(wrapper.contains("#join-link")).to.be.true;
	})
});

