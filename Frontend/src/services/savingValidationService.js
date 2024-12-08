import axios from 'axios';
import { getApiUrl } from '../enviroment';

const API_URL = getApiUrl();

export const SavingValidationService = {
    getSavingById: async (savingId) => {
        try {
            const response = await axios.get(`${API_URL}/saving/${savingId}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching saving:', error);
            throw error;
        }
    },

    createSaving: async (formData) => {
        try {
            const response = await axios.post(`${API_URL}/saving`, formData);
            return response.data;
        } catch (error) {
            console.error('Error creating saving:', error);
            throw error;
        }
    },

    updateSavingStatus: async (id, result) => {
        try {
            const response = await axios.put(`${API_URL}/saving`, {
                id,
                result
            });
            return response.data;
        } catch (error) {
            console.error('Error updating saving status:', error);
            throw error;
        }
    },

    // Función auxiliar para validar el formato de los datos
    validateSavingData: (formData) => {
        if (!formData.clientLoanId || !formData.years || !formData.actualBalance) {
            throw new Error('Faltan campos requeridos');
        }
        
        // Validar que los arrays tengan 12 elementos
        if (formData.balances.length !== 12 || 
            formData.deposit.length !== 12 || 
            formData.withdraw.length !== 12) {
            throw new Error('Los arrays deben tener 12 elementos');
        }

        return true;
    }
};