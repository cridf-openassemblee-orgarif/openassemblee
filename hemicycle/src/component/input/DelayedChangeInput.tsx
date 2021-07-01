/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import * as React from 'react';
import { colors } from '../../constants';

const delayTime = 400;

interface Props extends React.InputHTMLAttributes<HTMLInputElement> {
    onValueChange: (value: string) => void;
}

interface State {
    value: string;
}

export default class DelayedChangeInput extends React.Component<Props, State> {
    timeout: any = undefined;

    public state: State = {
        value: this.props.value as string,
    };

    componentDidUpdate(
        prevProps: Readonly<Props>,
        prevState: Readonly<State>
    ): void {
        if (prevProps.value !== this.props.value) {
            this.setState((state) => ({
                ...state,
                value: this.props.value as string,
            }));
        }
    }

    onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        if (this.timeout) {
            clearTimeout(this.timeout);
        }
        const value = event.currentTarget.value;
        this.timeout = setTimeout(
            () => this.props.onValueChange(value),
            delayTime
        );
        this.setState((state) => ({ ...state, value }));
    };

    public render() {
        const { onValueChange, ...inputProps } = this.props;
        return (
            <input
                {...inputProps}
                value={this.state.value}
                onChange={this.onChange}
                css={css`
                    ${this.state.value &&
                    this.state.value !== '' &&
                    css`
                        background: ${colors.blue};
                    `}
                `}
            />
        );
    }
}
