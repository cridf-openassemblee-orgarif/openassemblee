/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { colors } from '../constants';
import ReactTooltip from 'react-tooltip';
import { domUid } from '../utils';

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
    hemicycle: HemicycleDTO;
    width: number;
    height: number;
    selectedChairNumber?: number;
    updateChairNumber: (chairNumber: number) => void;
    associations: Dict<number, EluListDTO>;
}

interface State {
    tooltipElu?: EluListDTO;
}

export default class Hemicycle extends React.PureComponent<Props, State> {
    state = {
        tooltipElu: undefined
    } as State;

    private setEluInTooltip = (tooltipElu?: EluListDTO) =>
        this.setState(state => ({ ...state, tooltipElu }));

    public render() {
        const tooltipId = domUid();
        const tooltipElu = this.state.tooltipElu;
        return (
            <React.Fragment>
                <ReactTooltip
                    id={tooltipId}
                    aria-haspopup="true"
                    role="elu"
                    disable={!tooltipElu}
                >
                    <p>
                        {tooltipElu
                            ? `${tooltipElu.elu.prenom} ${tooltipElu.elu.nom}`
                            : undefined}
                    </p>
                </ReactTooltip>
                {/*viewbox avec 20 / 30 en size de chair : viewBox="20 60 960 510"*/}
                {/*FIXMENOW calculer auto !*/}
                <svg
                    width={this.props.width}
                    height={this.props.height}
                    viewBox="46 56 910 500"
                >
                    {/*<svg width="1000" height="600" viewBox="0 0 20 600" >*/}
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
                    {/*    x="20"*/}
                    {/*    y="60"*/}
                    {/*    width="960"*/}
                    {/*    height="510"*/}
                    {/*    fill="white"*/}
                    {/*    // stroke="#bd5e46"*/}
                    {/*/>*/}
                    {this.props.hemicycle.chairs.map(chair => {
                        const association = this.props.associations[
                            chair.number
                        ];
                        return (
                            <React.Fragment key={chair.number}>
                                <g
                                    data-tip
                                    data-for={tooltipId}
                                    css={css`
                                        &:hover {
                                            polygon,
                                            circle {
                                                fill: ${colors.blue};
                                            }
                                        }
                                    `}
                                    onMouseOver={() =>
                                        this.setEluInTooltip(association)
                                    }
                                    onClick={() => {
                                        this.props.updateChairNumber(
                                            chair.number
                                        );
                                    }}
                                >
                                    <circle
                                        r={13}
                                        cx={chair.chairBaseX}
                                        cy={chair.chairBaseY}
                                        css={css`
                                            stroke: black;
                                            stroke-width: 1px;
                                            fill: ${this.props
                                                .selectedChairNumber ===
                                            chair.number
                                                ? colors.green
                                                : colors.white};
                                            cursor: pointer;
                                            //&:hover {
                                            //    fill: #4673bd;
                                            //    opacity: 0.4;
                                            //}
                                        `}
                                    />
                                    <polygon
                                        points={`${chair.x1}, ${chair.y1}, ${chair.x2}, ${chair.y2}, ${chair.x3}, ${chair.y3}, ${chair.x4}, ${chair.y4}`}
                                        css={css`
                                            stroke: none;
                                            fill: ${this.props
                                                .selectedChairNumber ===
                                            chair.number
                                                ? colors.green
                                                : colors.white};
                                            cursor: pointer;
                                            //&:hover {
                                            //    fill: #4673bd;
                                            //    opacity: 0.4;
                                            //}
                                        `}
                                        // ng-mouseover="test()"
                                    />
                                    <line
                                        css={css`
                                            stroke: black;
                                            stroke-width: 1px;
                                        `}
                                        x1={chair.x2}
                                        y1={chair.y2}
                                        x2={chair.x3}
                                        y2={chair.y3}
                                    />
                                    <line
                                        css={css`
                                            stroke: black;
                                            stroke-width: 1px;
                                        `}
                                        x1={chair.x3}
                                        y1={chair.y3}
                                        x2={chair.x4}
                                        y2={chair.y4}
                                    />
                                    <line
                                        css={css`
                                            stroke: black;
                                            stroke-width: 1px;
                                        `}
                                        x1={chair.x4}
                                        y1={chair.y4}
                                        x2={chair.x1}
                                        y2={chair.y1}
                                    />

                                    {(() => {
                                        if (
                                            association &&
                                            association.groupePolitique
                                        ) {
                                            return (
                                                <line
                                                    css={css`
                                                        stroke: #${association.groupePolitique.couleur};
                                                        stroke-width: 4px;
                                                    `}
                                                    x1={chair.x3}
                                                    y1={chair.y3}
                                                    x2={chair.x4}
                                                    y2={chair.y4}
                                                />
                                            );
                                        }
                                    })()}

                                    <text
                                        x={chair.centerX}
                                        y={chair.centerY}
                                        css={css`
                                            text-anchor: middle;
                                            font-size: 10px;
                                            cursor: pointer;
                                            alignment-baseline: central;
                                        `}
                                    >
                                        {chair.number}
                                    </text>
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
                                {/*    cx="{chair.baseX1}"*/}
                                {/*    cy="{chair.baseY1}"*/}
                                {/*    r="2"*/}
                                {/*    fill="blue"*/}
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
                </svg>
            </React.Fragment>
        );
    }
}
