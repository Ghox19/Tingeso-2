import React from 'react';
import axios from 'axios';
import { getApiUrl } from '../enviroment';

export function PdfUploader({ onUpload, documentType }) {
    const API_URL = getApiUrl();

    const handleFileChange = (event) => {
        handleSubmit(event, event.target.files[0]);
    };

    const handleSubmit = async (event, file) => {
        event.preventDefault();
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axios.post(`${API_URL}/document/jsonConvert`, formData, {
                headers: { 'Content-Type': 'multipart/form-data' },
            });
            onUpload(response.data, documentType); // Pass JSON data to parent component
        } catch (error) {
            console.error('Error al subir el documento!');
        }
    };

    return (
        <div>
            <input className="text-xs" type="file" accept="application/pdf" onChange={handleFileChange} required />
        </div>
    );
}