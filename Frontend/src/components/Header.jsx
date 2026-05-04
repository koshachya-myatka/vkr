import LinkButton from './LinkButton';
import { useLocation } from 'react-router-dom';

export default function Header() {
    const location = useLocation();

    return (
        <div style={{ display: 'flex', gap: '10px' }}>
            <LinkButton title="Лаборатория" to="/laboratory" active={location.pathname.startsWith('/laboratory')} />
            <LinkButton title="Производство" to="/production" active={location.pathname.startsWith('/production')} />
            <LinkButton title="Руководство" to="/management" active={location.pathname.startsWith('/management')} />
        </div>
    );
}