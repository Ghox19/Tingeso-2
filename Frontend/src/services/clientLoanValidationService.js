import axios from 'axios';
import { getApiUrl } from '../enviroment';

const API_URL = getApiUrl();

export const ClientLoanValidationService = {
    getClientLoanById: async (id) => {
        try {
            const response = await axios.get(`${API_URL}/clientLoan/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching loan:', error);
            throw error;
        }
    },

    fetchClientDocuments: async (id) => {
        try {
            const response = await axios.get(`${API_URL}/client/documents/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error fetching Client Documents:', error);
            throw error;
        }
    },

    downloadDocument: async (id, name) => {
        try {
            const response = await axios.get(`${API_URL}/document/download/${id}`, {
                responseType: 'blob',
                headers: {
                    'Accept': 'application/pdf'
                }
            });
            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', name.endsWith('.pdf') ? name : `${name}.pdf`);
            document.body.appendChild(link);
            link.click();
            window.URL.revokeObjectURL(url);
            link.remove();
        } catch (error) {
            console.error('Error downloading the document', error);
            throw error;
        }
    },

    approveDocument: async (id, document) => {
        document.approved = true;
        try {
            const response = await axios.put(`${API_URL}/document/${id}`, document);
            return response.data;
        } catch (error) {
            console.error('Error approving the document', error);
            throw error;
        }
    },

    rejectLoan: async (id, rejectMessage) => {
        const newFormData = { id, message: rejectMessage };
        try {
            const response = await axios.put(`${API_URL}/clientLoan/reject`, newFormData);
            return response.data;
        } catch (error) {
            console.error('Error rejecting the loan', error);
            throw error;
        }
    },

    finalizeClientLoan: async (id, fase) => {
        const newFormData = { id, fase };
        try {
            const response = await axios.put(`${API_URL}/clientLoan/final`, newFormData);
            return response.data;
        } catch (error) {
            console.error('Error finalizing the loan:', error);
            throw error;
        }
    },

    preApproveLoan: async (loanData) => {
        try {
            const response = await axios.put(`${API_URL}/clientLoan/preApproved`, loanData);
            return response.data;
        } catch (error) {
            console.error('Error pre-approving the loan', error);
            throw error;
        }
    }
};