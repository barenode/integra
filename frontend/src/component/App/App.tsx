import { useState } from 'react'
import ReportComponent from '../ReportComponent';
import Upload from '../Upload';
import { ApplicationContext, ApplicationState } from '../../context/applicationState';

function App() {
  const [ state, setState ] = useState<ApplicationState>({}); 
  const { report } = state;
  return (
    <ApplicationContext.Provider value= {{ state, setState }}>
      <div className="App">
        {!report && <Upload />}
        {report && <ReportComponent report={report} />}
      </div>
    </ApplicationContext.Provider>

  ); 
}

export default App;
