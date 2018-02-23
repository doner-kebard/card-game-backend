import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import App from "@/App.vue";
import MainView from "@/views/Main.vue";

describe("App", () => {
  it("renders a main-view component", () => {
    const wrapper = shallow(App, {});
    expect(wrapper.contains(MainView)).to.be.true;
  });
});
