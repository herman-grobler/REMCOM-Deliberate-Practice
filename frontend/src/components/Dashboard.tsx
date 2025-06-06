import { useAuth } from '../contexts/AuthContext';
import { useNavigate } from 'react-router-dom';

export function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="dashboard">
      <h1>Dashboard</h1>
      <div className="user-info">
        <p>Welcome, {user?.email}!</p>
        <p>Role: {user?.role}</p>
      </div>
      <button onClick={handleLogout}>Logout</button>
    </div>
  );
} 