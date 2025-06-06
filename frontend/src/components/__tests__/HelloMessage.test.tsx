import { render, screen, waitFor } from '@testing-library/react';
import { HelloMessage } from '../HelloMessage';

describe('HelloMessage', () => {
    beforeEach(() => {
        // Reset fetch mock before each test
        global.fetch = jest.fn();
    });

    it('shows loading state initially', () => {
        render(<HelloMessage />);
        expect(screen.getByText('Loading...')).toBeInTheDocument();
    });

    it('displays the message from the API', async () => {
        const mockMessage = 'Hello from Spring Boot!';
        (global.fetch as jest.Mock).mockResolvedValueOnce({
            text: () => Promise.resolve(mockMessage)
        });

        render(<HelloMessage />);
        
        await waitFor(() => {
            expect(screen.getByText(mockMessage)).toBeInTheDocument();
        });
    });

    it('shows error message when API call fails', async () => {
        (global.fetch as jest.Mock).mockRejectedValueOnce(new Error('API Error'));

        render(<HelloMessage />);
        
        await waitFor(() => {
            expect(screen.getByText('Error: Failed to fetch message')).toBeInTheDocument();
        });
    });
}); 