import React from 'react';
import { Route } from 'react-router-dom';
import { LoanSelection } from '../pages/loanSelection';
import { Register } from '../pages/register';
import { CreditCalculator } from '../pages/creditCalculator';
import { ClientLoanPage } from '../pages/clientLoanPage';
import { ClientLoanValidation } from '../pages/clientLoanValidation';
import { SavingValidation } from '../pages/savingValidation';
import { Test } from '../pages/test';

const routes = [
  { path: '/', element: <LoanSelection /> },
  { path: '/test', element: <Test /> },
  { path: '/register', element: <Register /> },
  { path: '/creditCalculator', element: <CreditCalculator /> },
  { path: '/loanCollection', element: <ClientLoanPage /> },
  { path: '/loanValidation', element: <ClientLoanValidation />},
  { path: '/savingValidation', element: <SavingValidation />}
];

export default routes;

