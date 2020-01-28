/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import { useEffect, useState } from 'react';
import { colors } from '../../constants';
import { useCombobox } from 'downshift';
import EluAutocompleteItem from './EluAutocompleteItem';
import { AppData, SelectedEluSource } from '../App';
import { eluToString } from '../../utils';

interface Props {
    selectedElu?: Elu;
    selectedEluSource?: SelectedEluSource;
    updateSelectedElu: (
        selectedElu: Elu | undefined,
        source: SelectedEluSource
    ) => void;
    data: AppData;
    deleteMode: boolean;
}

const isEqual = (item: Elu, inputValue: string) =>
    eluToString(item)
        .toLowerCase()
        .latinize() === inputValue.toLowerCase().latinize();

const compare = (itemAsString: string, inputValue: string) =>
    itemAsString
        .toLowerCase()
        .latinize()
        .startsWith(inputValue.toLowerCase().latinize());

const EluAutocomplete = (props: Props) => {
    const [inputItems, setInputItems] = useState(props.data.elus);
    const {
        isOpen,
        getInputProps,
        highlightedIndex,
        getItemProps,
        setInputValue
    } = useCombobox({
        items: inputItems,
        itemToString: eluToString,
        onInputValueChange: ({ inputValue, selectedItem, isOpen }) => {
            // must be open car onInputValueChange est appelé au setInputValue extérieur
            if (isOpen) {
                setTimeout(() => {
                    if (
                        inputValue &&
                        selectedItem &&
                        !isEqual(selectedItem, inputValue)
                    ) {
                        props.updateSelectedElu(undefined, 'input');
                    }
                }, 0);
            }
            // préférer ça au onSelectedItemChange car si user sélectionne deux fois de suite le meme item
            // onSelectedItemChange n'est pas rappelé
            setTimeout(() => {
                if (
                    inputValue &&
                    selectedItem &&
                    isEqual(selectedItem, inputValue)
                ) {
                    props.updateSelectedElu(selectedItem, 'input');
                }
            }, 0);
            setInputItems(
                props.data.elus.filter(elu =>
                    inputValue && inputValue !== ''
                        ? compare(eluToString(elu), inputValue) ||
                          compare(elu.prenom, inputValue) ||
                          compare(elu.nom, inputValue)
                        : []
                )
            );
        }
    });
    useEffect(() => {
        if (props.selectedEluSource !== 'input') {
            if (props.selectedElu) {
                setInputValue(eluToString(props.selectedElu));
            } else {
                setInputValue('');
            }
        }
    }, [props.selectedElu, props.selectedEluSource]);
    return (
        <div
            css={css`
                position: relative;
            `}
        >
            <input
                css={css`
                    border: 1px solid ${colors.grey};
                    outline: none;
                    font-size: 20px;
                    height: 40px;
                    width: 100%;
                    padding: 0 10px;
                    &:focus {
                        border: 1px solid ${colors.black};
                    }
                    ${props.selectedElu &&
                        css`
                            background: ${colors.blue};
                        `}
                    &:disabled {
                        background: ${colors.redBackground};
                        border-color: ${colors.red};
                    }
                `}
                {...getInputProps()}
                disabled={props.deleteMode}
            />
            <div
                css={css`
                    position: absolute;
                    width: 100%;
                    top: 40px;
                    left: 0;
                    background: ${colors.white};
                `}
            >
                {isOpen &&
                    inputItems.map((elu, index) => (
                        <div
                            key={elu.id}
                            {...getItemProps({ item: elu, index })}
                        >
                            <EluAutocompleteItem
                                elu={elu}
                                highlighted={highlightedIndex === index}
                                data={props.data}
                            />
                        </div>
                    ))}
            </div>
        </div>
    );
};
export default EluAutocomplete;
