import { ChairNumber, EluId } from './nominal';
import { HemicycleConfigurationRendu } from './assemblee';

export interface HemicyclePlanAssociations {
    associations: Association[];
    configurationRendu: HemicycleConfigurationRendu;
}

export interface Association {
    chairNumber: ChairNumber;
    eluId: EluId;
}
