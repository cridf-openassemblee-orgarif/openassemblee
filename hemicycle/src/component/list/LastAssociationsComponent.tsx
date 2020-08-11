/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { SelectedEluSource } from '../App';
import EluComponent from './EluComponent';
import AnimatedHeightContainer from '../util/AnimatedHeightContainer';
import { colors } from '../../constants';
import {
    ChairNumber,
    EluId,
    numberifyNominalNumber
} from '../../domain/nominal';
import { Association } from '../../domain/hemicycle';
import { Dict, get } from '../../utils';
import { Elu } from '../../domain/elu';

interface Props {
    eluById: Dict<EluId, Elu>;
    selectedEluId?: EluId;
    updateSelectedEluId: (
        selectedEluId: EluId | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: ChairNumber) => void;
    associations: Association[];
    deleteMode: boolean;
}

interface State {
    isOpen: boolean;
}

export default class LastAssociationsComponent extends React.Component<
    Props,
    State
> {
    public state: State = {
        isOpen: true
    };

    private switchOpen = () =>
        this.setState(state => ({
            ...state,
            isOpen: !state.isOpen
        }));

    render() {
        return (
            <div
                css={css`
                    border: 3px solid ${colors.grey};
                `}
            >
                <div
                    css={css`
                        position: relative;
                        background: ${colors.grey};
                        color: ${colors.white};
                        font-weight: bold;
                        font-size: 14px;
                        height: 24px;
                    `}
                >
                    <div
                        css={css`
                            padding-left: 4px;
                        `}
                    >
                        Dernières associations
                    </div>
                    <div
                        css={css`
                            position: absolute;
                            top: 3px;
                            right: 6px;
                            background: ${colors.clearGrey};
                            color: ${colors.grey};
                            height: 14px;
                            width: 14px;
                            cursor: pointer;
                            text-align: center;
                        `}
                        onClick={this.switchOpen}
                    >
                        <div
                            css={css`
                                position: relative;
                                top: ${this.state.isOpen ? -2 : -4}px;
                            `}
                        >
                            {this.state.isOpen ? '-' : '+'}
                        </div>
                    </div>
                </div>
                <AnimatedHeightContainer isOpen={this.state.isOpen}>
                    <div
                        css={css`
                            padding: 10px;
                            background: ${colors.white};
                        `}
                    >
                        {/* sans duplicat le reverse modifie l'original... */}
                        {[...this.props.associations]
                            .reverse()
                            .splice(0, 5)
                            .map(a => {
                                const elu = get(this.props.eluById, a.eluId);
                                return (
                                    <EluComponent
                                        key={numberifyNominalNumber(elu.id)}
                                        elu={elu}
                                        chairNumber={a.chairNumber}
                                        isSelected={
                                            this.props.selectedEluId === elu.id
                                        }
                                        deleteMode={this.props.deleteMode}
                                        updateSelectedEluId={
                                            this.props.updateSelectedEluId
                                        }
                                        removeAssociation={
                                            this.props.removeAssociation
                                        }
                                    />
                                );
                            })}
                    </div>
                </AnimatedHeightContainer>
            </div>
        );
    }
}
