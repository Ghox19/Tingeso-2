import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

export const Navbar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleRegister = () => {
    navigate('/register');
  };

  const handleCredit = () => {
    navigate('/');
  };

  const handleView = () => {
    navigate('/loanCollection');
  };

  return (
    <nav className="bg-[#282C35] shadow-lg"> {/* color1: Gris azulado oscuro */}
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div className="flex-shrink-0 cursor-pointer" onClick={handleCredit}>
            <span className="text-white text-xl font-bold">Presta Banco</span>
          </div>

          {/* Desktop Menu */}
          <div className="hidden md:flex md:items-center md:space-x-4">
            <button
              className={`text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-300 ${
                location.pathname === '/' 
                  ? 'bg-[#2A353D]' /* color5: Verde azulado oscuro */
                  : 'bg-[#2F3429] hover:bg-[#2A353D]' /* color4: Verde oliva oscuro */
              }`}
              onClick={handleCredit}
            >
              Solicitar Crédito
            </button>
            <button
              className={`text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-300 ${
                location.pathname === '/register' 
                  ? 'bg-[#2A353D]' /* color3: Marrón oscuro */
                  : 'bg-[#2F3429] hover:bg-[#2A353D]' /* color4: Verde oliva oscuro */
              }`}
              onClick={handleRegister}
            >
              Registrarse
            </button>
            <button
              className={`text-white px-4 py-2 rounded-md text-sm font-medium transition-colors duration-300 ${
                location.pathname === '/loanCollection' 
                  ? 'bg-[#2A353D]' /* color2: Púrpura profundo */
                  : 'bg-[#2F3429] hover:bg-[#2A353D]' /* color4: Verde oliva oscuro */
              }`}
              onClick={handleView}
            >
              Listar Solicitudes
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
};