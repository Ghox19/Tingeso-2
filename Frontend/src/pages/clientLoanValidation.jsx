import React, { useEffect, useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { ClientLoanValidationService } from '../services/clientLoanValidationService';

export const ClientLoanValidation = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { id } = location.state || {};

    const [loan, setLoan] = useState([]);
    const [client, setClient] = useState([]);
    const [clientDocuments, setClientDocuments] = useState([]);
    const [idSaving, setIdSaving] = useState(0);
    const [saving, setSaving] = useState([]);
    const [showAdditionalFields, setShowAdditionalFields] = useState(false);
    const [fireInsurance, setFireInsurance] = useState('');
    const [deduction, setDeduction] = useState('');
    const [showMessage, setShowMessage] = useState(false);
    const [rejectMessage, setRejectMessage] = useState('');

    const areAllDocumentsApproved = () => {
      const areClientDocsApproved = clientDocuments.every(doc => doc.approved);
      const areLoanDocsApproved = loan.documents?.every(doc => doc.approved);
      const isSavingApproved = saving.result === "Aprobado";
      return areClientDocsApproved && areLoanDocsApproved && isSavingApproved;
    };

    const fetchLoan = async () => {
      try {
          const loanData = await ClientLoanValidationService.getClientLoanById(id);
          setLoan(loanData);
          setClient(loanData.client);
          const documentsData = await ClientLoanValidationService.fetchClientDocuments(loanData.client.id);
          setClientDocuments(documentsData);
          if (loanData.savings !== null) {
              setSaving(loanData.savings);
              setIdSaving(loanData.savings.id);
          }
      } catch (error) {
          console.error('Error fetching loans:', error);
      }
  };

    const handleDownload = async (id, name) => {
      await ClientLoanValidationService.downloadDocument(id, name);
    };

    const handleSavings = () => {
      navigate('/savingValidation', { state: {id, idSaving} });
    };

    const handleDocumentApproved = async (id, document) => {
      await ClientLoanValidationService.approveDocument(id, document);
      fetchLoan();
    };

    const handleReject = async (e) => {
      e.preventDefault();
      await ClientLoanValidationService.rejectLoan(id, rejectMessage);
      setShowMessage(false);
      fetchLoan();
    };

    const handleClientApproved = async (e) => {
      e.preventDefault();
      await ClientLoanValidationService.finalizeClientLoan(id, "Desembolso");
      fetchLoan();
    };

    const handleClientReject = async (e) => {
      e.preventDefault();
      await ClientLoanValidationService.finalizeClientLoan(id, "Rechazado");
      fetchLoan();
    };

    const handleApprove = async (e) => {
      e.preventDefault();
        const newLoan = {
            clientLoanId: loan.id,
            fireInsurance,
            deduction
        };
        await ClientLoanValidationService.preApproveLoan(newLoan);
        fetchLoan();
    };

    useEffect(() => {
      if (id) {
          fetchLoan();
      }
    },[id]);

    useEffect(() => {
      setShowAdditionalFields(areAllDocumentsApproved());
    }, [clientDocuments, loan.documents, saving]);

  return (
    <div className="min-h-screen bg-[#282C35] text-white p-6">
      <div className="max-w-4xl mx-auto rounded-lg shadow-xl p-8">
        <h3 className="text-4xl font-medium mb-6 text-white rounded-md p-2">
          Solicitud de Credito
        </h3>
        
        <div className="space-y-4">
          {/* Loan Information Section */}
          <div className="flex flex-col text-center bg-[#2A353D] p-2 text-lg">
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Nombre del Préstamo:</span> {loan.loanName}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Años:</span> {loan.years} años
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Interés:</span> {loan.interest}%
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Monto del Préstamo:</span> ${loan.loanAmount}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Valor de la Propiedad:</span> ${loan.propertyValue}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Porcentaje del prestamo:</span> {loan.loanRatio}%
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Pago Mensual:</span> ${loan.mensualPay}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Cuota/Ingreso:</span> {loan.cuotaIncome}%
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Deuda/Ingreso:</span> {loan.debtCuota}%
            </p>
            <p className="py-2">
            <span className="font-semibold">Fase Actual:</span> 
            <span className={`${
              loan.fase === 'Rechazado' 
                ? 'text-red-500' 
                : loan.fase === 'Desembolso' 
                  ? 'text-green-500' 
                  : 'text-gray-400'
            }`}>
              {loan.fase}
            </span>
          </p>
          </div>
  
          {/* Additional Costs Section */}
          {loan.totalCost !== null && (
            <div className="bg-[#382E2C] p-4 rounded-lg space-y-2 text-lg">
              <p className="py-2 border-b border-gray-700">
                <span className="font-semibold">Costo Total:</span> ${loan.totalCost}
                </p>
              <p className="py-2 border-b border-gray-700">
                <span className="font-semibold">Seguro de Degravamen:</span> {loan.deduction}%
                </p>
              <p className="py-2">
                <span className="font-semibold">Seguro de Incendios:</span> ${loan.fireInsurance}</p>
            </div>
          )}

          {loan.message !== null &&(
            <p className = "bg-red-900 text-xl p-2"><strong>Mensaje:</strong> {loan.message}</p>
          )}
  
          {/* Approval Buttons */}
          {loan.fase === "Pre-Aprobada" && (
            <div className="flex space-x-4 justify-center items-center">
              <p className="text-xl">¿Desea aprobar esta solicitud?</p>
              <button 
                onClick={handleClientApproved}
                className="bg-green-600 hover:bg-green-700 px-4 py-2 text-xl rounded-md transition-colors"
              >
                Aprobar
              </button>
              <button 
                onClick={handleClientReject}
                className="bg-red-600 hover:bg-red-700 px-4 py-2 text-xl rounded-md transition-colors"
              >
                Rechazar
              </button>
            </div>
          )}
  
          {/* Documents Section */}
          <div className="bg-[#2A353D] p-4 rounded-lg">
            <h3 className="text-xl font-bold mb-4">Documentos Credito</h3>
            <div className="space-y-3 text-lg">
              {loan?.documents?.map((doc) => (
                <div key={doc.id} className="flex items-center justify-between bg-[#282C35] p-3 rounded-md">
                  <div className="space-x-4">
                    <span>{doc.name}</span>
                    <span className="text-gray-400">{doc.type}</span>
                    <span className={`${doc.approved ? 'text-green-500' : 'text-red-500'}`}>
                      {doc.approved ? "Aprobado" : "No aprobado"}
                    </span>
                  </div>
                  <div className="flex space-x-2">
                    <button 
                      onClick={() => handleDownload(doc.id, doc.name)}
                      className="bg-blue-600 hover:bg-blue-700 px-3 py-1 rounded-md text-sm transition-colors"
                    >
                      Descargar
                    </button>
                    {loan.fase === "En Revision Inicial" && !doc.approved && (
                      <button 
                        onClick={() => handleDocumentApproved(doc.id, doc)}
                        className="bg-green-600 hover:bg-green-700 px-3 py-1 rounded-md text-sm transition-colors"
                      >
                        Aprobar
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
  
          {/* Additional Fields Section */}
          {showAdditionalFields && loan.fase === "En Revision Inicial" && (
            <div className="bg-[#232E37] p-4 rounded-lg space-y-4">
              <h3 className="text-xl font-bold">Para Aprobar debe rellenar estos campos</h3>
              <div className="space-y-3 text-lg">
                <div className="flex flex-col space-y-1">
                  <label htmlFor="fireInsurance">Seguro de Incendios:</label>
                  <input
                    type="number"
                    id="fireInsurance"
                    value={fireInsurance}
                    onChange={(e) => setFireInsurance(e.target.value)}
                    className="form-input px-3 py-2 rounded-md text-black"
                    required
                  />
                </div>
                <div className="flex flex-col space-y-1">
                  <label htmlFor="deduction">Porcentaje de seguro de Desgravamen:</label>
                  <input
                    type="number"
                    id="deduction"
                    value={deduction}
                    onChange={(e) => setDeduction(e.target.value)}
                    className="form-input px-3 py-2 rounded-md text-black"
                    required
                  />
                </div>
                <button 
                  onClick={handleApprove}
                  className="bg-green-600 hover:bg-green-700 px-4 py-2 rounded-md transition-colors"
                >
                  Aprobar
                </button>
              </div>
            </div>
          )}
  
          {/* Client Information Section */}
          <div className="flex flex-col text-center bg-[#2F3429] p-2 text-lg">
            <h3 className="text-xl font-bold mb-4">Informacion cliente</h3>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">RUT:</span> {client.rut}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Nombre:</span> {client.name + " " + client.lastName}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Contacto:</span> {client.contact}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Correo:</span> {client.email}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Sueldo:</span> ${client.mensualIncome}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Deudas:</span> ${client.totalDebt}
            </p>
            <p className="py-2 border-b border-gray-700">
              <span className="font-semibold">Antiguedad Laboral:</span> {client.jobYears} años
            </p>
            <p className="py-2">
              <span className="font-semibold">Tipo de Trabajo:</span> {client.jobType}
            </p>
          </div>

          {/* Documentos Clientes */}
          <div className="bg-[#2A353D] p-4 rounded-lg">
            <h3 className="text-xl font-bold mb-4">Documentos Clientes</h3>
            <div className="space-y-3 text-lg">
              {clientDocuments && clientDocuments.length > 0 ? (
                clientDocuments.map((doc) => (
                  <div key={doc.id} className="flex items-center justify-between bg-[#282C35] p-3 rounded-md">
                    <div className="space-x-4">
                      <span>{doc.name}</span>
                      <span className="text-gray-400">{doc.type}</span>
                      <span className={`${doc.approved ? 'text-green-500' : 'text-red-500'}`}>
                        {doc.approved ? "Aprobado" : "No aprobado"}
                      </span>
                    </div>
                    <div className="flex space-x-2">
                      <button 
                        onClick={() => handleDownload(doc.id, doc.name)}
                        className="bg-blue-600 hover:bg-blue-700 px-3 py-1 rounded-md text-sm transition-colors"
                      >
                        Descargar
                      </button>
                      {loan.fase === "En Revision Inicial" && !doc.approved && (
                        <button 
                          onClick={() => handleDocumentApproved(doc.id, doc)}
                          className="bg-green-600 hover:bg-green-700 px-3 py-1 rounded-md text-sm transition-colors"
                        >
                          Aprobar
                        </button>
                      )}
                    </div>
                  </div>
                ))
              ) : (
                <p>No hay documentos disponibles</p>
              )}
            </div>
          </div>
            
          {/* Savings Account Section */}
          <div className="space-y-4">
            {loan.savings === null && loan.fase === "En Revision Inicial" &&(
              <button 
                onClick={() => handleSavings()}
                className="bg-blue-600 hover:bg-blue-700 px-4 py-2 text-lg rounded-md transition-colors"
              >
                Validar Cuenta de ahorros
              </button>
            )}
            {saving.result === "Revision Adicional" &&(
              <button 
                onClick={() => handleSavings()}
                className="bg-yellow-600 hover:bg-yellow-700 px-4 py-2 text-lg rounded-md transition-colors"
              >
                Validar Cuenta de ahorros
              </button>
            )}
            {loan.savings !== null && saving.result !== "Revision Adicional" && (
              <p className="bg-[#2A353D] p-4 rounded-lg text-lg">
                La cuenta de Ahorros se ha {' '}
                <span className={`${
                  saving.result === 'Aprobado' 
                    ? 'text-green-500 font-semibold' 
                    : saving.result === 'Rechazado' 
                      ? 'text-red-500 font-semibold' 
                      : 'text-gray-400'
                }`}>
                  {saving.result}
                </span>
              </p>
            )}
          </div>
  
          {loan.fase === "En Revision Inicial" && !showMessage &&(
          <button className = "bg-red-900 p-2 px-4 text-xl rounded-md" onClick={() => setShowMessage(true)}>Rechazar</button>
          )}
          {/* Reject Message Section */}
          {showMessage && (
            <div className="bg-[#382E2C] p-4 rounded-lg space-y-4">
              <h3 className="text-xl font-bold">Por favor indique el motivo de rechazo</h3>
              <input
                type="text"
                id="rejectMessage"
                value={rejectMessage}
                onChange={(e) => setRejectMessage(e.target.value)}
                className="w-full px-3 py-2 text-lg rounded-md text-black"
              />
              <div className="flex space-x-4">
                <button 
                  onClick={handleReject}
                  className="bg-red-600 hover:bg-red-700 px-4 py-2 text-xl rounded-md transition-colors"
                >
                  Enviar
                </button>
                <button 
                  onClick={() => setShowMessage(false)}
                  className="bg-gray-600 hover:bg-gray-700 px-4 py-2 text-xl rounded-md transition-colors"
                >
                  Cancelar
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};