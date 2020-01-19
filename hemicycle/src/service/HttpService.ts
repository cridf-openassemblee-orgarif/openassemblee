import { injector } from './injector';

type RequestType = 'GET' | 'POST';

export interface SecurityCsrfToken {
    readonly header: string;
    readonly inputName: string;
    token?: string;
}

export interface HttpResponse {
    status: number;
    body?: any;
}

export default class HttpService {
    public credentials: RequestCredentials = 'same-origin';

    public get = (url: string, getParams?: any) =>
        this.fetchAndDeserialize('GET', url, getParams);

    public post = (url: string, body?: any) =>
        this.fetchAndDeserialize('POST', url, null, body);

    public fetch(
        requestType: RequestType,
        url: string,
        getParams?: any,
        body?: any
    ): Promise<Response> {
        const params: RequestInit = {
            method: requestType,
            headers: {
                Accept: 'application/json',
                'Content-Type': 'application/json'
            },
            credentials: this.credentials
        };
        const csrfToken = injector().securityCsrfToken.token;
        if (csrfToken) {
            if (!params.headers) {
                throw new Error();
            }
            // @ts-ignore
            params.headers[injector().securityCsrfToken.header] = csrfToken;
        }
        if (body) {
            params.body = JSON.stringify(body);
        }
        let finalUrl = url;
        if (getParams) {
            finalUrl += '?' + encodeURIComponent(JSON.stringify(getParams));
        }
        return fetch(finalUrl, params);
    }

    private fetchAndDeserialize(
        requestType: RequestType,
        url: string,
        getParams?: any,
        body?: any
    ): Promise<HttpResponse> {
        return this.fetch(requestType, url, getParams, body).then(
            (response: Response) => {
                if (!response.ok) {
                    throw response.body;
                }
                return response.text().then(t => ({
                    status: response.status,
                    body: JSON.parse(t)
                }));
            }
        );
    }
}
