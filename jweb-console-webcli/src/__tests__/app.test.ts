import { render, screen } from "@testing-library/svelte";
import userEvent from "@testing-library/user-event";
import App from "../App.svelte";

describe("App.svelte", () => {
  it("should render initial state", () => {
    const { container } = render(App);
    expect(container).toBeTruthy();
  });
});
