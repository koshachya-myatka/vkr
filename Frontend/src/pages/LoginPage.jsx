import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Loader from '../components/general/Loader';
import { loginUser } from '../api/api';

export default function LoginPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = e => {
        setFormData(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const redirectByRole = role => {
        switch (role) {
            case 'LABORATORY':
                navigate('/laboratory');
                break;
            case 'PRODUCTION':
                navigate('/production');
                break;
            case 'MANAGEMENT':
                navigate('/management');
                break;
            case 'ADMIN':
                navigate('/admin');
                break;
            default:
                navigate('/');
        }
    };

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            setLoading(true);
            setError('');            
            const response = await loginUser(formData);
            const { token, role } = response.data;
            localStorage.setItem('token', token);
            localStorage.setItem('role', role);
            redirectByRole(role);
        } catch (err) {
            if (err.response) {
                setError(err.response.data?.message);
            } else { setError('Ошибка авторизации'); }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <title>Вход</title>

            <div className="auth-page">
                <div className="auth-card">
                    <h1>
                        Вход
                    </h1>

                    {error && (
                        <div className="error-text">
                            {error}
                        </div>
                    )}

                    <form
                        onSubmit={handleSubmit}
                        className="flex-column gap-lg"
                    >
                        <div className="form-group">
                            <label>
                                Логин
                            </label>
                            <input
                                type="text"
                                name="username"
                                value={formData.username}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>
                                Пароль
                            </label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        {
                            loading
                                ? <Loader size='supersmall' />
                                : <button
                                    type="submit"
                                    className="btn btn-primary"
                                    disabled={loading}
                                >
                                    Войти
                                </button>
                        }

                        <div className="auth-footer">
                            Нет аккаунта?
                            {'   '}
                            <Link to="/register" className="link">
                                Зарегистрироваться
                            </Link>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
}