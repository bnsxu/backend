import { check, sleep } from 'k6';
import http from 'k6/http';

export const options = {
    vus: 10, // 가상 사용자 수
    duration: '30s', // 테스트 지속 시간
};

export default function () {
    const res = http.get('http://your-api-url/api/endpoint'); // 테스트할 API 엔드포인트
    check(res, {
        'is status 200': (r) => r.status === 200,
    });
    sleep(1); // 각 가상 사용자 간의 대기 시간
}
