import { useNavigate } from 'react-router-dom';
import { disconnectWebSocket } from '../../websocket/websocket';

export default function LogoutButton() {
    const navigate = useNavigate();

    const logout = () => {        
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        navigate("/login");
    };

    return (
        <button
            className="btn btn-danger"
            onClick={logout}
        >
            Выйти
        </button>
    );
}