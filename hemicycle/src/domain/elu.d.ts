type Civilite = 'MONSIEUR' | 'MADAME';

type LocalDate = string;

interface Elu {
    id: number;
    civilite: Civilite;
    nom: string;
    prenom: string;
    groupePolitiqueId: number;
}

interface GroupePolitique {
    id: number;
    nom: string;
    nomCourt: string;
    couleur: string;
}

interface EluFromWs {
    id: number;
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

interface GroupePolitiqueFromWs {
    id: number;
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

interface EluListDTO {
    elu: EluFromWs;
    groupePolitique?: GroupePolitiqueFromWs;
}
