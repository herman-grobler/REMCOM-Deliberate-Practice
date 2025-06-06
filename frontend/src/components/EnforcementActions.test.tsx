import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { EnforcementActions } from './EnforcementActions';
import { AuthProvider } from '../contexts/AuthContext';
import axios from 'axios';

// Mock axios
jest.mock('axios');
const mockedAxios = axios as jest.Mocked<typeof axios>;

// Mock the auth context
jest.mock('../contexts/AuthContext', () => ({
    useAuth: () => ({
        token: 'mock-token'
    })
}));

describe('EnforcementActions', () => {
    const mockProps = {
        identifier: 'A1234567',
        type: 'WARRANT' as const,
        onEnforcementComplete: jest.fn()
    };

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('renders enforce button initially', () => {
        render(
            <AuthProvider>
                <EnforcementActions {...mockProps} />
            </AuthProvider>
        );

        expect(screen.getByText('Enforce warrant')).toBeInTheDocument();
    });

    it('shows confirmation dialog when enforce button is clicked', () => {
        render(
            <AuthProvider>
                <EnforcementActions {...mockProps} />
            </AuthProvider>
        );

        fireEvent.click(screen.getByText('Enforce warrant'));

        expect(screen.getByText('Confirm Enforcement')).toBeInTheDocument();
        expect(screen.getByText('Are you sure you want to enforce this warrant?')).toBeInTheDocument();
        expect(screen.getByPlaceholderText('Add enforcement notes...')).toBeInTheDocument();
        expect(screen.getByText('Confirm')).toBeInTheDocument();
        expect(screen.getByText('Cancel')).toBeInTheDocument();
    });

    it('calls API and triggers callback on successful enforcement', async () => {
        mockedAxios.post.mockResolvedValueOnce({
            data: {
                id: 'action1',
                type: 'WARRANT',
                identifier: 'A1234567',
                userId: 'admin@example.com',
                timestamp: new Date().toISOString(),
                status: 'ENFORCED',
                notes: 'Test notes'
            }
        });

        render(
            <AuthProvider>
                <EnforcementActions {...mockProps} />
            </AuthProvider>
        );

        // Click enforce button
        fireEvent.click(screen.getByText('Enforce warrant'));

        // Add notes
        fireEvent.change(screen.getByPlaceholderText('Add enforcement notes...'), {
            target: { value: 'Test notes' }
        });

        // Click confirm
        fireEvent.click(screen.getByText('Confirm'));

        await waitFor(() => {
            expect(mockedAxios.post).toHaveBeenCalledWith(
                '/api/enforce/warrant',
                { identifier: 'A1234567', notes: 'Test notes' },
                {
                    headers: {
                        Authorization: 'Bearer mock-token'
                    }
                }
            );
            expect(mockProps.onEnforcementComplete).toHaveBeenCalled();
        });
    });

    it('shows error message on API failure', async () => {
        mockedAxios.post.mockRejectedValueOnce(new Error('API Error'));

        render(
            <AuthProvider>
                <EnforcementActions {...mockProps} />
            </AuthProvider>
        );

        // Click enforce button
        fireEvent.click(screen.getByText('Enforce warrant'));

        // Add notes
        fireEvent.change(screen.getByPlaceholderText('Add enforcement notes...'), {
            target: { value: 'Test notes' }
        });

        // Click confirm
        fireEvent.click(screen.getByText('Confirm'));

        await waitFor(() => {
            expect(screen.getByText('Failed to enforce action. Please try again.')).toBeInTheDocument();
        });
    });

    it('cancels enforcement when cancel button is clicked', () => {
        render(
            <AuthProvider>
                <EnforcementActions {...mockProps} />
            </AuthProvider>
        );

        // Click enforce button
        fireEvent.click(screen.getByText('Enforce warrant'));

        // Add some notes
        fireEvent.change(screen.getByPlaceholderText('Add enforcement notes...'), {
            target: { value: 'Test notes' }
        });

        // Click cancel
        fireEvent.click(screen.getByText('Cancel'));

        // Should return to initial state
        expect(screen.getByText('Enforce warrant')).toBeInTheDocument();
        expect(screen.queryByText('Confirm Enforcement')).not.toBeInTheDocument();
    });
}); 