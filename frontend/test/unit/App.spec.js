import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import App from "@/App.vue";

describe("App", () => {
  it("renders", () => {
    const wrapper = shallow(App, {});
    expect(wrapper.contains("#app")).to.be.true;
  });
});
