import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../contexts/AuthContext';
import { LookupForm } from './LookupForm';

// Mock the fetch function
global.fetch = jest.fn();

const mockOnResult = jest.fn();
const mockOnError = jest.fn();

// Mock JWT token
const mockToken = 'mock.jwt.token';

const renderLookupForm = () => {
    // Set up localStorage with mock token
    localStorage.setItem('auth_token', mockToken);

    return render(
        <BrowserRouter>
            <AuthProvider>
                <LookupForm onResult={mockOnResult} onError={mockOnError} />
            </AuthProvider>
        </BrowserRouter>
    );
};

describe('LookupForm', () => {
    beforeEach(() => {
        jest.clearAllMocks();
        localStorage.setItem('auth_token', mockToken);
    });

    afterEach(() => {
        localStorage.removeItem('auth_token');
    });

    it('renders the form with input and button', () => {
        renderLookupForm();
        
        expect(screen.getByLabelText(/enter driver id or vehicle registration/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /search/i })).toBeInTheDocument();
    });

    it('shows help text with format examples', () => {
        renderLookupForm();
        
        expect(screen.getByText(/driver id format: a1234567/i)).toBeInTheDocument();
        expect(screen.getByText(/vehicle registration format: ab12 cde/i)).toBeInTheDocument();
    });

    it('handles successful lookup', async () => {
        const mockResult = {
            identifier: 'A1234567',
            type: 'DRIVER',
            notices: [],
            warrants: []
        };

        (global.fetch as jest.Mock).mockResolvedValueOnce({
            ok: true,
            json: () => Promise.resolve(mockResult)
        });

        renderLookupForm();
        
        fireEvent.change(screen.getByLabelText(/enter driver id or vehicle registration/i), {
            target: { value: 'A1234567' }
        });
        
        fireEvent.click(screen.getByRole('button', { name: /search/i }));
        
        await waitFor(() => {
            expect(mockOnResult).toHaveBeenCalledWith(mockResult);
            expect(mockOnError).not.toHaveBeenCalled();
        });

        // Verify the fetch call included the auth token
        expect(global.fetch).toHaveBeenCalledWith(
            expect.stringContaining('/api/lookup'),
            expect.objectContaining({
                headers: expect.objectContaining({
                    'Authorization': `Bearer ${mockToken}`
                })
            })
        );
    });

    it('handles lookup error', async () => {
        (global.fetch as jest.Mock).mockRejectedValueOnce(new Error('Lookup failed'));

        renderLookupForm();
        
        fireEvent.change(screen.getByLabelText(/enter driver id or vehicle registration/i), {
            target: { value: 'invalid' }
        });
        
        fireEvent.click(screen.getByRole('button', { name: /search/i }));
        
        await waitFor(() => {
            expect(mockOnError).toHaveBeenCalledWith(
                'Failed to perform lookup. Please check the identifier and try again.'
            );
            expect(mockOnResult).not.toHaveBeenCalled();
        });
    });

    it('disables button while loading', async () => {
        (global.fetch as jest.Mock).mockImplementationOnce(() => new Promise(() => {}));

        renderLookupForm();
        
        fireEvent.change(screen.getByLabelText(/enter driver id or vehicle registration/i), {
            target: { value: 'A1234567' }
        });
        
        fireEvent.click(screen.getByRole('button', { name: /search/i }));
        
        expect(screen.getByRole('button', { name: /searching/i })).toBeDisabled();
    });

    it('handles unauthorized error', async () => {
        (global.fetch as jest.Mock).mockResolvedValueOnce({
            ok: false,
            status: 401
        });

        renderLookupForm();
        
        fireEvent.change(screen.getByLabelText(/enter driver id or vehicle registration/i), {
            target: { value: 'A1234567' }
        });
        
        fireEvent.click(screen.getByRole('button', { name: /search/i }));
        
        await waitFor(() => {
            expect(mockOnError).toHaveBeenCalledWith(
                'Failed to perform lookup. Please check the identifier and try again.'
            );
            expect(mockOnResult).not.toHaveBeenCalled();
        });
    });
}); 