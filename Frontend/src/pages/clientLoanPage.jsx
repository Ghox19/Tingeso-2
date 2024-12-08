import React, { useEffect, useState } from 'react';
import { LoanInfo } from '../components/LoanInfo';
import { ClientLoanPageService } from '../services/clientLoanPageService';

export const ClientLoanPage = () => {
  const [loans, setLoans] = useState([]);

  useEffect(() => {
    const fetchLoans = async () => {
      try {
        const data = await ClientLoanPageService.getClientLoans();
        setLoans(data);
      } catch (error) {
        console.error('Error fetching loans:', error);
      }
    };

    fetchLoans();
  }, []);


  return (
    <div className="min-h-screen">
      <nav className="w-full px-6 py-4">
        <h2 className="text-2xl font-bold text-white">Solicitudes de Prestamo</h2>
      </nav>
      
      <div className="container mx-auto px-4 py-6">
        {/* Header Row */}
        <div className="max-w-6xl mx-auto  rounded-xl shadow-md overflow-hidden p-2 mb-4">
          <ul className="grid grid-cols-8 gap-4">
            <li className="w-32 overflow-hidden truncate text-center">
              <span className="font-bold text-white text-sm">Nombre</span>
            </li>
            <li className="w-32 overflow-hidden truncate text-center">
              <span className="font-bold text-white text-sm">Años</span>
            </li>
            <li className="w-32 overflow-hidden truncate text-center">
              <span className="font-bold text-white text-sm">Interés</span>
            </li>
            <li className="w-32 overflow-hidden truncate text-center">
              <span className="font-bold text-white text-sm">Monto</span>
            </li>
            <li className="w-32 overflow-hidden truncate text-center">
              <span className="font-bold text-white text-sm">Pago Mensual</span>
            </li>
            <li className="w-32 overflow-hidden truncate text-center">
              <span className="font-bold text-white text-sm">RUT</span>
            </li>
            <li className="w-32 overflow-hidden truncate text-center">
              <span className="font-bold text-white text-sm">Fase</span>
            </li>
            <li className="w-32 overflow-hidden truncate text-center">
              <span className="font-bold text-white text-sm">Acciones</span>
            </li>
          </ul>
        </div>
  
        {/* Loan Items */}
        <div className="flex flex-col space-y-4">
          {loans.map((loan) => (
            <LoanInfo
              key={loan.id}
              id={loan.id}
              loanName={loan.loanName}
              years={loan.years}
              interest={loan.interest}
              loanAmount={loan.loanAmount}
              mensualPay={loan.mensualPay}
              rut={loan.client.rut}
              fase={loan.fase}
            />
          ))}
        </div>
      </div>
    </div>
  );
};