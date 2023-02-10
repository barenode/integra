import { defineConfig } from 'orval';

export default defineConfig({
  api: {
    output: {
      mode: 'split',
      target: 'src/api.ts',
      schemas: 'src/model',
      client: 'react-query',
      mock: false,
      override: {
        mutator: {
          path: './src/custom-instance.ts',
          name: 'customInstance',
        }
      }
    },
    input: {
      target: 'http://localhost:7777/integra.yaml'
    },
  },
});
