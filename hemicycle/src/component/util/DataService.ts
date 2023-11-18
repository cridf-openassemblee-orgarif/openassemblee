import { Elu, GroupePolitique } from '../../domain/elu';
import { Dict, get, set } from '../../utils';
import {
    ChairNumber,
    EluId,
    GroupePolitiqueId,
    instanciateNominalNumber,
    PlanId,
} from '../../domain/nominal';
import { injector } from '../../service/injector';
import { urls } from '../../constants';
import { EluListDTO, GroupePolitiqueFromWs } from '../../domain/ws';
import { Association, HemicyclePlanAssociations } from '../../domain/hemicycle';

const alphabeticSort = (map: (item: any) => string) => (
    first: any,
    second: any
) => {
    const a = map(first);
    const b = map(second);
    if (a > b) {
        return 1;
    }
    if (b > a) {
        return -1;
    }
    return 0;
};

const convertElu = (dto: EluListDTO): Elu => ({
    id: dto.elu.id,
    civilite: dto.elu.civilite,
    nom: dto.elu.nom,
    prenom: dto.elu.prenom,
    groupePolitiqueId: dto.groupePolitique ? dto.groupePolitique.id : undefined,
    shortFonction: dto.shortFonction,
    actif: dto.actifInCurrentMandat,
});

const convertGroupePolitique = (
    gp: GroupePolitiqueFromWs
): GroupePolitique => ({
    id: gp.id,
    nom: gp.nom,
    nomCourt: gp.nomCourt,
    couleur: gp.couleur ? '#' + gp.couleur : '#000',
});

export interface RawElus {
    elus: Elu[];
    groupePolitiques: GroupePolitique[];
}

export class DataService {
    private elusDtoToGroupePolitiques = (
        eluDtos: EluListDTO[]
    ): GroupePolitique[] => {
        const gps: GroupePolitique[] = [];
        eluDtos
            .filter((d) => d.groupePolitique)
            .forEach((d) => {
                if (
                    gps.filter((gp) => gp.id === d.groupePolitique?.id)
                        .length === 0
                ) {
                    gps.push(convertGroupePolitique(d.groupePolitique!));
                }
            });
        // tri par ordre alphabétique
        return gps.sort(alphabeticSort((gp: GroupePolitique) => gp.nom));
    };

    fetchData = (
        planId: PlanId
    ): Promise<{
        rawElus: RawElus;
        hemicycle: HemicyclePlanAssociations;
    }> => {
        const data = injector()
            .httpService.get(injector().urlBase + urls.elus)
            .then((a) => {
                const eluDtos = a.body as EluListDTO[];
                const data: RawElus = {
                    elus: eluDtos.map((d) => convertElu(d)),
                    groupePolitiques: this.elusDtoToGroupePolitiques(eluDtos),
                };
                return data;
            });
        const associations = injector()
            .httpService.get(
                injector().urlBase +
                    urls.hemicyclePlansAssociations +
                    '/' +
                    planId
            )
            .then((a) => a.body as HemicyclePlanAssociations);
        return Promise.all([data, associations]).then((d: any) => {
            const rawElus: RawElus = d[0];
            const hemicycle: HemicyclePlanAssociations = d[1];
            return {
                rawElus,
                hemicycle,
            };
        });
    };

    rawElusMaps = (rawElus: RawElus) => {
        const eluById: Dict<EluId, Elu> = {};
        const elusByGroupeId: Dict<GroupePolitiqueId, Elu[]> = {};
        const elusSansGroupe: Elu[] = [];
        const elusDemissionaires: Elu[] = [];
        const groupePolitiqueById: Dict<
            GroupePolitiqueId,
            GroupePolitique
        > = {};
        rawElus.elus.forEach((e) => {
            set(eluById, e.id, e);
        });
        rawElus.groupePolitiques.forEach((gp) => {
            set(elusByGroupeId, gp.id, []);
            set(groupePolitiqueById, gp.id, gp);
        });
        // FIXMENOW filtrer les demissionaires ? ou et comment
        rawElus.elus.forEach((e) => {
            if (!e.actif) {
                elusDemissionaires.push(e);
            } else if (!e.groupePolitiqueId) {
                elusSansGroupe.push(e);
            } else {
                get(elusByGroupeId, e.groupePolitiqueId).push(e);
            }
        });
        // elus dans ordre alphabétique
        Object.keys(elusByGroupeId).forEach((idAsString: string) => {
            const gpId = instanciateNominalNumber<GroupePolitiqueId>(
                parseInt(idAsString)
            );
            set(
                elusByGroupeId,
                gpId,
                get(elusByGroupeId, gpId).sort(
                    alphabeticSort((elu: Elu) => elu.nom)
                )
            );
        });
        return {
            eluById,
            elusByGroupeId,
            groupePolitiqueById,
            elusDemissionaires,
            elusSansGroupe,
        };
    };

    associationMaps = (associations: Association[]) => {
        const associationByChair: Dict<ChairNumber, Association> = {};
        const associationByEluId: Dict<EluId, Association> = {};
        associations.forEach((a) => {
            set(associationByChair, a.chairNumber, a);
            set(associationByEluId, a.eluId, a);
        });
        return { associationByChair, associationByEluId };
    };
}
