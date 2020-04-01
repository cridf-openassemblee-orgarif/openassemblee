export const productionBuild = process.env.NODE_ENV === 'production';

export const colors = {
    white: '#fff',
    blue: '#bde4ff',
    grey: `#888`,
    clearGrey: '#eee',
    black: '#000',
    red: 'rgb(204, 0, 0, 1)',
    redBackground: '#ffcccc'
};

export const hexToRgbA = (hex: string, opacity: number) => {
    if (/^#([A-Fa-f0-9]{3}){1,2}$/.test(hex)) {
        let c: any = hex.substring(1).split('');
        if (c.length === 3) {
            c = [c[0], c[0], c[1], c[1], c[2], c[2]];
        }
        c = '0x' + c.join('');
        return (
            'rgba(' +
            [(c >> 16) & 255, (c >> 8) & 255, c & 255].join(',') +
            ',' +
            opacity +
            ')'
        );
    }
    throw new Error('Bad Hex');
};
