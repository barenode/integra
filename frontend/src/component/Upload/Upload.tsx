import { FileUploader } from 'react-drag-drop-files';
import { useParseReport } from '../../api/api'
import { useApplicationContext } from '../../context/applicationState';
import { ReportInfo } from '../../model';

function Upload() {
  const parseReport = useParseReport();
  const { state, setState } = useApplicationContext();
  const onSuccess = (report: ReportInfo) => {
    setState({ ...state, report })
  };    
  const handleChange = (file: File) => {
      parseReport.mutate(
        { data: { file }},
        { onSuccess }                      
      );        
  };    
  
  return (
    <div>
      <FileUploader handleChange={handleChange} name="file" />
    </div>
  ); 
}

export default Upload;
