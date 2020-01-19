/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import React from 'react';
import { injector } from '../service/injector';
import { Unsuscriber } from '../service/EventBus';

const classes = {
    container: css`
        width: 100%;
        height: 100%;
    `
};

interface Props {
    render: (width: number, height: number) => React.ReactNode;
}

interface State {
    width: number;
    height: number;
}

export default class SizingContainer extends React.PureComponent<Props, State> {
    state = {
        width: 1200,
        height: 720
    };

    private unsuscriber?: Unsuscriber;

    public componentDidMount() {
        // TODONOW attention à laisser () => this.forceUpdate(), NE PAS mettre this.forceUpdate
        // comment tester et ne plus se faire avoir ?
        // putain de javascript
        this.unsuscriber = injector().applicationEventBus.subscribe(
            'window_resized_event',
            () => this.forceUpdate()
        );
    }

    public componentWillUnmount() {
        if (this.unsuscriber) {
            this.unsuscriber();
        }
    }

    private sizing = (e: HTMLDivElement | null) => {
        if (e) {
            const width = e.offsetWidth;
            const height = e.offsetHeight;
            if (width !== this.state.width || height !== this.state.height) {
                this.setState(s => ({ ...s, width, height }));
            }
        }
    };

    public render() {
        const rendered = this.props.render(this.state.width, this.state.height);
        // so this.props.render return is optional
        return (
            <div ref={e => this.sizing(e)} css={classes.container}>
                {rendered ? rendered : null}
            </div>
        );
    }
}
