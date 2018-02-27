import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import MainView from "@/views/Main.vue";


describe("MainView", () => {
  it("renders a main-view element", () => {
    const wrapper = shallow(MainView, {
      stubs: {
        'router-link': {
          render: h => h("a")
        }
      }
    });
    expect(wrapper.contains("#main-view")).to.be.true;
  });
});
