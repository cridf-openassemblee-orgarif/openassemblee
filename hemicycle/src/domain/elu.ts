import { EluId, GroupePolitiqueId } from './nominal';

export type Civilite = 'MONSIEUR' | 'MADAME';

export interface Elu {
    id: EluId;
    civilite: Civilite;
    nom: string;
    prenom: string;
    groupePolitiqueId: GroupePolitiqueId;
    shortFonction?: string;
}

export interface GroupePolitique {
    id: GroupePolitiqueId;
    nom: string;
    nomCourt: string;
    couleur: string;
}
