import * as React from 'react';
import styled from 'styled-components';
import type { ReportInfo } from '../../model';    
import LogComponent from '../LogComponent';
import { useReadReport } from '../../api/api'
import { useApplicationContext } from '../../context/applicationState';
import SpanDetailComponent from '../SpanDetailComponent';

interface IReportComponentProps {
    report: ReportInfo;
}

const ReportComponentContainer = styled.div`
    padding: 2px; 
    flex: 1 1 500px; 
    overflow: auto;
`;

const SpanDetailContainer = styled.div`
    padding: 2px;   
    flex: 0 0 500px; 
    overflow: auto;
`;



const ReportComponent: React.FC<IReportComponentProps> = ({
    report: { id }
}) => {
    const { data: report } = useReadReport(id)
    const { spans } = report || {}
    const { state: { selectedSpanId }} = useApplicationContext();

    return (
        <>
            <ReportComponentContainer>
                {spans?.map((span) => (
                    <LogComponent key={id} reportId={id} span={span} />
                ))}
            </ReportComponentContainer>
            {selectedSpanId && (
                <SpanDetailContainer>
                    <SpanDetailComponent reportId={id} spanId={selectedSpanId} />
                </SpanDetailContainer>
            )}
        </>
    )
  }
  
  export default ReportComponent;