import { useState } from 'react'
import styled from 'styled-components';
import ReportComponent from '../ReportComponent';
import Upload from '../Upload';
import { ApplicationContext, ApplicationState } from '../../context/applicationState';
import Scroller from '../Scroller';

const AppContainer = styled.div`
  display: flex;
  height: 100%;
  width: 100%;
`;

function App() {
  const [ state, setState ] = useState<ApplicationState>({ expandedSpanIds: new Set<string>() }); 
  const { report } = state;
  return (
    <ApplicationContext.Provider value= {{ state, setState }}>
      <>
        {report ? 
          <ReportComponent report={report} /> 
          // <Scroller report={report} /> 
          : 
          <Upload />
        }
      </>
    </ApplicationContext.Provider>
  )
}

export default App;
