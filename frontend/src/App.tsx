import * as React from 'react';
import './App.css';
import { useParseReport } from './api'
import ReportComponent from './component/ReportComponent';

function App() {
  console.log('parsing report ...');
  const { data: reportInfo } = useParseReport(); 

  return (
    <div className="App">
      {reportInfo && <ReportComponent report={reportInfo} />}
    </div>
  ); 
}

export default App;
