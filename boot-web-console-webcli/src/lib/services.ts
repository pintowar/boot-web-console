type EngineEvalArgs = {
    engine: string
    data: FormData
    onSuccess: (arg: any) => void
    onFinally: () => void
}

export const engineEval = ({engine, data, onSuccess, onFinally}: EngineEvalArgs) => {
    return fetch(`/console/${engine}`, { method: "POST", body: data })
        .then(response => response.json())
        .then(onSuccess)
        .finally(onFinally);
}

export const listEngines = (onSuccess: (arg: string[]) => void) => {
    return fetch(`/console/engines`)
        .then(response => response.json())
        .then(onSuccess);
}