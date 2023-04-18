import type { ScriptResult } from "../interfaces";
import { engineEval, listEngines, sampleSelect } from "../services";

function setupMockFetch(data: any, status = 200, contentBody = "json") {
  const fetchMock = vi.fn().mockResolvedValue({
    ok: status < 400,
    [contentBody]: () => new Promise((resolve) => resolve(data)),
  });
  global.fetch = fetchMock;

  return fetchMock;
}

const fetchCopy = global.fetch;
const localStorageCopy = global.window.localStorage;

describe("services suite tests", () => {
  afterAll(() => {
    global.fetch = fetchCopy;
    global.window.localStorage = localStorageCopy;
  });

  it("should succesfully eval engine", async () => {
    const engine = "groovy";
    const script = 'prinltn("test"); 1 + 3';
    const expectedResult = {
      result: "4",
      stdout: ["test"],
      stderr: [],
    } as ScriptResult;

    const fetchMock = setupMockFetch(expectedResult);

    const result = await engineEval(engine, script);

    const form = new FormData();
    form.append("script", script);
    expect(fetchMock).toHaveBeenCalledWith(`/console/${engine}/eval`, {
      method: "POST",
      body: form,
    });
    assert.deepEqual(result, expectedResult);
  });

  it("should succesfully list engines", async () => {
    const expectedResult = ["groovy", "jruby"];
    const fetchMock = setupMockFetch(expectedResult);

    const engines = await listEngines();

    expect(fetchMock).toHaveBeenCalledWith("/console/engines");
    assert.equal(engines, expectedResult);
  });

  it("should succesfully get sample", async () => {
    const expectedResult = "some script content";
    const sampleEndpoint = "/some-sample.script";
    const fetchMock = setupMockFetch(expectedResult, 200, "text");

    const sample = await sampleSelect(sampleEndpoint);

    expect(fetchMock).toHaveBeenCalledWith(sampleEndpoint);
    assert.equal(sample, expectedResult);
  });
});
