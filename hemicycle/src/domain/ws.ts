import {
    EluId,
    GroupePolitiqueId,
    LocalDate,
    MandatId,
    PlanId,
} from './nominal';
import { Civilite, Elu, GroupePolitique } from './elu';
import { Association } from './hemicycle';
import { HemicycleConfigurationRendu } from './assemblee';

export interface EluFromWs {
    id: EluId;
    civilite: Civilite;
    nom: string;
    prenom: string;
    nomJeuneFille: string;
    profession: string;
    dateNaissance: LocalDate;
    lieuNaissance: string;
    image: number;
    importUid: string;
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
    actifInCurrentMandat: boolean;
}

export interface HemicyclePlanUpdateDTO {
    id: PlanId;
    associations: Association[];
}

export interface HemicycleArchiveCreationDTO {
    planId: PlanId;
    data: HemicycleArchiveDataDTO;
    svgPlan: string;
}

export interface HemicycleArchiveDataDTO {
    associations: Association[];
    elus: Elu[];
    groupePolitiques: GroupePolitique[];
}

export interface HemicycleArchiveDataWithConfigurationDTO {
    data: HemicycleArchiveDataDTO;
    rendu: HemicycleConfigurationRendu;
}
