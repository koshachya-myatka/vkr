import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { getMetalCards } from "../../api/api";
import MetalCard from './MetalCard';
import Loader from '../general/Loader'

export default function MetalGrid() {
    const navigate = useNavigate();

    const { data: metals, isLoading, isError, error } = useQuery({
        queryKey: ['dashboard-metal-grid'],
        queryFn: () => getMetalCards().then(res => res.data),
    });

    if (isLoading) {
        return (
            <Loader size="medium" />
        );
    }

    if (isError) {
        navigate("/error");
    }

    return (
        <>
            {(!metals || metals.length === 0)
                ? (
                    <div className="page-section">
                        <h4>Нет данных</h4>
                    </div>
                )
                : (
                    <div>
                        <h2>Каталог</h2>
                        <div className="grid-cards">
                            {metals && metals.map(m => (
                                <MetalCard key={m.metalType} metal={m} />
                            ))}
                        </div>
                    </div>
                )}
        </>
    );
}