import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import MainView from "@/views/Main.vue";

describe("MainView", () => {
  it("renders a create-game element", () => {
    const wrapper = shallow(MainView, {});
    expect(wrapper.contains("a#create-game")).to.be.true;
  });
  it("makes a join link and status appear after waiting for opponent", () => {
    const wrapper = shallow(MainView, {});
    expect(wrapper.contains("#join-link")).to.be.false;
    expect(wrapper.contains("#game-status")).to.be.false;

    wrapper.setData({ link: "potato", state: "waiting" });

    expect(wrapper.contains("#join-link")).to.be.true;
    expect(wrapper.find("#game-status").text()).to.equal(
      "Waiting for opponent"
    );
  });
  it("removes the create-game element when while waiting for opponent", () => {
    const wrapper = shallow(MainView, {});
    wrapper.setData({ link: "potato", state: "waiting" });
    expect(wrapper.contains("a#create-game")).to.be.false;
  });
});
