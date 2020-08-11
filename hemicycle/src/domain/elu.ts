import { EluId, GroupePolitiqueId, LocalDate } from './nominal';

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

export interface EluFromWs {
    id: EluId;
    civilite: Civilite;
    nom: string;
    prenom: string;
    nomJeuneFille: string;
    profession: string;
    dateNaissance: LocalDate;
    lieuNaissance: string;
    codeDepartement: string;
    departement: string;
    image: number;
    motifDemission: string;
    dateDemission: LocalDate;
    importUid: string;
    listeElectorale: string;
    listeCourt: string;
    uid: string;
    shortUid: number;
}

export interface GroupePolitiqueFromWs {
    id: GroupePolitiqueId;
    nom: string;
    nomCourt: string;
    adressePostale: any;
    dateDebut: LocalDate;
    dateFin: LocalDate;
    motifFin: string;
    image: number;
    website: string;
    phone: string;
    mail: string;
    fax: string;
    importUid: string;
    couleur: string;
    uid: string;
    shortUid: number;
}

export interface EluListDTO {
    elu: EluFromWs;
    groupePolitique?: GroupePolitiqueFromWs;
    shortFonction?: string;
}
