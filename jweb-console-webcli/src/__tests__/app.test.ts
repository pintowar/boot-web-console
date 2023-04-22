import { render } from "@testing-library/svelte";
import App from "../App.svelte";

describe("App.svelte", () => {
  it("should render initial state", () => {
    const { container } = render(App);
    expect(container).toBeTruthy();
  });
});
