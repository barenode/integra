import * as React from 'react';
import styled from 'styled-components';
import { useReadReportRange } from '../../api/api';
import { useApplicationContext } from '../../context/applicationState';
import LogComponent from '../LogComponent';

interface IContentContainerProps {
    id: string;
    page: number;
    pageSize: number;
}

const ITEM_HEIGHT = 28;

const Enevelope = styled.div<{ top: number; elementHeight: number;}>`
    position: absolute;   
    width: 100%; 
    background-color: red;
    border: 5px solid back;
    ${({ elementHeight }) => `height: ${elementHeight}px;`}    
    ${({ top }) => `top: ${top}px;`}    
`;

const Inner = styled.div`
    position: relative;   
`;

const ContentContainer: React.FC<IContentContainerProps> = ({
    id,
    page,
    pageSize
}) => {    
    const startIndex = page * pageSize;
    const endIndex = startIndex + pageSize;
    const { state, setState } = useApplicationContext();
    console.log(`rendering page ${page} translated to range ${startIndex}-${endIndex}`);
    const { data: report } = useReadReportRange(id, `${startIndex}`, `${endIndex}`);
    const { spans } = report || {}
    return (
        <Enevelope top={page * (pageSize * ITEM_HEIGHT)} elementHeight={pageSize * ITEM_HEIGHT}>
            <Inner>
                {spans?.slice(0, pageSize).map((span) => (
                    <LogComponent key={span.id} reportId={id} span={span} />
                ))}
            </Inner>
        </Enevelope>
    )
}

export default ContentContainer;