/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { colors } from '../constants';
import { assertUnreachable, Dict, domUid, get, getOrNull } from '../utils';
import { Civilite, Elu, GroupePolitique } from '../domain/elu';
import {
    ChairNumber,
    GroupePolitiqueId,
    EluId,
    numberifyNominalNumber,
} from '../domain/nominal';
import { Association } from '../domain/hemicycle';
import { HemicycleConfigurationRendu } from '../domain/assemblee';

// const classes = {
//     assembleeCouloir1: css`
//         transform: rotate(30deg);
//         transform-origin: 50% 100%;
//     `,
//     assembleeCouloir2: css`
//         transform: rotate(-30deg);
//         transform-origin: 50% 100%;
//     `
// };

interface Props {
    eluById: Dict<EluId, Elu>;
    groupePolitiques: GroupePolitique[];
    groupePolitiqueById: Dict<GroupePolitiqueId, GroupePolitique>;
    associationByChair: Dict<ChairNumber, Association>;
    width: number;
    height: number;
    configurationRendu: HemicycleConfigurationRendu;
    selectedChairNumber: ChairNumber | undefined;
    updateSelectedChairNumber: (selectedChairNumber: ChairNumber) => void;
    hideAssociationsChairs: boolean;
    removeAssociation: (chair: ChairNumber) => void;
    deleteMode: boolean;
    printMode: boolean;
}

interface State {
    tooltipElu?: Elu;
}

const legendSquareWidth = 6;

const civilite = (c: Civilite | undefined) => {
    switch (c) {
        case 'MADAME':
            return 'Mme.';
        case 'MONSIEUR':
            return 'M.';
        case undefined:
            return '';
        default:
            return assertUnreachable(c);
    }
};

export default class Hemicycle extends React.PureComponent<Props, State> {
    state = {
        tooltipElu: undefined,
    } as State;

    private setEluInTooltip = (tooltipElu?: Elu) =>
        this.setState((state) => ({ ...state, tooltipElu }));

