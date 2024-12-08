import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { PdfUploader } from '../components/pdfUploader';

export function Test() {
    const [documents, setDocuments] = useState([]);

    useEffect(() => {
        const fetchDocuments = async () => {
            try {
                const response = await axios.get('http://localhost:8080/document');
                setDocuments(response.data);
            } catch (error) {
                console.error('Error fetching documents', error);
            }
        };

        fetchDocuments();
    }, []);

    const handleDownload = async (id, name) => {
        try {
            const response = await axios.get(`http://localhost:8080/document/download/${id}`, {
                responseType: 'blob',
                headers: {
                    'Accept': 'application/pdf'
                }
            });
    
            // Crear un blob con el tipo correcto
            const blob = new Blob([response.data], { type: 'application/pdf' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', name.endsWith('.pdf') ? name : `${name}.pdf`);
            document.body.appendChild(link);
            link.click();
    
            // Limpieza
            window.URL.revokeObjectURL(url);
            link.remove();
        } catch (error) {
            console.error('Error downloading the document', error);
        }
    };

    return (
        <div>
            <h2>Lista de Documentos</h2>
            <PdfUploader />
            <ul>
                {documents.map((doc) => (
                    <li key={doc.id}>
                        {doc.name}
                        <button onClick={() => handleDownload(doc.id, doc.name)}>Descargar</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}