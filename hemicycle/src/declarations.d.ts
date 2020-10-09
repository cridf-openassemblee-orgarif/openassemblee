declare namespace NodeJS {
    export interface Global {
        urlBase: string | undefined;
        devHost: boolean | undefined;
        loadHemicycle: (props: { planId: any; isProjet: boolean }) => void;
        loadHemicycleArchive: (props: { archiveId: any }) => void;
        activateDebug: () => void;
    }
}
