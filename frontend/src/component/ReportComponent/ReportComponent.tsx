import * as React from 'react';
import type { ReportInfo } from '../../model';    
import LogComponent from '../LogComponent';
import { useReadReport } from '../../api'

interface IReportComponentProps {
    report: ReportInfo;
}

const ReportComponent: React.FC<IReportComponentProps> = ({
    report: { id }
}) => {
    const { data: report } = useReadReport(id); 
    const { spans } = report || {};
    return (
        <>
            {spans?.map((span) => <LogComponent span={span} />)}
        </>
    );
  };
  
  export default ReportComponent;