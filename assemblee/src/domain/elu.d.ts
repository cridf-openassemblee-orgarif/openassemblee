type Civilite = 'MONSIEUR' | 'MADAME';

type LocalDate = string;

interface Elu {
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
    appartenancesGroupePolitique: AppartenanceGroupePolitique[];
}

interface GroupePolitique {
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

interface AppartenanceGroupePolitique {
    id: number;
    dateDebut: LocalDate;
    dateFin: LocalDate;
    motifFin: string;
    importUid: string;
    elu: Elu;
    groupePolitique: GroupePolitique;
}

interface EluListDTO {
    elu: Elu;
    groupePolitique: GroupePolitique;
}