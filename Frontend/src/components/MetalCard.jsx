import SimpleLink from './SimpleLink';
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

            <p>Total: {metal.total ?? 0}</p>
            <p>Arrival: {metal.arrival ?? 0}</p>
            <p>Processing: {metal.processing ?? 0}</p>
            <p>Analysis: {metal.analysis ?? 0}</p>
            <p>Accepted: {metal.accepted ?? 0}</p>
            <p>Defective: {metal.defective ?? 0}</p>
        </div>
    );
}