import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import Game from "@/components/Game.vue";

const $route = { resolve: () => {}, params: { gameID: "5" } };
const options = {
  mocks: { $route },
  methods: {
    updateGame: function() {
      return true;
    }
  }
};

const sampleData = {
  response: {
    status: "Playing",
    hand: [{ power: 4 }],
    rows: [[{ power: 20, owner: "me" }], []]
  }
};

describe("Game", () => {
  it("Defaults to waiting for an opponent", () => {
    const wrapper = shallow(Game, options);
    wrapper.setData({
      response: { status: "Waiting for an opponent" }
    });
    expect(wrapper.contains("#join-link")).to.be.true;
    expect(wrapper.find("#game-status").text()).to.eql("Waiting for opponent");
  });

  it("Shows board when playing", () => {
    const wrapper = shallow(Game, options);
    wrapper.setData(sampleData);
    expect(wrapper.find(".hand").findAll(".card").length).eql(1);
    expect(wrapper.findAll(".game-row").length).eql(2);
    expect(
      wrapper
        .findAll(".game-row")
        .at(0)
        .findAll(".card").length
    ).eql(1);
  });

  it("Can play cards", () => {
    const wrapper = shallow(Game, options);
    wrapper.setData(sampleData);
    expect(
      wrapper
        .find(".hand")
        .find(".card")
        .attributes().draggable
    ).to.eql("true");
    expect(wrapper.find(".game-row").isVisible()).to.be.true;
  });
});
