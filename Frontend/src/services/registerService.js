import axios from 'axios';
import { getApiUrl } from '../enviroment';

const API_URL = getApiUrl();

export const RegisterService = {
    registerClient: async (formData) => {
        try {
            const documentsArray = Object.values(formData.documents).filter(doc => doc !== null);
            const dataToSend = {
                ...formData,
                documents: documentsArray
            };

            console.log('Data to send:', dataToSend);
            const response = await axios.post(`${API_URL}/client`, dataToSend, {
                headers: { 'Content-Type': 'application/json' }
            });
            return response.data;
        } catch (error) {
            console.error('Error registering client:', error);
            throw error;
        }
    },

    // Función auxiliar para validar el formato del formulario
    validateFormData: (formData) => {
        const requiredFields = [
            'name', 'lastName', 'rut', 'email', 
            'years', 'contact', 'jobType', 
            'mensualIncome', 'jobYears', 'totalDebt'
        ];

        const missingFields = requiredFields.filter(field => !formData[field]);
        if (missingFields.length > 0) {
            throw new Error(`Faltan campos requeridos: ${missingFields.join(', ')}`);
        }

        return true;
    }
};