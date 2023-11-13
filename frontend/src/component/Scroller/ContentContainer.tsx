import * as React from 'react';
import styled from 'styled-components';
import { useReadReportRange } from '../../api/api';
import { useApplicationContext } from '../../context/applicationState';
import LogComponent from '../LogComponent';

interface IContentContainerProps {
    id: string;
    pageNumber: number;
    startIndex: number;
    endIndex: number;
    top: number;
    envelopeRef: React.RefObject<HTMLDivElement>;
    innerRef: React.RefObject<HTMLDivElement>;
}

const ITEM_HEIGHT = 28;

const Enevelope = styled.div<{ elementHeight: number;}>`
    position: absolute;   
    width: 100%; 
    border: 5px solid back;
    ${({ elementHeight }) => `height: ${elementHeight}px;`}        
`;

const Inner = styled.div`
    position: relative;   
`;

const ContentContainer: React.FC<IContentContainerProps> = ({
    id,
    pageNumber,
    startIndex,
    endIndex,
    top,
    envelopeRef,
    innerRef
}) => {    

    console.log(`rendering page ${pageNumber} with top ${top}`);
    const { data: report } = useReadReportRange(id, `${startIndex}`, `${endIndex}`);
    const { spans } = report || {}
    
    if (!spans) {
        return null;
    }

    // const top = startIndex * ITEM_HEIGHT;
    const height =  spans.length * ITEM_HEIGHT;
    // console.log(`element starts at ${top}px with height ${height}px ends at ${top + height}px`);

    return (
        <Enevelope ref={envelopeRef} style={{top: `${top}px`}} elementHeight={height} className="Enevelope">
            <Inner ref={innerRef} className="Inner">
                {spans.map((span) => (
                    <LogComponent key={span.id} reportId={id} span={span} />
                ))}
            </Inner>
        </Enevelope>
    )
}

export default ContentContainer;