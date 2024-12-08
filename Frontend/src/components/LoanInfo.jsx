import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const LoanInfo = ({id,loanName, years, interest, loanAmount, mensualPay, rut, fase}) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate('/loanValidation', { state: {id}});
  };

  return (
    <div className="max-w-6xl mx-auto bg-[#2A353D] rounded-xl shadow-md overflow-hidden p-2">
      <ul className="grid grid-cols-8 gap-4">
        <li className="w-32 overflow-hidden truncate border-r border-[#29394D] text-center">
          <span className="font-semibold text-white text-sm">{loanName}</span>
        </li>
        <li className="w-32 overflow-hidden truncate border-r border-[#29394D] text-center">
          <span className="font-semibold text-white text-sm">{years}</span>
        </li>
        <li className="w-32 overflow-hidden truncate border-r border-[#29394D] text-center">
          <span className="font-semibold text-white text-sm">{interest}%</span>
        </li>
        <li className="w-32 overflow-hidden truncate border-r border-[#29394D] text-center">
          <span className="font-semibold text-white text-sm">${loanAmount}</span>
        </li>
        <li className="w-32 overflow-hidden truncate border-r border-[#29394D] text-center">
          <span className="font-semibold text-white text-sm">${mensualPay}</span>
        </li>
        <li className="w-32 overflow-hidden truncate border-r border-[#29394D] text-center">
          <span className="font-semibold text-white text-sm">{rut}</span>
        </li>
        <li className="w-32 overflow-hidden truncate border-r border-[#29394D] text-center">
          <span className="font-semibold text-white text-sm">{fase}</span>
        </li>
        <li className="w-32 overflow-hidden text-center">
          <button 
            onClick={handleClick}
            className={`w-full text-white text-sm py-1 px-3 rounded-lg transition duration-300 ${
              fase === 'Rechazado' 
                ? 'bg-red-600 hover:bg-red-700' 
                : fase === 'Desembolso' 
                  ? 'bg-green-600 hover:bg-green-700'
                  : 'bg-blue-600 hover:bg-blue-700'
            }`}
          >
            Revisar
          </button>
        </li>
      </ul>
    </div>
  );
};