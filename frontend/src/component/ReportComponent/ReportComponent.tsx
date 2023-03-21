import * as React from 'react';
import styled from 'styled-components';
import type { ReportInfo } from '../../model';    
import LogComponent from '../LogComponent';
import { useReadReport } from '../../api/api'

interface IReportComponentProps {
    report: ReportInfo;
}

const ReportComponentContainer = styled.div`
    flex-grow: 1;
    flex-basis: auto;
    padding: 2px;   
`;

const ReportComponent: React.FC<IReportComponentProps> = ({
    report: { id }
}) => {
    const { data: report } = useReadReport(id)
    const { spans } = report || {}
    return (
        <ReportComponentContainer>
            {spans?.map((span) => (
                <LogComponent key={id} reportId={id} span={span} />
            ))}
        </ReportComponentContainer>
    )
  }
  
  export default ReportComponent;