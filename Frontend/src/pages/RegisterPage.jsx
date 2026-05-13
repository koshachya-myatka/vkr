import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Loader from '../components/general/Loader';
import SimpleLink from '../components/general/SimpleLink';
import { registerUser } from '../api/api';

export default function RegisterPage() {
    const navigate = useNavigate();

    const [formData, setFormData] = useState({
        username: '',
        password: '',
        name: '',
        surname: '',
        patronymic: '',
        email: '',
        role: 'LABORATORY'
    });
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = e => {
        setFormData(prev => ({
            ...prev,
            [e.target.name]: e.target.value
        }));
    };

    const handleSubmit = async e => {
        e.preventDefault();
        try {
            setLoading(true);
            setError('');
            const response = await registerUser(formData);
            navigate('/login');
        } catch (err) {
            if (err.response) {
                setError(err.response.data?.message);
            } else { setError('Ошибка регистрации'); }
        } finally {
            setLoading(false);
        }
    };

    return (
        <>
            <title>Регистрация</title>

            <div className="auth-page">
                <div className="auth-card">
                    <h1>
                        Регистрация
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
                                Логин *
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
                                Пароль *
                            </label>
                            <input
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>
                                Имя *
                            </label>
                            <input
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>
                                Фамилия *
                            </label>
                            <input
                                type="text"
                                name="surname"
                                value={formData.surname}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>
                                Отчество
                            </label>
                            <input
                                type="text"
                                name="patronymic"
                                value={formData.patronymic}
                                onChange={handleChange}
                            />
                        </div>

                        <div className="form-group">
                            <label>
                                Email *
                            </label>
                            <input
                                type="email"
                                name="email"
                                value={formData.email}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>
                                Роль *
                            </label>
                            <select
                                name="role"
                                value={formData.role}
                                onChange={handleChange}
                            >
                                <option value="LABORATORY">
                                    Лаборатория
                                </option>
                                <option value="PRODUCTION">
                                    Производство
                                </option>
                            </select>
                        </div>

                        {
                            loading
                                ? <Loader size='supersmall' />
                                : <button
                                    type="submit"
                                    className="btn btn-primary"
                                    disabled={loading}
                                >
                                    Зарегистрироваться
                                </button>
                        }

                        <div className="auth-footer">
                            Уже есть аккаунт?
                            {'   '}
                            <Link to="/login" className="link">
                                Войти
                            </Link>
                        </div>
                    </form>
                </div>
            </div>
        </>
    );
}