    public render() {
        const tooltipId = domUid();
        return (
            <React.Fragment>
                <svg
                    width={this.props.width}
                    height={this.props.height}
                    viewBox={`${this.props.configurationRendu.viewPortX} ${this.props.configurationRendu.viewPortY} ${this.props.configurationRendu.viewPortWidth} ${this.props.configurationRendu.viewPortHeight}`}
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <style>
                        {/*    .assembleeChair {*/}
                        {/*    fill: none;*/}
                        {/*}*/}

                        {/*    .assembleeChair:hover {*/}
                        {/*    fill: #4673bd;*/}
                        {/*    opacity: 0.4;*/}
                        {/*}*/}

                        {/*    .assembleeCouloir1 {*/}
                        {/*    transform: rotate(30deg);*/}
                        {/*    transform-origin: 50% 100%;*/}
                        {/*}*/}

                        {/*    .assembleeCouloir2 {*/}
                        {/*    transform: rotate(-30deg);*/}
                        {/*    transform-origin: 50% 100%;*/}
                        {/*}*/}
                    </style>
                    {/*<rect*/}
                    {/*    x={this.props.hemicycle.viewPortX}*/}
                    {/*    y={this.props.hemicycle.viewPortY}*/}
                    {/*    width={this.props.hemicycle.viewPortWidth}*/}
                    {/*    height={this.props.hemicycle.viewPortHeight}*/}
                    {/*    fill="none"*/}
                    {/*    stroke={colors.red}*/}
                    {/*/>*/}
                    {this.props.configurationRendu.chairs.map((chair) => {
                        const association = getOrNull(
                            this.props.associationByChair,
                            chair.number
                        );
                        const elu = association
                            ? get(this.props.eluById, association.eluId)
                            : undefined;
                        const groupePolitique = elu
                            ? get(
                                  this.props.groupePolitiqueById,
                                  elu.groupePolitiqueId
                              )
                            : undefined;
                        const strokeChairColor =
                            this.props.hideAssociationsChairs &&
                            this.props.selectedChairNumber !== chair.number &&
                            association
                                ? colors.grey
                                : colors.black;
                        const fillChairColor = this.props.printMode
                            ? colors.white
                            : this.props.selectedChairNumber === chair.number
                            ? colors.blue
                            : this.props.hideAssociationsChairs && association
                            ? colors.clearGrey
                            : groupePolitique
                            ? groupePolitique.couleur
                            : colors.white;
                        const fillChairOpacity = this.props.printMode
                            ? 1
                            : this.props.selectedChairNumber === chair.number ||
                              (this.props.hideAssociationsChairs &&
                                  association) ||
                              !groupePolitique
                            ? 1
                            : 0.15;
                        const opacity =
                            this.props.hideAssociationsChairs &&
                            this.props.selectedChairNumber !== chair.number &&
                            association
                                ? 0.15
                                : 1;
                        return (
                            <React.Fragment
                                key={numberifyNominalNumber(chair.number)}
                            >
                                <g
                                    data-tip
                                    data-for={tooltipId}
                                    css={css`
                                        opacity: ${opacity};
                                        &:hover {
                                            polygon {
                                                fill: ${colors.blue};
                                                fill-opacity: 1;
                                            }
                                        }
                                    `}
                                    onMouseOver={() =>
                                        this.setEluInTooltip(elu)
                                    }
                                    onClick={() => {
                                        if (!this.props.deleteMode) {
                                            this.props.updateSelectedChairNumber(
                                                chair.number
                                            );
                                        } else {
                                            this.props.removeAssociation(
                                                chair.number
                                            );
                                            this.setEluInTooltip(undefined);
                                        }
                                    }}
                                >
                                    {/*<circle*/}
                                    {/*    r={13}*/}
                                    {/*    cx={chair.chairBaseX}*/}
                                    {/*    cy={chair.chairBaseY}*/}
                                    {/*    css={css`*/}
                                    {/*        stroke: ${strokeChairColor};*/}
                                    {/*        stroke-width: 1px;*/}
                                    {/*        fill: ${fillChairColor};*/}
                                    {/*        cursor: pointer;*/}
                                    {/*    `}*/}
                                    {/*/>*/}
                                    <polygon
                                        points={`${chair.x1}, ${chair.y1}, ${chair.x2}, ${chair.y2}, ${chair.x3}, ${chair.y3}, ${chair.x4}, ${chair.y4}`}
                                        style={{
                                            stroke: strokeChairColor,
                                            strokeWidth: '0.5px',
                                            fill: fillChairColor,
                                            fillOpacity: fillChairOpacity,
                                            cursor: 'pointer',
                                        }}
                                    />
                                    {/*<line*/}
                                    {/*    css={css`*/}
                                    {/*        stroke: ${strokeChairColor};*/}
                                    {/*        stroke-width: 1px;*/}
                                    {/*    `}*/}
                                    {/*    x1={chair.x2}*/}
                                    {/*    y1={chair.y2}*/}
                                    {/*    x2={chair.x3}*/}
                                    {/*    y2={chair.y3}*/}
                                    {/*/>*/}
                                    {/*<line*/}
                                    {/*    css={css`*/}
                                    {/*        stroke: ${strokeChairColor};*/}
                                    {/*        stroke-width: 1px;*/}
                                    {/*    `}*/}
                                    {/*    x1={chair.x3}*/}
                                    {/*    y1={chair.y3}*/}
                                    {/*    x2={chair.x4}*/}
                                    {/*    y2={chair.y4}*/}
                                    {/*/>*/}
                                    {/*<line*/}
                                    {/*    css={css`*/}
                                    {/*        stroke: ${strokeChairColor};*/}
                                    {/*        stroke-width: 1px;*/}
                                    {/*    `}*/}
                                    {/*    x1={chair.x4}*/}
                                    {/*    y1={chair.y4}*/}
                                    {/*    x2={chair.x1}*/}
                                    {/*    y2={chair.y1}*/}
                                    {/*/>*/}
                                    {groupePolitique && (
                                        <line
                                            style={{
                                                stroke: groupePolitique.couleur,
                                                strokeWidth: '4px',
                                            }}
                                            x1={chair.x3}
                                            y1={chair.y3}
                                            x2={chair.x4}
                                            y2={chair.y4}
                                        />
                                    )}
                                    <text
                                        x={chair.centerX}
                                        y={chair.centerY}
                                        textLength={chair.x2 - chair.x1 + 'px'}
                                        style={{
                                            fill: strokeChairColor,
                                            textAnchor: 'middle',
                                            fontSize: '8px',
                                            cursor: 'pointer',
                                            alignmentBaseline: 'central',
                                            display: 'none',
                                        }}
                                    >
                                        <tspan x={chair.baseX1 + 1} dy="1em">
                                            {elu?.prenom}
                                        </tspan>
                                        <tspan x={chair.baseX1 + 1} dy="1em">
                                            {elu?.nom}
                                        </tspan>
                                    </text>
                                    <foreignObject
                                        x={chair.baseX1 + 1}
                                        y={chair.y1}
                                        width={20}
                                        height={26}
                                        style={{
                                            fontSize: '3.8px',
                                            wordBreak: 'break-word',
                                        }}
                                    >
                                        {React.createElement('div', {
                                            xmlns:
                                                'http://www.w3.org/1999/xhtml',
                                            children: (
                                                <React.Fragment>
                                                    {chair.number}{' '}
                                                    {groupePolitique?.nomCourt}
                                                    <br />
                                                    {elu?.shortFonction && (
                                                        <React.Fragment>
                                                            <b>
                                                                {
                                                                    elu?.shortFonction
                                                                }
                                                            </b>{' '}
                                                        </React.Fragment>
                                                    )}
                                                    {civilite(elu?.civilite)}
                                                    <br />
                                                    {elu?.nom}
                                                </React.Fragment>
                                            ),
                                        })}
                                    </foreignObject>
                                </g>
                                {/*<line*/}
                                {/*    x1={chair.baseX1}*/}
                                {/*    y1={chair.baseY1}*/}
                                {/*    x2={chair.baseX2}*/}
                                {/*    y2={chair.baseY2}*/}
                                {/*    stroke="green"*/}
                                {/*/>*/}
                                {/*<line*/}
                                {/*    x1={500}*/}
                                {/*    y1={300}*/}
                                {/*    x2={chair.chairBaseX}*/}
                                {/*    y2={chair.chairBaseY}*/}
                                {/*    stroke="yellow"*/}
                                {/*/>*/}
                                {/*<circle*/}
                                {/*    cx={chair.baseX1}*/}
                                {/*    cy={chair.baseY1}*/}
                                {/*    r="2"*/}
                                {/*    fill="blue"*/}
                                {/*/>*/}
                                {/*<circle*/}
                                {/*    cx={chair.centerX}*/}
                                {/*    cy={chair.centerY}*/}
                                {/*    r="2"*/}
                                {/*    fill="red"*/}
                                {/*/>*/}
                            </React.Fragment>
                        );
                    })}
                    {/*<ellipse rx="240"*/}
                    {/*         ry="160"*/}
                    {/*         cx="500"*/}
                    {/*         cy="600"*/}
                    {/*         fill="none"*/}
                    {/*         stroke="#4673bd"*/}
                    {/*         opacity="0.4"*/}
                    {/*/>*/}
                    {/*<g>*/}
                    {/*    <rect x="480"*/}
                    {/*          y="0"*/}
                    {/*          width="40"*/}
                    {/*          height="600"*/}
                    {/*          className="assembleeCouloir1"*/}
                    {/*          fill="none"*/}
                    {/*          stroke="#4673bd"*/}
                    {/*          opacity="0.4"*/}
                    {/*    />*/}
                    {/*    <rect x="480"*/}
                    {/*          y="0"*/}
                    {/*          width="40"*/}
                    {/*          height="600"*/}
                    {/*          className="assembleeCouloir2"*/}
                    {/*          fill="none"*/}
                    {/*          stroke="#4673bd"*/}
                    {/*          opacity="0.4"*/}
                    {/*    />*/}
                    {/*</g>*/}
                    <g
                        transform={`translate(${
                            this.props.configurationRendu.viewPortX +
                            legendSquareWidth
                            // +
                            //     this.props.hemicycle.viewPortWidth -
                            //     100
                        },
                        ${
                            this.props.configurationRendu.viewPortY +
                            this.props.configurationRendu.viewPortHeight -
                            62
                        })`}
                    >
                        {this.props.groupePolitiques.map((gp, index) => (
                            <React.Fragment key={numberifyNominalNumber(gp.id)}>
                                <rect
                                    x={0}
                                    y={index * legendSquareWidth}
                                    width={legendSquareWidth}
                                    height={legendSquareWidth}
                                    style={{
                                        fill: gp.couleur,
                                    }}
                                />
                                <text
                                    x={legendSquareWidth * 1.5}
                                    y={(index + 1) * legendSquareWidth}
                                    style={{
                                        fontSize: '6px',
                                    }}
                                >
                                    {gp.nom}
                                </text>
                            </React.Fragment>
                        ))}
                    </g>
                </svg>
            </React.Fragment>
        );
    }
}
