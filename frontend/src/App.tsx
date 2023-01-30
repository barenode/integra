import React from 'react';
import logo from './logo.svg';
import './App.css';
import { useQuery } from 'react-query'

function App() {
  
  const { isLoading, error, data } = useQuery('repoData', () =>
     fetch('https://api.github.com/repos/tannerlinsley/react-query').then(res =>
       res.json()
     )
   );
 
  //  if (isLoading) return 'Loading...';
 
  //  if (error) return 'An error has occurred: ';

  if (!data) {
    return null;
  }

  return (
    <div className="App">
      <hr />
      <h1>{data.message}</h1>      
    </div>
  );
}

export default App;
