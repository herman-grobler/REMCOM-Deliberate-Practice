import React, { useState } from 'react';
import axios from 'axios';
import { useAuth } from '../contexts/AuthContext';

interface EnforcementActionsProps {
    identifier: string;
    type: 'WARRANT' | 'FINE';
    onEnforcementComplete: () => void;
}

interface EnforcementAction {
    id: string;
    type: string;
    identifier: string;
    userId: string;
    timestamp: string;
    status: string;
    notes: string;
}

export const EnforcementActions: React.FC<EnforcementActionsProps> = ({
    identifier,
    type,
    onEnforcementComplete
}) => {
    const [isConfirming, setIsConfirming] = useState(false);
    const [notes, setNotes] = useState('');
    const [error, setError] = useState<string | null>(null);
    const { token } = useAuth();

    const handleEnforce = async () => {
        if (!isConfirming) {
            setIsConfirming(true);
            return;
        }

        try {
            const endpoint = type === 'WARRANT' ? '/api/enforce/warrant' : '/api/enforce/fine';
            const response = await axios.post<EnforcementAction>(
                endpoint,
                { identifier, notes },
                {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }
            );

            if (response.data.status === 'ENFORCED') {
                onEnforcementComplete();
                setIsConfirming(false);
                setNotes('');
                setError(null);
            }
        } catch (err) {
            setError('Failed to enforce action. Please try again.');
            setIsConfirming(false);
        }
    };

    const cancelEnforcement = () => {
        setIsConfirming(false);
        setNotes('');
        setError(null);
    };

    return (
        <div className="enforcement-actions">
            {!isConfirming ? (
                <button
                    className="enforce-button"
                    onClick={handleEnforce}
                >
                    Enforce {type.toLowerCase()}
                </button>
            ) : (
                <div className="enforcement-confirmation">
                    <h3>Confirm Enforcement</h3>
                    <p>Are you sure you want to enforce this {type.toLowerCase()}?</p>
                    <textarea
                        value={notes}
                        onChange={(e) => setNotes(e.target.value)}
                        placeholder="Add enforcement notes..."
                        className="enforcement-notes"
                    />
                    {error && <div className="error-message">{error}</div>}
                    <div className="confirmation-buttons">
                        <button
                            className="confirm-button"
                            onClick={handleEnforce}
                        >
                            Confirm
                        </button>
                        <button
                            className="cancel-button"
                            onClick={cancelEnforcement}
                        >
                            Cancel
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
}; 