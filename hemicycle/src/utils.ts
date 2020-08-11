import { NominalNumber, NominalString } from './domain/nominal';
import { Elu } from './domain/elu';

let uniqueIdIndex = 0;
export const domUid = () => 'WivaldyUid_' + uniqueIdIndex++;

export const eluToString = (elu: Elu) => elu.prenom + ' ' + elu.nom;

export function assertUnreachable(x: never): never {
    throw new Error(
        'Wivaldy (theoretically) unreachable code with value : "' +
            JSON.stringify(x) +
            '"'
    );
}

export type Dict<K extends NominalNumber<any> | NominalString<any>, T> = Record<
    // @ts-ignore
    K,
    T | undefined
>;

export const getMaybe = <K extends NominalNumber<any> | NominalString<any>, T>(
    dict: Dict<K, T>,
    key: K
): T | undefined => dict[key];

export const get = <K extends NominalNumber<any> | NominalString<any>, T>(
    dict: Dict<K, T>,
    key: K
): T => {
    const r = getMaybe(dict, key);
    if (!r) {
        throw new Error(`Could not find item ${key}`);
    }
    return r;
};

export const set = <K extends NominalNumber<any> | NominalString<any>, T>(
    dict: Dict<K, T>,
    key: K,
    value: T
): T | undefined => (dict[key] = value);
