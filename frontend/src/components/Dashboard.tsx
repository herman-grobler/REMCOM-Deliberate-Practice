import { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';
import { LookupForm } from './LookupForm';
import { LookupResults } from './LookupResults';
import { LookupResult } from '../types/lookup';
import './Dashboard.css';

export function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [lookupResult, setLookupResult] = useState<LookupResult | null>(null);
  const [error, setError] = useState('');

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const handleLookupResult = (result: LookupResult) => {
    setLookupResult(result);
    setError('');
  };

  const handleLookupError = (errorMessage: string) => {
    setError(errorMessage);
    setLookupResult(null);
  };

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>Dashboard</h1>
        <div className="user-info">
          <p>Welcome, {user?.email}!</p>
          <p>Role: {user?.role}</p>
          <button onClick={handleLogout} className="logout-button">Logout</button>
        </div>
      </header>

      <main className="dashboard-content">
        <LookupForm onResult={handleLookupResult} onError={handleLookupError} />
        
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        {lookupResult && (
          <LookupResults result={lookupResult} />
        )}
      </main>
    </div>
  );
} 