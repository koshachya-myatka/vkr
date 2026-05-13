import HeaderLinkButton from './HeaderLinkButton';
import LogoutButton from '../security/LogoutButton';
import { useLocation } from 'react-router-dom';

export default function Header() {
    const location = useLocation();
    const role = localStorage.getItem('role');
    const isAdmin = role === "ADMIN";
    const isManager = role === "MANAGEMENT";
    const isProd = role === "PRODUCTION";
    const isLab = role === "LABORATORY";

    return (
        <header className="header">
            <div className="header-left">
                <div className="header-logo">
                    <span className="material-symbols-outlined">
                        Assessment
                    </span>
                    Витрина качества металлов
                </div>
            </div>
            <nav className="header-nav">
                {(isAdmin || isManager || isLab)
                    && <HeaderLinkButton text="Лаборатория" link="/laboratory" active={location.pathname.startsWith('/laboratory')} />}
                {(isAdmin || isManager || isProd)
                    && <HeaderLinkButton text="Производство" link="/production" active={location.pathname.startsWith('/production')} />}
                {(isAdmin || isManager)
                    && <HeaderLinkButton text="Руководство" link="/management" active={location.pathname.startsWith('/management')} />}
                {isAdmin
                    && <HeaderLinkButton text="Управление" link="/admin" active={location.pathname.startsWith('/admin')} />}

                <LogoutButton />
            </nav>
        </header>
    );
}