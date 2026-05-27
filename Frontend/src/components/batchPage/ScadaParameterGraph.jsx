import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Scatter } from 'recharts';

const STATUS_COLORS = {
    NORMAL: '#22c55e',
    WARNING: '#eab308',
    ALARM: '#ef4444'
};

export default function ScadaParameterGraph({ parameter }) {
    const data = parameter.values.map(item => ({
        ...item,
        timestamp: new Date(item.time).toLocaleTimeString(),
        color: STATUS_COLORS[item.status]
    }));

    const alarmPoints = data.filter(
        x => x.status !== 'NORMAL'
    );

    return (
        <div className="flex-column gap-md">
            <div className="flex-between">
                <h3>
                    {parameter.parameter}, {parameter.unit}
                </h3>
                <span className="badge badge-secondary">
                    {parameter.equipmentId}
                </span>
            </div>
            <div style={{ width: '100%', height: 300, minWidth: 0 }}>
                <ResponsiveContainer width="100%" height="100%" initialDimension={{ width: '100%', height: 300 }}>
                    <LineChart data={data}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis
                            dataKey="timestamp"
                            tick={{ fontSize: 12 }}
                        />
                        <YAxis
                            dataKey="value"
                            tick={{ fontSize: 12 }}
                        />
                        <Tooltip content={<CustomTooltip />} />
                        <Line
                            type="monotone"
                            dataKey="value"
                            stroke="#2563eb"
                            strokeWidth={2}
                            dot={<CustomDot />}
                            activeDot={{ r: 4, fill: '#2563eb' }}
                            isAnimationActive={false}
                        />
                    </LineChart>
                </ResponsiveContainer>
            </div>
        </div>
    );
}

function CustomDot(props) {
    const { cx, cy, payload } = props;
    if (!payload || !payload.status || payload.status === 'NORMAL') return null;
    const fill = STATUS_COLORS[payload.status] ?? '#ef4444';
    return <circle cx={cx} cy={cy} r={5} fill={fill} stroke="#fff" strokeWidth={1} />;
}

function CustomTooltip({ active, payload }) {
    if (!active || !payload?.length) return null;
    const d = payload[0].payload;
    return (
        <div style={{
            background: '#225cda1f', border: '1px solid #2257DA33',
            borderRadius: 6, padding: '8px 12px', fontSize: 12, color: '#f3f4f6'
        }}>
            <div>{new Date(d.time).toLocaleTimeString()}</div>
            <div>Значение: <b>{d.value?.toFixed(2)}</b></div>
        </div>
    );
}