import React from 'react';
import { useNavigate } from 'react-router-dom';

export const LoanCard = ({ name, description, maxYears, minInterest, maxInterest, maxAmount, requirements }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate('/creditCalculator', { state: { name, maxYears, minInterest, maxInterest, maxAmount, requirements} });
  };

  return (
    <div className="p-6 bg-[#282C35] rounded-xl shadow-lg border border-gray-700 w-full">
      <h2 className="text-4xl font-bold text-white mb-2">{name}</h2>
      <p className="text-2xl text-gray-300 mb-4">{description}</p>
      
      <div className="grid grid-cols-2 gap-8 text-gray-200">
        {/* Columna izquierda - Especificaciones */}
        <div className="text-xl space-y-3">
          <li className="flex items-center justify-between">
            <span className="font-medium">Años máximos:</span>
            <span>{maxYears}</span>
          </li>
          <li className="flex items-center justify-between">
            <span className="font-medium">Interés mínimo:</span>
            <span>{minInterest}%</span>
          </li>
          <li className="flex items-center justify-between">
            <span className="font-medium">Interés máximo:</span>
            <span>{maxInterest}%</span>
          </li>
          <li className="flex items-center justify-between">
            <span className="font-medium">Monto máximo:</span>
            <span>
              {maxAmount === 50 
                ? `${maxAmount}% del valor actual de la propiedad` 
                : `${maxAmount}% del valor de la propiedad`}
            </span>
          </li>
        </div>

        {/* Columna derecha - Requisitos */}
        <div className="text-xl">
          <span className="font-medium block mb-2">Requisitos:</span>
          <ul className="space-y-2">
            {requirements.map((req, index) => (
              <li key={index} className="flex items-center text-gray-300">
                <span className="mr-2">•</span>
                {req}
              </li>
            ))}
          </ul>
        </div>
      </div>

      <button 
        onClick={handleClick}
        className="mt-8 w-1/2 mx-auto block text-xl py-2 px-4 bg-[#2A353D] text-white rounded-lg
        hover:bg-[#232E37] transition-colors duration-300 font-medium"
      >
        Solicitar Préstamo
      </button>
    </div>
  );
};