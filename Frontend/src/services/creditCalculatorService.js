import axios from 'axios';
import { getApiUrl } from '../enviroment';

const API_URL = getApiUrl();

export const CreditCalculatorService = {
    validateClient: async (rut) => {
        try {
            const response = await axios.get(`${API_URL}/client/rinfo/${rut}`);
            return response.data;
        } catch (error) {
            console.error('Error al obtener el cliente:', error);
            throw error;
        }
    },

    calculateLoan: async (formData) => {
        try {
            const response = await axios.post(`${API_URL}/clientLoan/calculator`, formData);
            return response.data.toString();
        } catch (error) {
            console.error('Error calculating:', error);
            throw error;
        }
    },

    submitLoanApplication: async (submitData) => {
        try {
            const response = await axios.post(`${API_URL}/clientLoan`, submitData, {
                headers: { 'Content-Type': 'application/json' }
            });
            return response.data;
        } catch (error) {
            throw error;
        }
    },

    validateLoanApplication: (data) => {
        const {
            clientData,
            mensualPay,
            formData,
            propertyValue,
            maxAmountPercentage
        } = data;

        // Validación del monto máximo del préstamo
        const maxLoanAmount = (propertyValue * maxAmountPercentage) / 100;
        if (formData.loanAmount > maxLoanAmount) {
            throw new Error('Monto del préstamo excede el límite');
        }

        // Validación de la cuota respecto al ingreso
        const cuotaIncome = (mensualPay / clientData.mensualIncome) * 100;
        if (cuotaIncome > 35) {
            throw new Error('La cuota excede el 35% del sueldo');
        }

        // Validación de años de trabajo
        if (clientData.jobYears < 1) {
            throw new Error('El cliente no tiene suficiente antigüedad laboral');
        }

        // Validación de deuda total
        const totalDebt = parseInt(clientData.totalDebt) + parseInt(mensualPay);
        const debtCuota = (totalDebt / clientData.mensualIncome) * 100;
        if (debtCuota > 50) {
            throw new Error('La deuda total excede el 50% del sueldo');
        }

        // Validación de edad
        const totalYears = parseInt(clientData.years) + parseInt(formData.years);
        if (totalYears > 70) {
            throw new Error('No se puede superar los 70 años de edad');
        }

        return true;
    }
};