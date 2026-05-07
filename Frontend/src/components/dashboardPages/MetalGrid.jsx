import { useQuery } from '@tanstack/react-query';
import { getMetalCards } from "../../api/api";
import MetalCard from './MetalCard';

export default function MetalGrid() {
    const { data: metals, isLoading, isError, error } = useQuery({
        queryKey: ['dashboard-metal-grid'],
        queryFn: () => getMetalCards().then(res => res.data),
    });

    if (isLoading) return <div>Загрузка...</div>;
    if (isError || !metals.length) return <div>Ошибка: {error?.message}</div>;

    return (
        <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(4, 1fr)',
            gap: '10px'
        }}>
            {metals && metals.map(m => (
                <MetalCard key={m.metalType} metal={m} />
            ))}
        </div>
    );
}