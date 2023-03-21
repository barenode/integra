import { useState } from 'react'
import styled from 'styled-components';
import ReportComponent from '../ReportComponent';
import Upload from '../Upload';
import { ApplicationContext, ApplicationState } from '../../context/applicationState';

const AppContainer = styled.div`
  display: flex;
`;

function App() {
  const [ state, setState ] = useState<ApplicationState>({}); 
  const { report } = state;
  return (
    <ApplicationContext.Provider value= {{ state, setState }}>
      <AppContainer>
        {report ? 
          <ReportComponent report={report} /> 
          : 
          <Upload />
        }
      </AppContainer>
    </ApplicationContext.Provider>
  )
}

export default App;
