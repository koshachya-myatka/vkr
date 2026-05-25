import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { getMetalCards } from "../../api/api";
import MetalCard from './MetalCard';
import Loader from '../general/Loader'

const METAL_TYPES = {
    NI: "Никель (Ni)",
    CU: "Медь (Cu)",
    CO: "Кобальт (Co)",
    AG: "Серебро (Ag)",
    AU: "Золото (Au)",
    PD: "Палладий (Pd)",
    PT: "Платина (Pt)",
    RH: "Родий (Rh)",
    IR: "Иридий (Ir)",
    RU: "Рутений (Ru)"
};

export default function MetalGrid() {
    const navigate = useNavigate();

    const { data: metals, isLoading, isError, error } = useQuery({
        queryKey: ['dashboard-metal-grid'],
        queryFn: () => getMetalCards().then(res => res.data),
    });

    useEffect(() => {
        if (isError) {
            const errorMessage = error?.response?.data?.message || null;
            if (error.response?.status === 403) { errorMessage = "У вас нет доступа к этому ресурсу."; }
            navigate('/error', { replace: true, state: { message: errorMessage } });
        }
    }, [isError, error, navigate]);

    if (isLoading) {
        return (
            <Loader size="medium" />
        );
    }

    if (isError) {
        return null;
    }

    const metalCards = Object.entries(METAL_TYPES).map(([metalType, metalTypeName]) => {
        const existingMetal = metals?.find(
            m => m.metalType === metalType
        );
        if (existingMetal) {
            return existingMetal;
        }
        return {
            metalType,
            metalTypeName
        };
    });

    return (
        <>
            <div>
                <h2>Каталог</h2>
                <div className="grid-cards">
                    {metalCards && metalCards.map(m => (
                        <MetalCard key={m.metalType} metal={m} />
                    ))}
                </div>
            </div>
        </>
    );
}