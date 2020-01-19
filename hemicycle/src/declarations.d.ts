declare namespace NodeJS {
    export interface Global {
        urlBase: string | undefined;
        devHost: boolean | undefined;
        loadHemicycle: () => void;
    }
}
