declare namespace NodeJS {
    export interface Global {
        urlBase: string | undefined;
        devHost: boolean | undefined;
        loadHemicycle: (props: { planId: number; isProjet: boolean }) => void;
        activateDebug: () => void;
    }
}
