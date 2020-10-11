declare namespace NodeJS {
    export interface Global {
        urlBase: string | undefined;
        devHost: boolean | undefined;
        loadHemicycle: (props: { planId: any; isProjet: boolean }) => void;
        loadHemicycleArchive: (props: { archiveId: any }) => void;
        saveArchive: (props: { planId: any; then: () => void }) => void;
        activateDebug: () => void;
    }
}
