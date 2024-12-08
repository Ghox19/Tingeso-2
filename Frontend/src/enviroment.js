export const environment = {
    development: {
      API_URL: 'http://localhost:8080'
    },
    production: {
      API_URL: 'http://104.41.28.230:8090'
    }
  };
  
  // Función para obtener la URL según el ambiente
export const getApiUrl = () => {
return environment[process.env.NODE_ENV].API_URL;
};
