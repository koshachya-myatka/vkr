import SimpleLink from '../general/SimpleLink';
import { useLocation } from 'react-router-dom';

export default function MetalCard({ metal }) {
    if (!metal) return null;
    const location = useLocation();

    return (
        <div style={{
            border: '1px solid gray',
            padding: 10,
            borderRadius: 8,
            background: '#fff'
        }}>
            <img
                src={`/metals/${metal.metalType}.png`}
                width={80}
                alt={metal.metalType}
                onError={(e) => {
                    e.target.style.display = 'none';
                }}
            />

            <SimpleLink
                text={<h3>{metal.metalTypeName}</h3>}
                link={location.pathname + '/metals/' + metal.metalType}
                style={{ background: 'gray' }}
            />

            <p>Всего партий: {metal.total ?? 0}</p>
            <p>Поступило: {metal.arrival ?? 0}</p>
            <p>Обработка: {metal.processing ?? 0}</p>
            <p>На анализах: {metal.analysis ?? 0}</p>
            <p>Одобрено: {metal.accepted ?? 0}</p>
            <p>Брак: {metal.defective ?? 0}</p>
        </div>
    );
}