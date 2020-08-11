import { ChairNumber } from './nominal';

export interface ChairRendu {
    number: ChairNumber;
    baseX1: number;
    baseY1: number;
    baseX2: number;
    baseY2: number;
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    x3: number;
    y3: number;
    x4: number;
    y4: number;
    // attention à son utilisation, bien pour positionner les sièges, pas forcément pr autre chose
    centerAngle: number;
    chairBaseX: number;
    chairBaseY: number;
    centerX: number;
    centerY: number;
}

export interface HemicycleConfigurationRendu {
    chairs: ChairRendu[];
    viewPortX: number;
    viewPortY: number;
    viewPortWidth: number;
    viewPortHeight: number;
    minChairNumber: ChairNumber;
    maxChairNumber: ChairNumber;
}
