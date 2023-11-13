import * as React from 'react';
import styled from 'styled-components';
import type { ReportInfo } from '../../model';    
import LogComponent from '../LogComponent';
import { useReadReport } from '../../api/api'
import { useApplicationContext } from '../../context/applicationState';
import SpanDetailComponent from '../SpanDetailComponent';
import Scroller from '../Scroller';

interface IReportComponentProps {
    report: ReportInfo;
}

const ReportComponentContainer = styled.div`
    padding: 2px; 
    flex: 1 1 500px; 
    overflow: auto;
    width: 100%;
`;

const SpanDetailContainer = styled.div`
    padding: 2px;   
    flex: 0 0 500px; 
    overflow: auto;
    width: 100%;
`;



const ReportComponent: React.FC<IReportComponentProps> = ({
    report
}) => {
    // const { data: report } = useReadReport(id)
    // const { spans } = report || {}
    const { id } = report;
    const { state: { selectedSpanId }} = useApplicationContext();
    

    return (
        <>
            {/* <ReportComponentContainer>
                <div>
                    {spans?.map((span) => (
                        <LogComponent key={id} reportId={id} span={span} />
                    ))}
                </div>
            </ReportComponentContainer> */}
            {report && <Scroller report={report} />}
            {selectedSpanId && (
                <SpanDetailContainer>
                    <div>
                        <SpanDetailComponent reportId={id} spanId={selectedSpanId} />
                    </div>
                </SpanDetailContainer>
            )}
        </>
    )
  }
  
  export default ReportComponent;