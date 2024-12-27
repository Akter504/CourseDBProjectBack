import React, { useState } from 'react';
import axiosInstance from '../utils/axiosInstance';

const AdminBackupRestore = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [message, setMessage] = useState('');

    const handleBackup = async () => {
        setLoading(true);
        setError(null);
        setMessage('');
        try {
            const response = await axiosInstance.get('/backup', { responseType: 'blob' });
            const url = window.URL.createObjectURL(new Blob([response.data]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', 'db_backup.sql');
            document.body.appendChild(link);
            link.click();
            setMessage('Backup created successfully.');
        } catch (error) {
            setError('Error creating backup.');
            console.error('Error creating backup', error);
        } finally {
            setLoading(false);
        }
    };

    const handleRestore = async (file) => {
        setLoading(true);
        setError(null);
        setMessage('');
        const formData = new FormData();
        formData.append('file', file);

        try {
            const response = await axiosInstance.post('/restore', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data',
                },
            });
            setMessage('Restore completed successfully.');
            console.log('Restore completed:', response.data);
        } catch (error) {
            setError('Error restoring backup.');
            console.error('Error restoring backup', error);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div>
            <button onClick={handleBackup} disabled={loading}>
                {loading ? 'Creating Backup...' : 'Create Backup'}
            </button>
            <input
                type="file"
                onChange={(e) => handleRestore(e.target.files[0])}
                accept=".sql, .dump"
                disabled={loading}
            />
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {message && <p style={{ color: 'green' }}>{message}</p>}
        </div>
    );
};

export default AdminBackupRestore;