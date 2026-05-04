import { useEffect, useState } from 'react';
import { getMetalCards } from '../api/api';
import MetalCard from './MetalCard';

export default function MetalGrid() {
    const [metals, setMetals] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(false);

    useEffect(() => {
        getMetalCards()
            .then(res => {
                setMetals(res.data);
                setLoading(false);
            })
            .catch(() => {
                setError(true);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return (
            <div style={{ padding: 10 }}>
                <div style={{
                    display: 'grid',
                    gridTemplateColumns: 'repeat(4, 1fr)',
                    gap: '10px'
                }}>
                    {Array.from({ length: 4 }).map((_, i) => (
                        <div key={i} style={{
                            height: 150,
                            background: 'linear-gradient(180deg, #e0e0e0 25%, #f5f5f5 50%, #e0e0e0 75%)',
                            backgroundSize: '100% 200%',
                            animation: 'loading 1.5s infinite'
                        }} />
                    ))}
                </div>

                <style>
                    {`
                        @keyframes loading {
                            0% { background-position: 0% 0%; }
                            100% { background-position: 0% 100%; }
                        }
                    `}
                </style>
            </div>
        );
    }

    if (error || !metals.length) {
        return <div>Данные по металлам не найдены</div>;
    }

    return (
        <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(4, 1fr)',
            gap: '10px'
        }}>
            {metals.map(m => (
                <MetalCard key={m.metalType} metal={m} />
            ))}
        </div>
    );
}