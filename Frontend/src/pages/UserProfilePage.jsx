import { useQuery } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import Header from '../components/general/Header';
import Footer from '../components/general/Footer';
import Loader from '../components/general/Loader';
import { getCurrentUser } from '../api/api';

const roleNames = new Map([["LABORATORY", "Лаборатория"], ["PRODUCTION", "Производство"],
["MANAGEMENT", "Руководство"], ["ADMIN", "Админинистратор"]]);

export default function UserProfilePage() {
    const navigate = useNavigate();

    const { data: user, isLoading, isError } = useQuery({
        queryKey: ['current-user'],
        queryFn: () => getCurrentUser().then(res => res.data)
    });

    if (isLoading) {
        return <Loader size="large" />;
    }

    if (isError || !user) {
        navigate('/error');
    }

    return (
        <>
            <title>Профиль</title>

            <Header />

            <main className="page-container">
                <div className="page-section">
                    <h1>Профиль</h1>
                    <div className="divider" />
                </div>

                <div className="card">
                    <div className="profile-header">
                        <div className="profile-avatar">
                            <span className="material-symbols-outlined" style={{ fontSize: "45px" }}>
                                person
                            </span>
                        </div>
                        <div>
                            <h2>{user.surname} {user.name} {user.patronymic}</h2>
                            <h4>@{user.username}</h4>
                        </div>
                    </div>
                    <div className="divider" />
                    <div className="profile-grid">
                        <div className="profile-field">
                            <span className="profile-label">
                                Email
                            </span>
                            <span>
                                {user.email}
                            </span>
                        </div>
                        <div className="profile-field">
                            <span className="profile-label">
                                Роль
                            </span>
                            <span>
                                {roleNames.get(user.role)}
                            </span>
                        </div>
                    </div>
                </div>
            </main>

            <Footer />
        </>
    );
}