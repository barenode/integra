import * as React from 'react';
import type { Report } from '../../model';    
import LogComponent from '../LogComponent';

interface IReportComponentProps {
    report: Report;
}

const ReportComponent: React.FC<IReportComponentProps> = ({
    report: { spans }
}) => {
    return (
        <>
            {spans?.map((span) => <LogComponent span={span} />)}
        </>
    );
  };
  
  export default ReportComponent;