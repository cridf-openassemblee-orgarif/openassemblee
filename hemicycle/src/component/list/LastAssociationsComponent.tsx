/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { Associations, SelectedEluSource } from '../App';
import EluComponent from './EluComponent';
import AnimatedHeightContainer from '../util/AnimatedHeightContainer';
import { colors } from '../../constants';

interface Props {
    selectedElu?: Elu;
    updateSelectedElu: (
        selectedElu: Elu | undefined,
        source: SelectedEluSource
    ) => void;
    removeAssociation: (chair: number) => void;
    associations: Associations;
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
                            top: 0;
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
                                top: -3px;
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
                        {[...this.props.associations.list]
                            .reverse()
                            .splice(0, 5)
                            .map(a => (
                                <EluComponent
                                    key={a.elu.id}
                                    elu={a.elu}
                                    association={a}
                                    isSelected={
                                        this.props.selectedElu?.id === a.elu.id
                                    }
                                    deleteMode={this.props.deleteMode}
                                    updateSelectedElu={
                                        this.props.updateSelectedElu
                                    }
                                    removeAssociation={
                                        this.props.removeAssociation
                                    }
                                />
                            ))}
                    </div>
                </AnimatedHeightContainer>
            </div>
        );
    }
}
