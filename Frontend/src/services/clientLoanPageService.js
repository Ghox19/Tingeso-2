import axios from 'axios';
import { getApiUrl } from '../enviroment';

const API_URL = getApiUrl();

export const ClientLoanPageService = {
    getClientLoans: async () => {
        try {
            const response = await axios.get(`${API_URL}/clientLoan`);
            return response.data;
        } catch (error) {
            console.error('Error fetching loans:', error);
            throw error;
        }
    }
};