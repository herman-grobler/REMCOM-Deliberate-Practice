import { useState, useEffect } from 'react';

export function HelloMessage() {
    const [message, setMessage] = useState<string>('');
    const [error, setError] = useState<string>('');

    useEffect(() => {
        fetch('/api/hello')
            .then(response => response.text())
            .then(data => setMessage(data))
            .catch(err => setError('Failed to fetch message'));
    }, []);

    if (error) return <div>Error: {error}</div>;
    if (!message) return <div>Loading...</div>;

    return <div>{message}</div>;
} 