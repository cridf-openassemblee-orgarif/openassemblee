/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';

interface Props {
    isOpen: boolean;
}

interface State {
    subContainerHeight?: number;
}

export default class AnimatedHeightContainer extends React.PureComponent<
    Props,
    State
> {
    public state: State = {
        subContainerHeight: undefined,
    };

    public setSubContainerHeight = (subContainerHeight: number | undefined) =>
        this.setState((state) => ({
            ...state,
            subContainerHeight,
        }));

    public render() {
        return (
            <div
                css={css`
                    height: ${this.props.isOpen
                        ? this.state.subContainerHeight + 'px'
                        : 0};
                    overflow: hidden;
                    transition: height 0.4s ease-in-out;
                `}
            >
                <div
                    css={css`
                        display: inline-block;
                        width: 100%;
                    `}
                    ref={(subContainer: HTMLDivElement | null) =>
                        this.setSubContainerHeight(subContainer?.offsetHeight)
                    }
                >
                    {this.props.children}
                </div>
            </div>
        );
    }
}
