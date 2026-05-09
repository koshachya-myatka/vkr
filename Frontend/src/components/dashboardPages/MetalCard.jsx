import SimpleLink from '../general/SimpleLink';
import { useLocation } from 'react-router-dom';

export default function MetalCard({ metal }) {
    if (!metal) return null;
    const location = useLocation();

    return (
        <div className="card card-hover info-card">
            <div className="flex-center">
                <img
                    src={`/metals/${metal.metalType}.png`}
                    width={80}
                    alt={metal.metalType}
                    onError={(e) => {
                        e.target.style.display = 'none';
                    }}
                />
            </div>

            <SimpleLink
                text={<h3 className="card-title"> {metal.metalTypeName}</h3>}
                link={location.pathname + '/metals/' + metal.metalType}
                style={{ textDecoration: 'none' }}
            />

            <div className="divider" />

            <div className="stat-block">
                <span className="stat-label">
                    Всего партий
                </span>
                <span className="stat-value">
                    {metal.total ?? 0}
                </span>
            </div>
            <div className="flex-between">
                <small>Поступило</small>
                <span>{metal.arrival ?? 0}</span>
            </div>
            <div className="flex-between">
                <small>Обработка</small>
                <span>{metal.processing ?? 0}</span>
            </div>
            <div className="flex-between">
                <small>На анализах</small>
                <span>{metal.analysis ?? 0}</span>
            </div>
            <div className="flex-between">
                <small>Одобрено</small>
                <span className="badge badge-success">
                    {metal.accepted ?? 0}
                </span>
            </div>
            <div className="flex-between">
                <small>Брак</small>
                <span className="badge badge-danger">
                    {metal.defective ?? 0}
                </span>
            </div>
        </div>
    );
}