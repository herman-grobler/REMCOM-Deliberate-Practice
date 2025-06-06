import { useState, FormEvent } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { LookupResult } from '../types/lookup';
import './LookupForm.css';

interface LookupFormProps {
    onResult: (result: LookupResult) => void;
    onError: (error: string) => void;
}

export function LookupForm({ onResult, onError }: LookupFormProps) {
    const [identifier, setIdentifier] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    const { token } = useAuth();

    const handleSubmit = async (e: FormEvent) => {
        e.preventDefault();
        setIsLoading(true);
        onError('');

        try {
            const response = await fetch(`/api/lookup?identifier=${encodeURIComponent(identifier)}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (!response.ok) {
                throw new Error('Lookup failed');
            }

            const result = await response.json();
            onResult(result);
        } catch (error) {
            onError('Failed to perform lookup. Please check the identifier and try again.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="lookup-form">
            <div className="form-group">
                <label htmlFor="identifier">Enter Driver ID or Vehicle Registration:</label>
                <input
                    id="identifier"
                    type="text"
                    value={identifier}
                    onChange={(e) => setIdentifier(e.target.value)}
                    placeholder="e.g., A1234567 or AB12 CDE"
                    required
                />
                <small className="help-text">
                    Driver ID format: A1234567<br />
                    Vehicle Registration format: AB12 CDE
                </small>
            </div>
            <button type="submit" disabled={isLoading}>
                {isLoading ? 'Searching...' : 'Search'}
            </button>
        </form>
    );
} 