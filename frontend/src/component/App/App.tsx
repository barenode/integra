import { useState } from 'react'
import styled from 'styled-components';
import ReportComponent from '../ReportComponent';
import Upload from '../Upload';
import { ApplicationContext, ApplicationState } from '../../context/applicationState';

const AppContainer = styled.div`
  display: flex;
  height: 100%;
  width: 100%;
`;

function App() {
  const [ state, setState ] = useState<ApplicationState>({}); 
  const { report } = state;
  return (
    <ApplicationContext.Provider value= {{ state, setState }}>
      <>
        {report ? 
          <ReportComponent report={report} /> 
          : 
          <Upload />
        }
      </>
    </ApplicationContext.Provider>
  )
}

export default App;
