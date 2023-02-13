import * as React from 'react';
import { useReadSpan } from '../../api'

interface ISpanDetailComponentProps {
    reportId: string;
    spanId: string;
}

const SpanDetailComponent: React.FC<ISpanDetailComponentProps> = ({
    reportId,
    spanId
}) => {
    const { data: spanDetail } = useReadSpan(reportId, spanId); 
    const { request, response } = spanDetail || {};
    return (
        <div>
            <pre>{request}</pre>
            <hr />
            <pre>{response}</pre>
        </div>
    );
  };
  
  export default SpanDetailComponent;