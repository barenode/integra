import * as React from 'react';
import { FileUploader } from 'react-drag-drop-files';
import { useUpload } from './api'
import './App.css';

function Upload() {
    const uploadFn = useUpload();
    const handleChange = (file: File) => {
        console.log(`file ${file.name}`);
        uploadFn.mutate({ data: { file: file}});
    };    

  return (
    <div>
      <FileUploader handleChange={handleChange} name="file" />
    </div>
  ); 
}

export default Upload;
