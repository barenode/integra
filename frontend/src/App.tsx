import React from 'react';
import logo from './logo.svg';
import './App.css';
import { useFind } from './api'

function App() {
  
  const { data: report, isLoading  } = useFind(); 

  return (
    <div className="App">
      <hr />
      <h1>{report?.spans?.map(({spanId}) => <div>{spanId}</div>)}</h1>      
    </div>
  ); 
}

export default App;
