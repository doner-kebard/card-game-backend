import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import Game from "@/views/Game.vue";

describe("Game", () => {
  it("renders a hand of cards", async () => {
    const wrapper = shallow(Game, {});
    const handOfCards = await wrapper.findAll(".hand .card");
    expect(handOfCards.length).to.equal(12);
    expect(handOfCards.at(0).text()).to.equal("Power: 10");
    expect(handOfCards.at(10).text()).to.equal("Power: 10");
  });
  it("renders rows", () => {
    const wrapper = shallow(Game, {});
    expect(wrapper.findAll(".game-row").length).to.equal(5);
  });
  it("renders my score", () => {
    const wrapper = shallow(Game, {});
    expect(wrapper.contains("#my-score")).to.be.true;
  });
  it("renders opponent's score", () => {
    const wrapper = shallow(Game, {});
    expect(wrapper.contains("#opponent-score")).to.be.true;
  });
});
