import * as React from 'react';
import './App.css';
import { useFind } from './api'
import ReportComponent from './component/ReportComponent';

function App() {
  
  const { data: report } = useFind(); 

  return (
    <div className="App">
      {report && <ReportComponent report={report} />}
    </div>
  ); 
}

export default App;
