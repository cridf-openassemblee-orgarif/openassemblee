/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import { domUid } from '../../utils';

const keyframeId = domUid();

// thanks to https://loading.io/css/
const LoadingIcon = (props: { height: number; color: string }) => (
    <div
        css={css`
            display: inline-block;
            width: ${props.height}px;
            height: ${props.height}px;
            &:after {
                content: ' ';
                display: block;
                width: ${props.height * 0.8}px;
                height: ${props.height * 0.8}px;
                margin: ${props.height * 0.1}px;
                border-radius: 50%;
                border: ${props.height * 0.075}px solid ${props.color};
                border-color: ${props.color} transparent ${props.color}
                    transparent;
                animation: ${keyframeId} 1.2s linear infinite;
            }
            @keyframes ${keyframeId} {
                0% {
                    transform: rotate(0deg);
                }
                100% {
                    transform: rotate(360deg);
                }
            }
        `}
    />
);
export default LoadingIcon;
