import React from 'react';
import { EnforcementActions } from './EnforcementActions';
import { useAuth } from '../contexts/AuthContext';
import { LookupResult } from '../types/lookup';
import './LookupResults.css';

interface Notice {
    id: string;
    type: string;
    description: string;
    amount?: number;
    status: string;
}

interface Warrant {
    id: string;
    type: string;
    description: string;
    status: string;
}

interface LookupResultsProps {
    result: LookupResult | null;
    onRefresh: () => void;
}

export const LookupResults: React.FC<LookupResultsProps> = ({ result, onRefresh }) => {
    const { user } = useAuth();
    const isAdmin = user?.role === 'ADMIN';

    if (!result) {
        return null;
    }

    return (
        <div className="lookup-results">
            <h2>Results for {result.identifier}</h2>
            
            {result.warrants.length > 0 && (
                <div className="warrants-section">
                    <h3>Warrants</h3>
                    {result.warrants.map((warrant) => (
                        <div key={warrant.id} className="warrant-item">
                            <div className="warrant-details">
                                <h4>{warrant.type}</h4>
                                <p>{warrant.description}</p>
                                <span className={`status ${warrant.status.toLowerCase()}`}>
                                    {warrant.status}
                                </span>
                            </div>
                            {isAdmin && warrant.status !== 'ENFORCED' && (
                                <EnforcementActions
                                    identifier={result.identifier}
                                    type="WARRANT"
                                    onEnforcementComplete={onRefresh}
                                />
                            )}
                        </div>
                    ))}
                </div>
            )}

            {result.notices.length > 0 && (
                <div className="notices-section">
                    <h3>Notices</h3>
                    {result.notices.map((notice) => (
                        <div key={notice.id} className="notice-item">
                            <div className="notice-details">
                                <h4>{notice.type}</h4>
                                <p>{notice.description}</p>
                                {notice.amount && (
                                    <p className="amount">Amount: £{notice.amount.toFixed(2)}</p>
                                )}
                                <span className={`status ${notice.status.toLowerCase()}`}>
                                    {notice.status}
                                </span>
                            </div>
                            {isAdmin && notice.status !== 'ENFORCED' && (
                                <EnforcementActions
                                    identifier={result.identifier}
                                    type="FINE"
                                    onEnforcementComplete={onRefresh}
                                />
                            )}
                        </div>
                    ))}
                </div>
            )}

            {result.warrants.length === 0 && result.notices.length === 0 && (
                <p className="no-results">No records found for this identifier.</p>
            )}
        </div>
    );
}; 