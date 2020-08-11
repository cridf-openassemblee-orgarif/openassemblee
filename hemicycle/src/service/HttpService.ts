import { productionBuild } from '../constants';
import { Errors } from '../component/util/errors';

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

const getCookie = (cookieName: String) => {
    const value = '; ' + document.cookie;
    const parts = value.split('; ' + cookieName + '=');
    if (parts.length === 2) {
        const firstPart = parts.pop();
        if (!firstPart) {
            throw Errors._e32ccca2();
        }
        return firstPart.split(';').shift();
    } else {
        return undefined;
    }
};

export default class HttpService {
    private credentials: RequestCredentials = productionBuild
        ? 'include'
        : 'same-origin';

    private csrfToken: string | undefined;

    public constructor() {
        this.csrfToken = getCookie('CSRF-TOKEN');
    }

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
        const headers: HeadersInit = {
            Accept: 'application/json',
            'Content-Type': 'application/json',
        };
        if (this.csrfToken) {
            headers['X-CSRF-TOKEN'] = this.csrfToken;
        }
        const params: RequestInit = {
            method: requestType,
            headers,
            credentials: this.credentials,
        };
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
                return response.text().then((t) => ({
                    status: response.status,
                    body: JSON.parse(t),
                }));
            }
        );
    }
}
