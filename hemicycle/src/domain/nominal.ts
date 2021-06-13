export abstract class NominalString<T extends string> {
    private _typeGuard!: T;
}

export abstract class NominalNumber<T extends string> {
    private _typeGuard!: T;
}

export const stringifyNominalString = (value: NominalString<any>) =>
    (value as unknown) as string;

export const numberifyNominalNumber = (value: NominalNumber<any>) =>
    (value as unknown) as number;

export const instanciateNominalString = <T extends NominalString<any>>(
    value: string
) => (value as unknown) as T;

export const instanciateNominalNumber = <T extends NominalNumber<any>>(
    value: number
) => (value as unknown) as T;

export type ArchiveId = NominalNumber<'ArchiveId'>;
export type EluId = NominalNumber<'EluId'>;
export type GroupePolitiqueId = NominalNumber<'GroupePolitiqueId'>;
export type MandatId = NominalNumber<'MandatId'>;
export type PlanId = NominalNumber<'PlanId'>;

export type ChairNumber = NominalNumber<'ChairNumber'>;

export type LocalDate = NominalString<'LocalDate'>;
