const MAX_POINTS = 60;
const WINDOW_MS = 1 * 60 * 1000;

export function optimizeRealtimePoints(values) {
    if (!values || values.length === 0) return [];
    const now = Date.now();
    const recent = values.filter(v => {
        const time = parseTime(v.time);
        return now - time <= WINDOW_MS;
    });

    const warnings = values
        .filter(v => v.status === 'WARNING')
        .sort((a, b) => new Date(a.time) - new Date(b.time));
    const alarms = values
        .filter(v => v.status === 'ALARM')
        .sort((a, b) => new Date(a.time) - new Date(b.time));
    const warningPeaks = extractLocalMaxima(warnings);
    const alarmPeaks = extractLocalMaxima(alarms);

    const merged = [...recent];
    const existingTimes = new Set(recent.map(v => v.time));
    [...warningPeaks, ...alarmPeaks].forEach(peak => {
        if (!existingTimes.has(peak.time)) {
            merged.push(peak);
            existingTimes.add(peak.time);
        }
    });
    merged.sort((a, b) => new Date(a.time) - new Date(b.time));

    if (merged.length > MAX_POINTS) {
        const faultPoints = merged.filter(v => v.status !== 'NORMAL');
        const faultTimes = new Set(faultPoints.map(v => v.time));
        const normalPoints = merged.filter(v => v.status === 'NORMAL');
        const slotsForNormal = MAX_POINTS - faultPoints.length;
        const trimmedNormal = slotsForNormal > 0
            ? normalPoints.slice(-slotsForNormal)
            : [];
        const finalMerged = [...trimmedNormal, ...faultPoints]
            .sort((a, b) => new Date(a.time) - new Date(b.time));
        return finalMerged;
    }
    return merged;
}

function parseTime(timeStr) {
    if (!timeStr) return NaN;
    const hasTimezone = /([Zz]|[+-]\d{2}:\d{2})$/.test(timeStr);
    return new Date(hasTimezone ? timeStr : timeStr + 'Z').getTime();
}

function extractLocalMaxima(points) {
    if (points.length === 0) return [];
    if (points.length === 1) return points;
    const result = [];
    const gaps = [];
    for (let i = 1; i < points.length; i++) {
        gaps.push(new Date(points[i].time) - new Date(points[i - 1].time));
    }
    const avgGap = gaps.length
        ? gaps.reduce((a, b) => a + b, 0) / gaps.length
        : Infinity;
    const threshold = avgGap * 3;
    let groupStart = 0;
    for (let i = 1; i <= points.length; i++) {
        const isLast = i === points.length;
        const gap = isLast ? Infinity : (new Date(points[i].time) - new Date(points[i - 1].time));
        if (isLast || gap > threshold) {
            const group = points.slice(groupStart, i);
            const peak = group.reduce((max, p) => p.value > max.value ? p : max, group[0]);
            result.push(peak);
            groupStart = i;
        }
    }
    return result;
}