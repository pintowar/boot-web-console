import { assert, describe, expect, it, vi } from 'vitest'
import { listEngines, sampleSelect } from '../services';

global.fetch = vi.fn();

function createJsonResponse(data) {
    return { json: () => new Promise((resolve) => resolve(data)) }
}

function createTextResponse(data) {
    return { text: () => new Promise((resolve) => resolve(data)) }
}

describe('services test', () => {
    it('should succesfully list engines', async () => {
        const expectedResult = ['groovy', 'jruby'];
        fetch.mockResolvedValue(createJsonResponse(expectedResult));

        const engines = await listEngines();

        expect(fetch).toHaveBeenCalledWith('/console/engines');
        assert.equal(engines, expectedResult);
    });

    it('should succesfully get sample', async () => {
        const expectedResult = 'some script content';
        const sampleEndpoint = '/some-sample.script'
        fetch.mockResolvedValue(createTextResponse(expectedResult));

        const sample = await sampleSelect(sampleEndpoint);

        expect(fetch).toHaveBeenCalledWith(sampleEndpoint);
        assert.equal(sample, expectedResult);
    });

    it.todo('should succesfully eval engine')
});