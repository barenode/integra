import * as React from 'react';
import styled from 'styled-components';
import { useReadSpan } from '../../api/api'

interface ISpanDetailComponentProps {
    reportId: string;
    spanId: string;
}

const SpanDetailHeader = styled.div`
    padding: 0.5em; 
    background: rgb(19, 35, 55); 
    position: sticky; 
    top: 0px; 
    z-index: 1;
`;

const SpanDetailComponent: React.FC<ISpanDetailComponentProps> = ({
    reportId,
    spanId
}) => {
    const { data: spanDetail } = useReadSpan(reportId, spanId); 
    const { request, response } = spanDetail || {};
    return (
        <>
            <SpanDetailHeader>Request</SpanDetailHeader>
            <pre>{request}</pre>
            <SpanDetailHeader>response</SpanDetailHeader>
            <pre>{response}</pre>
        </>
    );
  };
  
  export default SpanDetailComponent;