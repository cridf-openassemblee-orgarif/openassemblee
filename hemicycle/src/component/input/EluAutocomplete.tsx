/** @jsx jsx */
import { css, jsx } from '@emotion/core';
import { useEffect, useState } from 'react';
import { colors } from '../../constants';
import { useCombobox } from 'downshift';
import EluAutocompleteItem from './EluAutocompleteItem';
import { SelectedEluSource, Selection } from '../App';
import { Dict, eluToString, get } from '../../utils';
import { Elu, GroupePolitique } from '../../domain/elu';
import { EluId, GroupePolitiqueId } from '../../domain/nominal';

interface Props {
    selection: Selection;
    elus: Elu[];
    eluById: Dict<EluId, Elu>;
    groupePolitiqueById: Dict<GroupePolitiqueId, GroupePolitique>;
    updateSelectedEluId: (
        selectedEluId: EluId | undefined,
        source: SelectedEluSource
    ) => void;
    deleteMode: boolean;
}

const isEqual = (item: Elu, inputValue: string) =>
    eluToString(item).toLowerCase().latinize() ===
    inputValue.toLowerCase().latinize();

const compare = (itemAsString: string, inputValue: string) =>
    itemAsString
        .toLowerCase()
        .latinize()
        .startsWith(inputValue.toLowerCase().latinize());

const EluAutocomplete = (props: Props) => {
    const [inputItems, setInputItems] = useState(props.elus);
    const {
        isOpen,
        getInputProps,
        highlightedIndex,
        getItemProps,
        setInputValue,
        getComboboxProps,
        getMenuProps,
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
                        props.updateSelectedEluId(undefined, 'input');
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
                    props.updateSelectedEluId(selectedItem.id, 'input');
                }
            }, 0);
            setInputItems(
                props.elus.filter((elu) =>
                    inputValue && inputValue !== ''
                        ? compare(eluToString(elu), inputValue) ||
                          compare(elu.prenom, inputValue) ||
                          compare(elu.nom, inputValue)
                        : []
                )
            );
        },
    });
    useEffect(
        () => {
            if (props.selection.selectedEluSource !== 'input') {
                if (props.selection.selectedEluId) {
                    const elu = get(
                        props.eluById,
                        props.selection.selectedEluId
                    );
                    setInputValue(eluToString(elu));
                } else {
                    setInputValue('');
                }
            }
        },
        // eslint-disable-next-line
        [props.selection.selectedEluId, props.selection.selectedEluSource]
    );
    return (
        <div
            css={css`
                position: relative;
            `}
            {...getComboboxProps()}
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
                    ${props.selection.selectedEluId &&
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
            <ul
                css={css`
                    position: absolute;
                    width: 100%;
                    top: 40px;
                    left: 0;
                    background: ${colors.white};
                    padding-left: 0;
                `}
                {...getMenuProps()}
            >
                {isOpen &&
                    inputItems.map((elu, index) => (
                        <li
                            key={elu.id}
                            css={css`
                                list-style: none;
                            `}
                            {...getItemProps({ item: elu, index })}
                        >
                            <EluAutocompleteItem
                                elu={elu}
                                highlighted={highlightedIndex === index}
                                groupePolitiqueById={props.groupePolitiqueById}
                            />
                        </li>
                    ))}
            </ul>
        </div>
    );
};
export default EluAutocomplete;
